package cn.my.chat.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import cn.my.chat.model.Message;
import cn.my.chat.model.UserOnline;
import cn.my.chat.service.MessageService;

@Service
public class MessageServiceImpl implements ApplicationEventPublisherAware, MessageService {


	ApplicationEventPublisher publisher;


	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		publisher = applicationEventPublisher;
	}


	@Override
	public void sendToOne(String from, UserOnline to, String msg) {

		publisher.publishEvent(new Message(from, to, msg));
	}

	@Override
	public void sendToAll(String from, String msg) {
		// TODO
	}

}
