package com.vking.duhv.meterhub.client;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Socket拦截器，用于处理客户端的行为
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class IEC104StationHandler extends ChannelInboundHandlerAdapter {

    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        log.info("建立TCP连接, 通道ID为: {}, 客户端地址: {}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
    }

    boolean start = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (start) {
            return;
        }
        start = true;
        log.debug("收到采集端消息: {}", msg);
        int i = 10000;
        while (i-- > 0) {
            //模拟粘包
            String resp2 = "68 04 83 00 00 00";
            ctx.channel().writeAndFlush(HexUtil.decodeHex(StrUtil.cleanBlank(resp2)));
        }
        start = false;

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("新的客户端连接: {}", ctx.channel().id().asShortText());
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        clients.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
        clients.remove(ctx.channel());
    }

    public void sendMsgToClient(String msg) {
        for (Channel client : clients) {
            if (client != null) {
                client.writeAndFlush(msg);
            }
        }
    }

}
