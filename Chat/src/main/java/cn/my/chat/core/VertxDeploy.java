package cn.my.chat.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

/**
 * Vert.x 启动
 * 
 * @author WB
 *
 */

@Component
public class VertxDeploy {

	@Value("${vertx.server.port:8000}")
	Integer port;
	@Value("${vertx.server.logActivity:false}")
	Boolean logActivity;

	@Autowired
	Vertx vertx;

	@Autowired
	Handler<NetSocket> connectHandler;

	@PostConstruct
	void init() {

		NetServerOptions opts = new NetServerOptions().setPort(port).setLogActivity(logActivity);

		vertx.createNetServer(opts)
		.connectHandler(connectHandler)
		.listen(System.out::println);
	}
}
