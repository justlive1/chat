package cn.my.chat.model;

import io.vertx.core.json.Json;
import lombok.Data;

/**
 * 服务端消息发送基本结构
 * 
 * @author WB
 *
 */

@Data
public class ServerData {

	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 返回code 00000：成功 其他错误
	 */
	private String code = "00000";
	/**
	 * 操作
	 */
	private String option;
	/**
	 * 错误信息
	 */
	private String msg;
	/**
	 * 消息正文
	 */
	private String content;

	public ServerData() {
	}

	public ServerData(String version, String code, String msg, String content) {
		super();
		this.version = version;
		this.code = code;
		this.msg = msg;
		this.content = content;
	}

	public ServerData(String version, String code, String msg) {
		super();
		this.version = version;
		this.code = code;
		this.msg = msg;
	}

	public String toJson() {
		String line = Json.encode(this);
		return line + Constants.SEPARATE;
	}
}
