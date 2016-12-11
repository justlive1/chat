package cn.my.chat.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

/**
 * Vert.x 启动
 * 
 * @author WB
 *
 */

@Component
public class VertxManager {

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

		vertx.createNetServer(opts).connectHandler(connectHandler).listen(System.out::println);
	}

	/**
	 * 通过连接标识 发送消息
	 * @param handlerId 连接创建时的标识
	 * @param msg
	 */
	public void send(String handlerId, String msg) {
		vertx.eventBus().send(handlerId, Buffer.buffer(msg));
	}
	
	/**
	 * 注册一个事件处理
	 * @param key 事件监听key
	 * @param handler
	 */
	public <T> void subscribe(String key,Handler<Message<T>> handler){
		vertx.eventBus().<T>consumer(key).handler(handler);
	}
	
	/**
	 * 向事件处理发布一个消息
	 * @param handlerId
	 * @param event
	 */
	public <T> void publish(String handlerId, T message){
		vertx.eventBus().send(handlerId, message);
	}
	
}
