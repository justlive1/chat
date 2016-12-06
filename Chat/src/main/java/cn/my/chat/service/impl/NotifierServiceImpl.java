package cn.my.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import cn.my.chat.model.Constants;
import cn.my.chat.model.Message;
import cn.my.chat.model.MessageData;
import cn.my.chat.model.ServerData;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.NotifierService;
import cn.my.chat.util.RSAUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;

/**
 * 消息通知<br>
 * 单机模式下Spring事件传播的通知类<br>
 * 异步发送用户消息
 * @author WB
 *
 */

public class NotifierServiceImpl implements NotifierService{

	@Value("${chat.version}")
	private String version;
	
	@Value("${rsa.server.publicKey}")
	private String publicKey;
	
	@Autowired
	EventBus eventBus;
	
	
	@EventListener(classes = Message.class)
	@Async
	@Override
	public void send(Message msg) {

		UserOnline user = msg.getUser();
		
		ServerData data = new ServerData();
		data.setVersion(version);
		data.setOption(Constants.OPTIONS.SENDTOONE.name());
		
		MessageData message = new MessageData(msg.getFrom(),user.getName(),msg.getMsg());
		String encodeData = RSAUtil.encode(Json.encode(message), publicKey);
		data.setContent(encodeData);
		
		eventBus.send(user.getHandlerId(), Buffer.buffer(Json.encode(data)));
	}
}
