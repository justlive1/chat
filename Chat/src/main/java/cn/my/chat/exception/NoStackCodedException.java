package cn.my.chat.exception;

/**
 * 不带stack的异常
 * @author WB
 *
 */
public class NoStackCodedException extends CodedException {

	private static final long serialVersionUID = -9143455273084933556L;

	protected NoStackCodedException(ErrorCode errorCode) {
		super(errorCode);
	}
	
	protected NoStackCodedException(ErrorCode errorCode,Throwable throwable) {
		super(errorCode,throwable);
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}
