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
public class OptionsServiceImpl implements OptionsService{

	@Value("${chat.version}")
	private String version;
	@Value("${rsa.client.publicKey}")
	private String publicKey;
	
	@Autowired
	VertxManager vertxManager;
	
	@Override
	public void login(String name, String password) {
		
		vertxManager.client();
		
		ClientData data = new ClientData();
		data.setOption(OPTIONS.LOGIN.name());
		data.setVersion(version);
		
		User user = new User(name, password);
		data.setContent(RSAUtil.encode(Json.encode(user), publicKey));
		
		vertxManager.send(data.toJson());
		
	}

	@Override
	public void register(String name, String password) {
		
		vertxManager.client();
		
		ClientData data = new ClientData();
		data.setOption(OPTIONS.REG.name());
		data.setVersion(version);
		
		User user = new User(name, password);
		data.setContent(RSAUtil.encode(Json.encode(user), publicKey));
		
		vertxManager.send(data.toJson());
		
		
	}

	@Override
	public void sendToOne(String from, String to, String msg) {
		
		ClientData data = new ClientData();
		data.setOption(OPTIONS.SENDTOONE.name());
		data.setVersion(version);
		
		MessageData message = new MessageData(from, to, msg);
		
		data.setContent(RSAUtil.encode(Json.encode(message), publicKey));
		
		vertxManager.send(data.toJson());
		
	}

}
