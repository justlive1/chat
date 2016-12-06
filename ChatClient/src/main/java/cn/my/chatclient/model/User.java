package cn.my.chatclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * @author WB
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;

}
