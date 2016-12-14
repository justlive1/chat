package cn.my.chatclient.core;

import java.lang.ref.SoftReference;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.my.chatclient.exception.CanceledException;
import cn.my.chatclient.util.Optional;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

@Component
public class VertxManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${vertx.server.host:localhost}")
	private String host;
	@Value("${vertx.server.port:8000}")
	private int port;

	Optional<String> value = Optional.empty();

	@Autowired
	Vertx vertx;
	@Autowired
	Handler<Buffer> connecHandler;
	@Autowired
	OptsHandler optHandler;
	
	SoftReference<NetClient> softClient;

	public void client() {

		if (value.isWaitingPresent()) {
			return;
		}

		softClient = new SoftReference<NetClient>(vertx.createNetClient(new NetClientOptions().setLogActivity(true)));
		
		final CountDownLatch latch = new CountDownLatch(1);

		value.of(latch);

		softClient.get().connect(port, host, r -> {

			if (r.succeeded()) {

				NetSocket socket = r.result();

				value.of(socket.writeHandlerID());
				latch.countDown();

				socket.handler(connecHandler).exceptionHandler(e -> {
					// read exception handler
					logger.error("",e);
				}).closeHandler(c -> {
					// do something after connection closed
					value.clean();
				});
			} else {
				// handler exceptions
				logger.error("client connect failed,", r.cause());
				value.clean();
				optHandler.connectFailed();
			}

		});

	}

	public void send(String msg) {

		if (value.isWaitingPresent()) {
			String handerId;
			try {
				handerId = value.getWaitingUncheck();
			} catch (CanceledException e) {
				return;
			}
			vertx.eventBus().send(handerId, Buffer.buffer(msg));
		} else {
			logger.warn("local storage is null");
		}
	}
}
