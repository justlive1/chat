package cn.my.chat.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编码方式异常<br>
 * 增加code和msg
 * 
 * @author WB
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class CodedException extends RuntimeException {

	private static final long serialVersionUID = -5249169062884130172L;

	private ErrorCode errorCode;

	public CodedException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public CodedException(ErrorCode errorCode, Throwable e) {
		super(e);
		this.errorCode = errorCode;
	}

}
