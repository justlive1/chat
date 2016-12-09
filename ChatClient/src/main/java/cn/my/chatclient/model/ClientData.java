package cn.my.chatclient.model;

import io.vertx.core.json.Json;
import lombok.Data;

/**
 * 客户端消息发送基本结构
 * 
 * @author WB
 *
 */

@Data
public class ClientData {

	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 操作码
	 */
	private String option;
	/**
	 * 内容
	 */
	private String content;
	
	public String toJson(){
		String line = Json.encode(this);
		return line + Constants.SEPARATE;
	}
}
