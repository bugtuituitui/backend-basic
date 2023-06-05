package com.example.common.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kfg
 * @date 2023/6/5 10:45
 */
public class ConcurrentMessageHandler implements MessageHandler {

    private static final Map<String, Channel> channels = new HashMap<>();

    private ConcurrentMessageHandler() {
    }

    public static ConcurrentMessageHandler getInstance() {
        return new ConcurrentMessageHandler();
    }

    @Override
    public void sendMsg(String message) {
        channels.forEach((key, channel) -> {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        });
    }

    @Override
    public void close(String key) {
        channels.remove(key);
    }

    @Override
    public void connect(String key, Channel channel) {
        channels.put(key, channel);
    }
}
