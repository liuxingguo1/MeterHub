package com.vking.duhv.meterhub.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.common.Meter;
import com.vking.duhv.meterhub.mq.KafkaSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Socket拦截器，用于处理客户端的行为
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class SocketHandler extends SimpleChannelInboundHandler<String> {

    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final String topic = "meterhub-server_collect_";

    private ObjectMapper objectMapper;

    private KafkaSender kafkaSender;

    public SocketHandler(ObjectMapper objectMapper, KafkaSender kafkaSender) {
        this.objectMapper = objectMapper;
        this.kafkaSender = kafkaSender;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        log.info("建立TCP连接, 通道ID为: {}, 客户端地址: {}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            Meter meter = objectMapper.readValue(msg, Meter.class);
            kafkaSender.send(topic + meter.getProtocol(), UUID.randomUUID().toString(), msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("收到采集端消息: {}", msg);
        Channel client = clients.find(ctx.channel().id());
        if (client != null) {
            client.writeAndFlush("{code:200}");
        }
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
