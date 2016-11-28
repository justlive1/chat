package cn.my.chat.service;

/**
 * 消息发送<br>
 * 用于处理用户向指定用户或所有人发送消息
 * @author WB
 *
 */
public interface MessageService {
	
	/**
	 * 向指定用户发送消息
	 * @param from
	 * @param to
	 * @param msg
	 */
	void sendToOne(String from, String to, String msg);
	
	/**
	 * 向所有用户发送消息
	 * @param from
	 * @param msg
	 */
	void sendToAll(String from, String msg);
}
