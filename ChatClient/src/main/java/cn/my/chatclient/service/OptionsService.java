package cn.my.chatclient.service;

/**
 * 操作服务<br>
 * 封装连接服务
 * 
 * @author WB
 *
 */
public interface OptionsService {

	void login(String name, String password);

	void register(String name, String password);
	
	void loadOnlineUsers();

	void sendToOne(String from, String to, String msg);
}
