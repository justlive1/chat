package cn.my.chat.model;

/**
 * 常量类
 * 
 * @author WB
 *
 */
public class Constants {

	public static final String SEPARATE = ">_<";
	
	/**
	 * 操作码枚举
	 */
	public enum OPTIONS {
		REG, // 注册
		LOGIN, // 登录
		SENDTOONE, // 发送给某人
		SNDTOALL,// 发送给所有人
		
		ONLINEUSERS,//在线用户
		
		;

		/**
		 * 当发生异常时，连接是否需要断开
		 * 
		 * @param opt
		 * @return
		 */
		public static boolean isClosed(OPTIONS opt) {
			return opt == null || opt == REG || opt == LOGIN;
		}
	}
}
