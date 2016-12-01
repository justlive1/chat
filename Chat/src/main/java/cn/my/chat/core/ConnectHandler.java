package cn.my.chat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.my.chat.exception.CodedException;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.model.ClientData;
import cn.my.chat.util.RSAUtil;
import cn.my.chat.util.ThreadStorage;
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

	private Logger logger = LoggerFactory.getLogger(ConnectHandler.class);
	
	@Value("${rsa.client.privateKey}")
	private String privateKey;

	@Autowired
	SessionManager sessionManager;

	@Autowired
	OptsHandler optsHandler;

	@Override
	public void handle(NetSocket socket) {

		socket.handler(buffer -> {
			
			ClientData.OPTIONS opt = null;
			
			try {
				
				String data = buffer.getString(0, buffer.length());
				ClientData resp = Json.decodeValue(data, ClientData.class);

				if (resp.getOption() == null || (opt = ClientData.OPTIONS.valueOf(resp.getOption())) == null) {
					socket.write(optsHandler.result(ErrorCodes.UNKNOWOPTS)).end();
					return;
				}
				
				ThreadStorage.set(opt.name());
				String decodeData = RSAUtil.decode(resp.getContent(), privateKey);
				
				optsHandler.handler(socket, opt, decodeData);

			} catch (CodedException e) {
				socket.write(optsHandler.result(e));
				if(ClientData.OPTIONS.isClosed(opt)){
					socket.end();
				}
			} catch (DecodeException e){
				socket.write(optsHandler.result(ErrorCodes.ILEGALDATA));
				if(ClientData.OPTIONS.isClosed(opt)){
					socket.end();
				}
			} catch (Exception e){
				logger.error("处理异常",e);
				socket.write(optsHandler.result(ErrorCodes.SYSTEMERROR)).end();
			}

		}).closeHandler(r -> {
			// 断开连接
			sessionManager.closed(socket);
			
		}).exceptionHandler(e -> {
			// read exception handler
			
		});
	}

}
