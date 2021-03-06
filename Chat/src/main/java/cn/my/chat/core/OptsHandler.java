package cn.my.chat.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.my.chat.exception.CodedException;
import cn.my.chat.exception.ErrorCode;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
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
		case ONLINEUSERS:
			onlineUsers(socket, content);
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

		messageService.send(socket.writeHandlerID(),OPTIONS.REG, null);
	}

	private void login(NetSocket socket, String content) {

		User user = Json.decodeValue(content, User.class);

		if (user == null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
			throw Exceptions.fail(ErrorCodes.ILEGALARGS);
		}

		sessionManager.connected(socket.writeHandlerID(), user);

		messageService.send(socket.writeHandlerID(),OPTIONS.LOGIN, null);
	}

	private void onlineUsers(NetSocket socket, String content) {
		// TODO 按用户好友返回 现返回所有在线用户

		List<String> users = sessionManager.loadAllOnlineUsers();

		messageService.send(socket.writeHandlerID(),OPTIONS.ONLINEUSERS, users);
	}

	private void sendToOne(NetSocket socket, String content) {

		MessageData message = Json.decodeValue(content, MessageData.class);

		UserOnline from = sessionManager.checkOnlineById(socket.writeHandlerID());
		if (!from.getName().equals(message.getFrom())) {
			throw Exceptions.fail(ErrorCodes.ILEGALOPTS);
		}
		UserOnline to = sessionManager.checkOnlineByName(message.getTo());

		messageService.send(to.getHandlerId(),OPTIONS.SENDTOONE, new MessageData(from.getName(), to.getName(), message.getMsg()));
	}

	public String result(CodedException e) {

		ErrorCode errorCode = e.getErrorCode();

		return result(errorCode);
	}

	public String result(ErrorCode e) {

		ServerData data = new ServerData(version, e.getCode(), e.getMsg());
		data.setOption(ThreadStorage.get());

		return data.toJson();
	}

}
