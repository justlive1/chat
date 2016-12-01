package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.AccountService;
import io.vertx.core.net.NetSocket;

/**
 * 用户会话管理
 * 
 * @author WB
 *
 */
@Component
@CacheConfig(cacheNames = CacheMangagerConfig.ONLINES)
public class SessionManager {

	@Autowired
	AccountService accountService;

	@Autowired
	CacheManager cacheManager;

	/**
	 * 用户连接成功<br>
	 * 通过验证则保存会话
	 * 
	 * @param socket
	 * @param user
	 * @return
	 */
	@Caching(cacheable = { @Cacheable(key = "#socket"), @Cacheable(key = "#user.name") })
	public UserOnline connected(NetSocket socket, User user) {

		boolean authed = accountService.login(user.getName(), user.getPassword());

		if (!authed) {
			throw Exceptions.fail(ErrorCodes.AUTHFAILD);
		}

		return new UserOnline(user.getName(), socket);
	}

	/**
	 * 用户断开连接<br>
	 * 清除会话
	 * 
	 * @param socket
	 */
	@CacheEvict(key = "#socket")
	public void closed(NetSocket socket) {

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		UserOnline user;

		if (cache == null || (user = cache.get(socket, UserOnline.class)) == null) {
			return;
		}

		cache.evict(user.getName());
	}

	public UserOnline checkOnline(String name) {

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		UserOnline user;

		if (cache == null || (user = cache.get(name, UserOnline.class)) == null) {
			throw Exceptions.fail(ErrorCodes.USERNOTONLINE);
		}

		return user;
	}

}
