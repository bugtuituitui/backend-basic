package com.example.common.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 当web客户端连接后，触发该方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("建立连接, '{}'", ctx.channel().id().asLongText());
        Channel channel = ctx.channel();
        ConcurrentMessageHandler.getInstance().connect(channel.id().asLongText(), channel);
    }

    // 客户端离线
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("连接关闭, '{}'", ctx.channel().id().asLongText());

        ConcurrentMessageHandler.getInstance().close(ctx.channel().id().asLongText());
        ctx.channel().close();
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发 channelInactive
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());

        // ConcurrentMessageHandler.getInstance().close(ctx.name());
        ctx.channel().close();
    }

    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("异常发生，异常消息 ={}", cause.getMessage());
        ctx.channel().close();
    }

    // 读取客户端发送的请求报文
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // TODO 自定义Service处理请求

        ConcurrentMessageHandler.getInstance().sendMsg(msg.text());
    }

}
