package cn.my.chat.model;

import cn.my.chat.model.Constants.OPTIONS;
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
public class Message<T> {

	private String handlerId;
	/**
	 * 消息发送者
	 */
	private OPTIONS opt;
	/**
	 * 消息
	 */
	private T msg;
	
	
}
