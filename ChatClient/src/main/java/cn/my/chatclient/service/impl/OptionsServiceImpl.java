package cn.my.chatclient.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.my.chatclient.core.VertxManager;
import cn.my.chatclient.model.ClientData;
import cn.my.chatclient.model.MessageData;
import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.model.User;
import cn.my.chatclient.service.OptionsService;
import cn.my.chatclient.util.RSAUtil;
import io.vertx.core.json.Json;

@Service
public class OptionsServiceImpl implements OptionsService {

	@Value("${chat.version}")
	private String version;
	@Value("${rsa.client.publicKey}")
	private String publicKey;

	@Autowired
	VertxManager vertxManager;

	@Override
	public void login(String name, String password) {

		User user = new User(name, password);

		send(OPTIONS.LOGIN, user);
	}

	@Override
	public void register(String name, String password) {

		User user = new User(name, password);

		send(OPTIONS.REG, user);
	}

	@Override
	public void sendToOne(String from, String to, String msg) {

		MessageData message = new MessageData(from, to, msg);

		send(OPTIONS.SENDTOONE, message);
	}

	private <T> void send(OPTIONS opt, T msg) {

		vertxManager.client();

		ClientData data = new ClientData();
		data.setOption(opt.name());
		data.setVersion(version);

		data.setContent(RSAUtil.encode(Json.encode(msg), publicKey));

		vertxManager.send(data.toJson());
	}

}
