package cn.my.chat.exception;

/**
 * 认证失败
 * @author WB
 *
 */
public class AuthFailedExcetion extends RuntimeException {

	private static final long serialVersionUID = -5068449231788263200L;
	
	public AuthFailedExcetion() {
		super();
	}

	public AuthFailedExcetion(String message) {
		super(message);
	}

	public AuthFailedExcetion(String message, Throwable cause) {
		super(message, cause);
	}

}
