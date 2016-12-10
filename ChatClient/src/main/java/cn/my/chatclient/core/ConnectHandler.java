package cn.my.chatclient.core;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.model.Constants;
import cn.my.chatclient.model.ServerData;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

@Component
public class ConnectHandler implements Handler<Buffer> {

	private Logger logger = LoggerFactory.getLogger(ConnectHandler.class);

	@Autowired
	OptsHandler handler;

	@Override
	public void handle(Buffer buffer) {

		String data = buffer.getString(0, buffer.length());

		Arrays.asList(data.split(Constants.SEPARATE)).forEach(line -> {

			Constants.OPTIONS opt = null;

			try {

				ServerData resp = Json.decodeValue(line, ServerData.class);

				if (resp.getOption() == null || (opt = Constants.OPTIONS.valueOf(resp.getOption())) == null) {
					// unknown options
					return;
				}

				handler.handler(opt, resp);

			} catch (Exception e) {
				logger.error("处理消息出错", e);
			}

		});

	}

}
