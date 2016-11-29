package cn.my.chat.service;

import cn.my.chat.model.UserOnline;

/**
 * 账户服务<br>
 * 包括注册、登陆、登出功能
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
	UserOnline login(String name, String password);
	
	/**
	 * 登出
	 * @param name
	 */
	void logout(String name);
}
