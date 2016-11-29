package cn.my.chat.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import cn.my.chat.conf.CacheMangagerConfig;
import cn.my.chat.exception.UserNotOnlineException;
import cn.my.chat.model.Message;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.MessageService;

@Service
public class MessageServiceImpl implements ApplicationEventPublisherAware, MessageService {

	private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	ApplicationEventPublisher publisher;

	@Autowired
	CacheManager cacheManager;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		publisher = applicationEventPublisher;
	}

	UserOnline getCache(String name) {
		Cache cache = cacheManager.getCache(CacheMangagerConfig.ONLINES);
		if (cache == null) {
			return null;
		}
		return cache.get(name, UserOnline.class);
	}

	@Override
	public void sendToOne(String from, String to, String msg) {

		if (getCache(from) == null) {
			logger.warn("消息发送用户[{}]不在线", from);
			return;
		}

		UserOnline toUser = getCache(to);

		if (toUser == null) {
			throw new UserNotOnlineException();
		}

		publisher.publishEvent(new Message(from, toUser, msg));
	}

	@Override
	public void sendToAll(String from, String msg) {
		// TODO
	}

}
