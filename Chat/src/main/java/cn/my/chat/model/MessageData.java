package cn.my.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageData {

	/**
	 * 消息发送者
	 */
	private String from;
	/**
	 * 消息接收者
	 */
	private String to;
	/**
	 * 消息
	 */
	private String msg;
}
