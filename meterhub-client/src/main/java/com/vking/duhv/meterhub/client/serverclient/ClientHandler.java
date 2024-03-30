package com.vking.duhv.meterhub.client.serverclient;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private MeterHubServerClient client;

    public ClientHandler(MeterHubServerClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("已连接MeterHub服务端: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("收到来自MeterHub服务端的消息: {}, {}", ctx.channel().remoteAddress(), msg.toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("MeterHub服务端连接已中断: {}", ctx.channel().remoteAddress());
        client.reconnect();
    }

    // 捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }

}