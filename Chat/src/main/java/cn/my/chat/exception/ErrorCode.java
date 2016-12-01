package cn.my.chat.exception;

import java.io.Serializable;

import lombok.Data;

@Data
public class ErrorCode implements Serializable {

	private static final long serialVersionUID = 3511523116358724868L;

	private String module;
	private String code;
	private String msg;

	protected ErrorCode(String module, String code) {
		this.module = module;
		this.code = code;
	}

	protected ErrorCode(String module, String code, String msg) {
		this.module = module;
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.module != null) {
			sb.append(module).append("|");
		}
		sb.append(code);
		if (this.msg != null) {
			sb.append("|").append(msg);
		}
		return sb.toString();
	}
}
