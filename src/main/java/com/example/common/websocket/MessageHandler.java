package com.example.common.websocket;

import io.netty.channel.Channel;

/**
 * @author kfg
 * @date 2023/6/5 10:44
 */
public interface MessageHandler {
    /**
     * 发送信息
     */
    void sendMsg(String message);

    /**
     * 关闭连接
     *
     * @param key
     */
    void close(String key);

    /**
     * 建立连接
     *
     * @param key
     * @param channel
     */
    void connect(String key, Channel channel);
}
