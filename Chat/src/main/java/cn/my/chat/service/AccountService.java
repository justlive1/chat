package cn.my.chat.service;

/**
 * 账户服务<br>
 * 包括注册、登陆功能
 * @author WB
 *
 */
public interface AccountService {

	/**
	 * 注册
	 * @param name
	 * @param password
	 */
	void register(String name, String password);
	
	/**
	 * 登陆
	 * @param name
	 * @param password
	 * @return
	 */
	boolean login(String name, String password);
}
