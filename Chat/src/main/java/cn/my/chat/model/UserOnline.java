package cn.my.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在线用户缓存对象
 * @author WB
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOnline {

	/**
	 * 用户名
	 */
	private String name;
	
	// 连接id
	private String handlerId;
}
