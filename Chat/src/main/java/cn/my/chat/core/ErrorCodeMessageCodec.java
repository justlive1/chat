package cn.my.chat.core;

import cn.my.chat.exception.ErrorCode;
import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;

public class ErrorCodeMessageCodec implements MessageCodec<ErrorCode, ErrorCode>{

	@Override
	public void encodeToWire(Buffer buffer, ErrorCode s) {
		String json = Json.encode(s);
		byte[] strBytes = json.getBytes(CharsetUtil.UTF_8);
		buffer.appendInt(strBytes.length);
		buffer.appendBytes(strBytes);
	}

	@Override
	public ErrorCode decodeFromWire(int pos, Buffer buffer) {
		int length = buffer.getInt(pos);
		pos += 4;
		byte[] bytes = buffer.getBytes(pos, pos + length);
		String json = new String(bytes, CharsetUtil.UTF_8);
		return Json.decodeValue(json, ErrorCode.class);
	}

	@Override
	public ErrorCode transform(ErrorCode s) {
		return s;
	}

	@Override
	public String name() {
		return getClass().getName();
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
