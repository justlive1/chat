package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.AccountService;

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
	
	@Autowired
	VertxManager vertxManager;

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
		
		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		
		UserOnline local = cache.get(handlerId, UserOnline.class);
		// login to another user use the same connection
		if(local != null && !local.getName().equals(user.getName())){
			throw Exceptions.fail(ErrorCodes.ILEGALOPTS);
		}

		UserOnline userOnline =  new UserOnline(user.getName(), handlerId);
		
		ValueWrapper exist = cache.putIfAbsent(user.getName(), userOnline);
		// login to a user which is online
		UserOnline lastUser;
		if(exist != null && !(lastUser = (UserOnline)exist.get()).getHandlerId().equals(handlerId)){
			closedExistingUser(lastUser.getHandlerId());
		}
		
		//save the cache which key is handler
		cache.put(handlerId, userOnline);
		
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

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		UserOnline user;

		if (cache == null || (user = cache.get(handlerId, UserOnline.class)) == null) {
			return;
		}

		if(handlerId.equals(user.getHandlerId())){
			cache.evict(user.getName());
		}
	}

	public UserOnline checkOnline(String name) {

		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		UserOnline user;

		if (cache == null || (user = cache.get(name, UserOnline.class)) == null) {
			throw Exceptions.fail(ErrorCodes.USERNOTONLINE);
		}

		return user;
	}
	
	private void closedExistingUser(String handlerId){
		vertxManager.publish(handlerId+"_closed", ErrorCodes.LOGINONCEMORE);
	}

}
