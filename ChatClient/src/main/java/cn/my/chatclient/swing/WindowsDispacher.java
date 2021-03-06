package cn.my.chatclient.swing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.core.Callback;
import cn.my.chatclient.util.LocalStorage;

/**
 * 窗体控制器
 * 
 * @author WB
 *
 */
@Component
public class WindowsDispacher {

	@Autowired
	AlertDialog alert;
	@Autowired
	FriendsWindow friends;
	@Autowired
	AuthenticationWindow auth;
	@Autowired
	PrivateChatWindow privateChat;

	/**
	 * 弹出提示窗口
	 * 
	 * @param info
	 */
	public void alert(String info) {
		alert.alert(info);
	}

	public void alert(String info, Callback callback) {
		alert.alert(info, callback);
	}

	public void showFriends(List<String> users) {
		friends.showAllFriend(users);
	}

	public void showFriends() {
		friends.show();
	}

	public void showAuth(int type) {
		switch (type) {
		case 0:
			auth.loginSuccessed();
			break;
		case 1:
			auth.registerSuccessed();
			break;
		default:
			break;
		}
	}

	public void showPrivateChat(String target) {
		privateChat.show(target);
	}

	public void privateChatgMsg(String from, String msg) {
		privateChat.showChatMsg(from, msg);
	}

	public void friendLoginout(String name, boolean in) {

		if (LocalStorage.getUncheck().getName().equals(name)) {
			return;
		}

		if (in) {
			friends.friendLogin(name);
			return;
		}
		friends.friendLogout(name);
	}
}
