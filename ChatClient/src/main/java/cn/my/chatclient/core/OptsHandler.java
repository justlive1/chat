package cn.my.chatclient.core;

import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.model.MessageData;
import cn.my.chatclient.model.ServerData;
import io.vertx.core.json.Json;

/**
 * 请求操作处理
 * 
 * @author WB
 *
 */

@Component
public class OptsHandler {

	/**
	 * 处理收到的消息
	 * 
	 * @param opt
	 *            操作类型
	 * @param content
	 *            内容
	 */
	public void handler(OPTIONS opt, ServerData content) {

		switch (opt) {
		case LOGIN:
			login(content);
			break;
		case REG:
			register(content);
			break;
		case SENDTOONE:
			handleMsgFromOne(content);
			break;
		default:
			break;
		}

	}

	private void login(ServerData content) {

		if(content.failed()){
			// TODO 提示登陆失败
			
			return;
		}
		
		// TODO 登陆成功处理
		
	}

	private void register(ServerData content) {

		if(content.failed()){
			// TODO 提示注册失败
			
			return;
		}
		
		// TODO 登陆
		
	}

	private void handleMsgFromOne(ServerData content) {

		if(content.failed() || content.getContent() == null){
			// TODO 提示数据接收失败 通常情况不会出现
			
			return;
		}
		
		MessageData data = Json.decodeValue(content.getContent(), MessageData.class);
		// TODO 显示接收到的信息
		System.out.println(data);
		
	}

}
