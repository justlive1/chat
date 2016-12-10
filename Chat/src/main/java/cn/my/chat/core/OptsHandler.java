package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.my.chat.exception.CodedException;
import cn.my.chat.exception.ErrorCode;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
import cn.my.chat.model.Constants;
import cn.my.chat.model.Constants.OPTIONS;
import cn.my.chat.model.MessageData;
import cn.my.chat.model.ServerData;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.AccountService;
import cn.my.chat.service.MessageService;
import cn.my.chat.util.ThreadStorage;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetSocket;

/**
 * 请求操作处理
 * 
 * @author WB
 *
 */

@Component
public class OptsHandler {

	@Autowired
	SessionManager sessionManager;
	@Autowired
	AccountService accountService;
	@Autowired
	MessageService messageService;
	@Autowired
	VertxManager vertxManager;

	@Value("${chat.version}")
	private String version;

	/**
	 * 处理收到的消息
	 * 
	 * @param opt
	 *            操作类型
	 * @param content
	 *            内容
	 */
	public void handler(NetSocket socket, OPTIONS opt, String content) {

		switch (opt) {
		case REG:
			register(socket, content);
			break;
		case LOGIN:
			login(socket, content);
			break;
		case SENDTOONE:
			sendToOne(socket, content);
			break;
		case SNDTOALL:
			// TODO
			break;
		default:
			break;
		}
	}

	private void register(NetSocket socket, String content) {

		User user = Json.decodeValue(content, User.class);

		if (user == null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
			throw Exceptions.fail(ErrorCodes.ILEGALARGS);
		}

		accountService.register(user.getName(), user.getPassword());

		ServerData data = new ServerData();
		data.setVersion(version);
		data.setOption(OPTIONS.REG.name());

		vertxManager.send(socket.writeHandlerID(), Json.encode(data));
	}

	private void login(NetSocket socket, String content) {

		User user = Json.decodeValue(content, User.class);

		if (user == null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
			throw Exceptions.fail(ErrorCodes.ILEGALARGS);
		}

		sessionManager.connected(socket.writeHandlerID(), user);

		ServerData data = new ServerData();
		data.setVersion(version);
		data.setOption(OPTIONS.LOGIN.name());

		vertxManager.send(socket.writeHandlerID(), Json.encode(data));
	}

	private void sendToOne(NetSocket socket, String content) {

		MessageData message = Json.decodeValue(content, MessageData.class);

		UserOnline from = sessionManager.checkOnline(socket.writeHandlerID());
		if (!from.getName().equals(message.getFrom())) {
			throw Exceptions.fail(ErrorCodes.ILEGALOPTS);
		}
		UserOnline to = sessionManager.checkOnline(message.getTo());

		messageService.sendToOne(message.getFrom(), to, message.getMsg());
	}

	public String result(CodedException e) {

		ErrorCode errorCode = e.getErrorCode();

		return result(errorCode);
	}

	public String result(ErrorCode e) {

		ServerData data = new ServerData(version, e.getCode(), e.getMsg());
		data.setOption(ThreadStorage.get());

		return Json.encode(data) + Constants.SEPARATE;
	}

}
