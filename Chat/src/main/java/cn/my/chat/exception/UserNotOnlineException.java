package cn.my.chat.exception;
/**
 * 用户不在线
 * @author WB
 *
 */
public class UserNotOnlineException extends RuntimeException{

	private static final long serialVersionUID = -1244714477688359743L;

	public UserNotOnlineException() {
		super();
	}

	public UserNotOnlineException(String message) {
		super(message);
	}

	public UserNotOnlineException(String message, Throwable cause) {
		super(message, cause);
	}
}
