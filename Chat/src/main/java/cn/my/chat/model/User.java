package cn.my.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 用户信息
 * @author WB
 *
 */

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 用户名
	 */
	@NonNull
	private String name;
	/**
	 * 密码
	 */
	@NonNull
	private String password;

}
