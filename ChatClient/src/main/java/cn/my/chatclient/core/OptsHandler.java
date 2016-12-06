package cn.my.chatclient.core;

import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants.OPTIONS;
import cn.my.chatclient.model.ServerData;

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


	}
}
