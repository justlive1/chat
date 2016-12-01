package cn.my.chat.exception;

/**
 * 异常信息集合
 * @author WB
 *
 */
public class ErrorCodes {

	private static final String MODULE = "CHAT";
	
	public static final ErrorCode SYSTEMERROR = Exceptions.errorMessage(MODULE, "99999", "系统异常");
	
	public static final ErrorCode ILEGALARGS = Exceptions.errorMessage(MODULE, "00001", "用户名或密码不正确");
	public static final ErrorCode AUTHFAILD = Exceptions.errorMessage(MODULE, "00002", "用户名或密码不正确");
	public static final ErrorCode NAMEEXIST = Exceptions.errorMessage(MODULE, "00003", "用户名已存在");
	public static final ErrorCode USERNOTONLINE = Exceptions.errorMessage(MODULE, "00004", "用户不在线");
	public static final ErrorCode ILEGALDATA = Exceptions.errorMessage(MODULE, "00005", "数据格式异常");
	public static final ErrorCode UNKNOWOPTS = Exceptions.errorMessage(MODULE, "00006", "未知操作");
	
}
