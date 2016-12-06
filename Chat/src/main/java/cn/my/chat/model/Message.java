package cn.my.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息发送实体
 * @author WB
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	/**
	 * 消息发送者
	 */
	private String from;
	/**
	 * 消息接收者
	 */
	private UserOnline user;
	/**
	 * 消息
	 */
	private String msg;
	
	
}
