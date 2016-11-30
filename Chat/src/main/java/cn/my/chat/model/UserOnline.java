package cn.my.chat.model;

import io.vertx.core.net.NetSocket;
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
	
	// 保存连接
	private NetSocket socket;
}
