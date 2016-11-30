package cn.my.chat.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.AuthFailedExcetion;
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

	/**
	 * 用户连接成功<br>
	 * 通过验证则保存会话
	 * 
	 * @param socket
	 * @param user
	 * @return
	 */
	@Cacheable(key = "#socket")
	public UserOnline connected(NetSocket socket, User user) {

		boolean authed = accountService.login(user.getName(), user.getPassword());

		if (!authed) {
			throw new AuthFailedExcetion();
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

	}
}
