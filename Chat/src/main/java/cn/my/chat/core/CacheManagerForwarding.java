package cn.my.chat.core;

import java.util.Collection;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public abstract class CacheManagerForwarding implements CacheManager{

	public abstract CacheManager delegate();
	/**
	 * 返回缓存中所有key值
	 * @param name
	 * @return
	 */
	public abstract Set<?> cacheKeys(String name);

	@Override
	public Cache getCache(String name) {
		return delegate().getCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return delegate().getCacheNames();
	}
}
