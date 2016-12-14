package cn.my.chat.service;

import cn.my.chat.model.Constants.OPTIONS;

/**
 * 消息发送<br>
 * 用于处理用户向指定用户或所有人发送消息
 * 
 * @author WB
 *
 */
public interface MessageService {

	/**
	 * 向指定用户发送消息
	 * 
	 * @param handlerId
	 *            指定用户的id
	 * @param msg
	 */
	<T> void send(String handlerId, OPTIONS opt, T msg);

}
