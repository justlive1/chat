package cn.my.chatclient.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.model.MessageData;
import cn.my.chatclient.model.ServerData;
import cn.my.chatclient.swing.WindowsDispacher;
import cn.my.chatclient.util.RSAUtil;
import io.vertx.core.json.Json;

/**
 * 请求操作处理
 * 
 * @author WB
 *
 */

@Component
public class OptsHandler {
	
	@Value("${rsa.server.privateKey}")
	private String privateKey;
	
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
		case ONLINEUSERS:
			handlerOnlineUsers(content);
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
	
	private void handlerOnlineUsers(ServerData content) {
		
		if(content.failed()){
			// 提示注册失败
			dispacher.alert(content.getMsg());
			return;
		}
		
		if(content.getContent() != null){
			
			String decoedData = RSAUtil.decode(content.getContent(), privateKey);
			@SuppressWarnings("unchecked")
			List<String> users = Json.decodeValue(decoedData, List.class);
			
			dispacher.showFriends(users);
			
		}
	}

	private void handleMsgFromOne(ServerData content) {

		if(content.failed() || content.getContent() == null){
			// 提示数据接收失败 通常情况不会出现
			dispacher.alert(content.getMsg());
			return;
		}
		
		
		String decoedData = RSAUtil.decode(content.getContent(), privateKey);
		MessageData data = Json.decodeValue(decoedData, MessageData.class);
		// TODO 显示接收到的信息
		System.out.println(data);
		
	}
	
}
