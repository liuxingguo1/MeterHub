package com.vking.duhv.meterhub.client.core.handler;

import com.vking.duhv.meterhub.client.core.TCPHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class CommonTCPHandler extends TCPHandler {

    private String name;
    private String code;

    public CommonTCPHandler(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Connected to: " + ctx.channel().remoteAddress());
    }

    /**
     * 服务端回写数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info(String.format("收到[%s][%s][%s]消息：%s", name, code, ctx.channel().remoteAddress(), msg.toString()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("Disconnected from: " + ctx.channel().remoteAddress());
    }

    // 捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }

}