package cn.my.chatclient.util;

import java.util.concurrent.CountDownLatch;

import cn.my.chatclient.model.UserLocal;

public class LocalStorage {

	public static final Optional<UserLocal> USER = Optional.empty();
	
	public static boolean isWaitingPresent(){
		return USER.isWaitingPresent();
	}
	
	public static UserLocal getUncheck(){
		return USER.getWaitingUncheck();
	}
	
	public static void of(CountDownLatch latch){
		USER.of(latch);
	}
	
	public static void of(UserLocal user){
		USER.of(user);
	}
	
	public static void clean(){
		USER.clean();
	}
	
	
}
