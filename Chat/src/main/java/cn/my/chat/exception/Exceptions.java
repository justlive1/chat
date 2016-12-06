package cn.my.chat.exception;

/**
 * 异常抛出类
 * @author WB
 *
 */
public class Exceptions {

	/**
	 * 创建ErrorCode
	 * 
	 * @param moduleCode
	 * @param subErrorCode
	 * @return
	 */
	public static ErrorCode errorCode(String moduleCode, String subErrorCode) {
		return new ErrorCode(moduleCode, subErrorCode);
	}

	/**
	 * 创建带错误提示信息的ErrorCode
	 * 
	 * @param module
	 * @param code
	 * @param message
	 * @return
	 */
	public static ErrorCode errorMessage(String module, String code, String message) {
		return new ErrorCode(module, code, message);
	}

	public static CodedException fail(ErrorCode errCode) {
		return new NoStackCodedException(errCode);
	}

	public static CodedException fail(String code, String message) {
		return new NoStackCodedException(new ErrorCode(null, code, message));
	}

	public static CodedException fail(ErrorCode errCode, Throwable e) {
		return new NoStackCodedException(errCode, e);
	}

}
