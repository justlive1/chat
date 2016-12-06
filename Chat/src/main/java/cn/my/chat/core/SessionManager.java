package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.ErrorCodes;
import cn.my.chat.exception.Exceptions;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.AccountService;
import io.vertx.core.eventbus.EventBus;

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
	EventBus eventBust;

	/**
	 * 用户连接成功<br>
	 * 通过验证则保存会话
	 * 
	 * @param socket
	 * @param user
	 * @return
	 */
	@Cacheable(key = "#handlerId")
	public UserOnline connected(String handlerId, User user) {

		boolean authed = accountService.login(user.getName(), user.getPassword());

		if (!authed) {
			throw Exceptions.fail(ErrorCodes.AUTHFAILD);
		}

		UserOnline userOnline =  new UserOnline(user.getName(), handlerId);
		
		ValueWrapper exist = cacheManager.getCache(CacheMangagerConfig.ONLINES).putIfAbsent(user.getName(), userOnline);
		
		if(exist != null){
			UserOnline lastUser = (UserOnline)exist.get();
			closedExistingUser(lastUser.getHandlerId());
		}
		
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
		eventBust.send(handlerId+"_closed", ErrorCodes.LOGINONCEMORE);
	}

}
