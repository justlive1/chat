package cn.my.chat.core;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.my.chat.exception.CodedException;
import cn.my.chat.exception.ErrorCode;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.model.ClientData;
import cn.my.chat.model.Constants;
import cn.my.chat.util.RSAUtil;
import cn.my.chat.util.ThreadStorage;
import io.vertx.core.Handler;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetSocket;

/**
 * socket连接处理<br>
 * 1.连接的建立<br>
 * 2.最外层数据接收解析<br>
 * 3.连接断开的处理<br>
 * 4.数据读取异常处理
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
	@Autowired
	VertxManager vertxManager;

	@Override
	public void handle(NetSocket socket) {

		socket.handler(buffer -> {

			String data = buffer.getString(0, buffer.length());

			Arrays.asList(data.split(Constants.SEPARATE)).forEach(line -> {

				Constants.OPTIONS opt = null;

				try {

					ClientData resp = Json.decodeValue(line, ClientData.class);

					if (resp.getOption() == null || (opt = Constants.OPTIONS.valueOf(resp.getOption())) == null) {
						socket.write(optsHandler.result(ErrorCodes.UNKNOWOPTS)).end();
						return;
					}

					ThreadStorage.set(opt.name());
					String decodeData = null;
					if (resp.getContent() != null) {
						decodeData = RSAUtil.decode(resp.getContent(), privateKey);
					}

					optsHandler.handler(socket, opt, decodeData);
				} catch (CodedException e) {
					socket.write(optsHandler.result(e));
					if (Constants.OPTIONS.isClosed(opt)) {
						socket.end();
					}
				} catch (DecodeException e) {
					socket.write(optsHandler.result(ErrorCodes.ILEGALDATA));
					if (Constants.OPTIONS.isClosed(opt)) {
						socket.end();
					}
				} catch (Exception e) {
					logger.error("处理异常", e);
					socket.write(optsHandler.result(ErrorCodes.SYSTEMERROR)).end();
				}

			});

		}).closeHandler(r -> {
			// 断开连接
			sessionManager.closed(socket.writeHandlerID());

		}).exceptionHandler(e -> {
			// read exception handler
			logger.error("",e);
		});

		// 注册异地登陆监听
		vertxManager.<ErrorCode>subscribe(socket.writeHandlerID() + "_closed", msg -> {
			socket.write(optsHandler.result(msg.body())).end();
		});

	}

}
