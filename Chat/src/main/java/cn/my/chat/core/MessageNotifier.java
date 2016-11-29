package cn.my.chat.core;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.my.chat.model.Message;

/**
 * 消息通知
 * @author WB
 *
 */

@Component
public class MessageNotifier {

	@EventListener(classes = Message.class)
	@Async
	public void send(Message msg) {

		System.out.println(msg);
	}
}
