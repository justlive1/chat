package cn.my.chatclient.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.core.Callback;

/**
 * 窗体控制器
 * @author WB
 *
 */
@Component
public class WindowsDispacher {

	@Autowired
	AlertDialog alert;
	@Autowired
	AuthenticationWindow auth;
	
	/**
	 * 弹出提示窗口
	 * @param info
	 */
	public void alert(String info){
		alert.alert(info);
	}
	
	public void alert(String info, Callback callback){
		alert.alert(info, callback);
	}
	
	public void showAuth(int type) {
		switch(type){
		case 0:
			auth.loginSuccessed();
			break;
		case 1:
			auth.registerSuccessed();
			break;
		}
	}
	
}
