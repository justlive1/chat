package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chat.exception.AuthFailedExcetion;
import cn.my.chat.model.User;
import io.vertx.core.Handler;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetSocket;

/**
 * socket连接处理<br>
 * 1.验证账户信息<br>
 * 2.保存用户连接
 * 
 * @author WB
 *
 */

@Component
public class ConnectHandler implements Handler<NetSocket> {

	@Autowired
	SessionManager sessionManager;

	@Override
	public void handle(NetSocket socket) {
		
		socket.handler(buffer -> {
			// TODO 消息解析协议
			String data = buffer.getString(0, buffer.length());
			try {
				// TODO 消息加解密
				User user = Json.decodeValue(data, User.class);
				
				sessionManager.connected(socket, user);
				
				socket.write("success!");
			
			} catch (DecodeException e) {
				socket.write("illegal data!").end();
			} catch (AuthFailedExcetion e) {
				socket.write("auth failed!").end();
			}
			
		}).closeHandler(r -> {
			// 断开连接
			sessionManager.closed(socket);
		});
	}

}
