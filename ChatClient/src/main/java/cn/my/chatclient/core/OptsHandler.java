package cn.my.chatclient.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.model.MessageData;
import cn.my.chatclient.model.ServerData;
import cn.my.chatclient.swing.WindowsDispacher;
import io.vertx.core.json.Json;

/**
 * 请求操作处理
 * 
 * @author WB
 *
 */

@Component
public class OptsHandler {
	
	@Autowired
	WindowsDispacher dispacher;

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
			handlerLogin(content);
			break;
		case REG:
			handlerRegister(content);
			break;
		case SENDTOONE:
			handleMsgFromOne(content);
			break;
		default:
			break;
		}

	}
	
	public void connectFailed(){
		
		dispacher.alert("连接服务器失败");
		
	}

	private void handlerLogin(ServerData content) {

		if(content.failed()){
			// 提示登陆失败
			dispacher.alert(content.getMsg());
			return;
		}
		// 登陆成功处理
		dispacher.showAuth(0);
	}

	private void handlerRegister(ServerData content) {

		if(content.failed()){
			// 提示注册失败
			dispacher.alert(content.getMsg());
			return;
		}
		
		// 登陆
		dispacher.showAuth(1);
	}

	private void handleMsgFromOne(ServerData content) {

		if(content.failed() || content.getContent() == null){
			// TODO 提示数据接收失败 通常情况不会出现
			System.out.println(content);
			return;
		}
		
		MessageData data = Json.decodeValue(content.getContent(), MessageData.class);
		// TODO 显示接收到的信息
		System.out.println(data);
		
	}
	
}
