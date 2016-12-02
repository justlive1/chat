package cn.my.chat.service;

import cn.my.chat.model.Message;

/**
 * 消息通知服务
 * @author WB
 *
 */
public interface NotifierService {

	/**
	 * 发送消息
	 * @param msg
	 */
	public void send(Message msg);
}
