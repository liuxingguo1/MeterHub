package com.vking.duhv.meterhub.client.core.handler;

import com.vking.duhv.meterhub.client.core.TCPHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IEC103Handler extends TCPHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info(String.format("收到[%s]消息：%s", ctx.channel().remoteAddress(), msg.toString()));
    }

}