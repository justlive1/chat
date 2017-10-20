package cn.my.chat.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.model.Constants.OPTIONS;
import cn.my.chat.service.AccountService;
import cn.my.chat.service.MessageService;

/**
 * 用户会话管理
 * 
 * @author WB
 *
 */
@Component
@CacheConfig(cacheNames = CacheMangagerConfig.ONLINES_ID)
public class SessionManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AccountService accountService;

	@Autowired
	CacheManagerForwarding cacheManager;

	@Autowired
	VertxManager vertxManager;

	@Autowired
	MessageService messageService;

	/**
	 * 用户连接成功<br>
	 * 通过验证则保存会话
	 * 
	 * @param socket
	 * @param user
	 * @return
	 */
	public UserOnline connected(String handlerId, User user) {

		boolean authed = accountService.login(user.getName(), user.getPassword());

		if (!authed) {
			throw Exceptions.fail(ErrorCodes.AUTHFAILD);
		}

		Cache idCache = cacheManager.getCache(CacheMangagerConfig.ONLINES_ID);

		UserOnline local = idCache.get(handlerId, UserOnline.class);
		// login to another user use the same connection
		if (local != null && !local.getName().equals(user.getName())) {
			throw Exceptions.fail(ErrorCodes.ILEGALOPTS);
		}

		UserOnline userOnline = new UserOnline(user.getName(), handlerId);

		Cache nameCache = cacheManager.getCache(CacheMangagerConfig.ONLINES_NAME);

		ValueWrapper exist = nameCache.putIfAbsent(user.getName(), userOnline);
		// login to a user which is online
		UserOnline lastUser;
		if (exist != null && !(lastUser = (UserOnline) exist.get()).getHandlerId().equals(handlerId)) {
			nameCache.put(user.getName(), userOnline);
			closedExistingUser(lastUser.getHandlerId());
		}

		// save the cache which key is handler
		idCache.put(handlerId, userOnline);

		this.notifyOnlineUsersForLogin(user.getName());

		return userOnline;
	}

	/**
	 * 用户断开连接<br>
	 * 清除会话
	 * 
	 * @param socket
	 */
	@CacheEvict(key = "#handlerId")
	public void closed(String handlerId) {

		Cache idCache = cacheManager.getCache(CacheMangagerConfig.ONLINES_ID);
		UserOnline user;

		if (idCache == null || (user = idCache.get(handlerId, UserOnline.class)) == null) {
			return;
		}

		Cache nameCache = cacheManager.getCache(CacheMangagerConfig.ONLINES_NAME);
		user = nameCache.get(user.getName(), UserOnline.class);

		if (user != null && handlerId.equals(user.getHandlerId())) {
			nameCache.evict(user.getName());
		}

		this.notifyOnlineUsersForLogout(user.getName());
	}

	public UserOnline checkOnlineByName(String name) {

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES_NAME);
		UserOnline user;

		if (cache == null || (user = cache.get(name, UserOnline.class)) == null) {
			throw Exceptions.fail(ErrorCodes.USERNOTONLINE);
		}

		return user;
	}

	public UserOnline checkOnlineById(String id) {

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES_ID);
		UserOnline user;

		if (cache == null || (user = cache.get(id, UserOnline.class)) == null) {
			throw Exceptions.fail(ErrorCodes.USERNOTONLINE);
		}

		return user;
	}

	public List<String> loadAllOnlineUsers() {

		Set<?> keys = cacheManager.cacheKeys(CacheMangagerConfig.ONLINES_NAME);

		return keys.stream().map(String::valueOf).collect(Collectors.toList());
	}

	private void closedExistingUser(String handlerId) {

		logger.warn("断开已登陆的连接,{}", handlerId);

		vertxManager.publish(handlerId + "_closed", ErrorCodes.LOGINONCEMORE);
	}

	private void notifyOnlineUsersForLogin(String name) {

		Set<?> keys = cacheManager.cacheKeys(CacheMangagerConfig.ONLINES_ID);

		keys.stream().map(String::valueOf).forEach(item -> {
			messageService.send(item, OPTIONS.FRIENDLOGIN, name);
		});
	}

	private void notifyOnlineUsersForLogout(String name) {

		Set<?> keys = cacheManager.cacheKeys(CacheMangagerConfig.ONLINES_ID);

		keys.stream().map(String::valueOf).forEach(item -> {
			messageService.send(item, OPTIONS.FRIENDLOGOUT, name);
		});
	}

}
