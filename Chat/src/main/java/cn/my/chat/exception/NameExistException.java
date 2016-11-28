package cn.my.chat.exception;

/**
 * 用户名已存在
 * 
 * @author WB
 *
 */
public class NameExistException extends RuntimeException {

	private static final long serialVersionUID = -1223644094699736270L;

	public NameExistException() {
		super();
	}

	public NameExistException(String message) {
		super(message);
	}

	public NameExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
