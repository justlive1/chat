package cn.my.chatclient.core;

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

	public NetClient client() {

		NetClient client = vertx.createNetClient();

		client.connect(port, host, r -> {

			if (r.succeeded()) {

				NetSocket socket = r.result();

				if (!value.isPresent()) {
					value.of(socket.writeHandlerID());
				}

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

		return client;
	}

	public void send(String msg) {

		if (value.isPresent()) {
			vertx.eventBus().send(value.get(), Buffer.buffer(msg));
		} else {
			System.err.println("local storage is null");
		}
	}
}
