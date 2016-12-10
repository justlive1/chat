package cn.my.chatclient.core;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.my.chatclient.util.Optional;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

@Component
public class VertxManager {

	@Value("${vertx.server.host:localhost}")
	private String host;
	@Value("${vertx.server.port:8000}")
	private int port;

	Optional<String> value = Optional.empty();

	@Autowired
	Vertx vertx;
	@Autowired
	Handler<Buffer> connecHandler;

	public void client() {

		if (value.isWaitingPresent()) {
			return;
		}

		NetClient client = vertx.createNetClient();

		final CountDownLatch latch = new CountDownLatch(1);

		value.of(latch);

		client.connect(port, host, r -> {

			if (r.succeeded()) {

				NetSocket socket = r.result();

				value.of(socket.writeHandlerID());
				latch.countDown();

				socket.handler(connecHandler).exceptionHandler(e -> {
					// read exception handler
				}).closeHandler(c -> {
					// do something after connection closed
					value.clean();
				});
			} else {
				// TODO handler exceptions
			}

		});

	}

	public void send(String msg) {

		if (value.isWaitingPresent()) {
			vertx.eventBus().send(value.getWaitingUncheck(), Buffer.buffer(msg));
		} else {
			System.err.println("local storage is null");
		}
	}
}
