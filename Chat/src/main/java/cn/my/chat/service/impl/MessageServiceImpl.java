package cn.my.chat.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import cn.my.chat.model.Constants.OPTIONS;
import cn.my.chat.model.Message;
import cn.my.chat.service.MessageService;

/**
 * 消息发送实现类<br>
 * 单机模式下采用的是Spring的事件传播<br>
 * 这里只是消息真正发送前的处理<br>
 * 消息由消息通知服务异步发送给接收者
 * @author WB
 *
 */

public class MessageServiceImpl implements ApplicationEventPublisherAware, MessageService {


	ApplicationEventPublisher publisher;


	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		publisher = applicationEventPublisher;
	}


	@Override
	public <T> void send(String handlerId, OPTIONS opt, T msg) {

		publisher.publishEvent(new Message<T>(handlerId, opt, msg));
	}

}
