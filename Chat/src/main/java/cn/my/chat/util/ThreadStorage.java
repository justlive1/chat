package cn.my.chat.util;

public class ThreadStorage {

	private static ThreadLocal<Object> local = new ThreadLocal<>();

	public static void set(Object value) {
		local.set(value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get() {
		return (T) local.get();
	}
}
