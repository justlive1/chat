package cn.my.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.google.common.base.Charsets;

import cn.my.chat.dao.UserDao;
import cn.my.chat.exception.AuthFailedExcetion;
import cn.my.chat.exception.NameExistException;
import cn.my.chat.model.User;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.AccountService;

@Service
@CacheConfig(cacheNames="onlines")
public class AccountServiceImpl implements AccountService{

	@Autowired
	UserDao userDao;
	@Autowired
	CacheManager cacheManager;
	
	@Transactional
	@Override
	public void register(String name, String password) {
		//检查name是否重复
		boolean isExist = userDao.isExistForName(name);
		if(isExist){
			throw new NameExistException();
		}
		//保存账号
		String pwd = DigestUtils.md5DigestAsHex(password.getBytes(Charsets.UTF_8));
		userDao.save(new User(name,pwd));
	}

	@Cacheable(key="#name")
	@Override
	public UserOnline login(String name, String password) {
		//验证账号
		String pwd = DigestUtils.md5DigestAsHex(password.getBytes(Charsets.UTF_8));
		boolean authed = userDao.auth(name, pwd);
		
		if(!authed){
			throw new AuthFailedExcetion();
		}
		
		return new UserOnline(name);
	}

	@CacheEvict(key="#name")
	@Override
	public void logout(String name) {
		//TODO do something before user logout
	}

}
