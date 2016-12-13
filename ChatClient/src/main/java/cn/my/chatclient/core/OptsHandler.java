package cn.my.chatclient.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.awt.AlertWindow;
import cn.my.chatclient.awt.AuthenticationWindow;
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
	
	@Autowired
	AlertWindow alert;
	@Autowired
	AuthenticationWindow auth;

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
		
		alert.alert("连接服务器失败", () -> {
			auth.authFailed();
		});
		
	}

	private void handlerLogin(ServerData content) {

		if(content.failed()){
			// 提示登陆失败
			alert.alert(content.getMsg(), () -> {
				auth.authFailed();
			});
			return;
		}
		// 登陆成功处理
		auth.loginSuccessed();
	}

	private void handlerRegister(ServerData content) {

		if(content.failed()){
			// 提示注册失败
			alert.alert(content.getMsg(), () -> {
				auth.authFailed();
			});
			return;
		}
		
		// 登陆
		auth.registerSuccessed();
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
