package com.vking.duhv.meterhub.client.serverclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MeterHubServerClient {

    private final EventLoopGroup group = new NioEventLoopGroup();

    private SimpleChannelFutureListener listener = new SimpleChannelFutureListener();

    private ChannelFuture future = null;

    private Bootstrap bootstrap;

    private String host;

    private Integer port;

    public MeterHubServerClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @PostConstruct
    private void init() {
        ForkJoinPool.commonPool().submit(this::start);
    }

    private void start() {
        try {
            ClientHandler clientHandler = new ClientHandler(this);
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    /*
                     * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，
                     * 这套机制才会被激活
                     */
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    /*
                     * 1.在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                     * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，
                     * 避免网络中充斥着许多小数据块。
                     * 2.TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                     * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                     */
                    .option(ChannelOption.TCP_NODELAY, true)
                    /*
                     * The timeout period of the connection.
                     * If this time is exceeded or the connection cannot be established, the connection fails.
                     */
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                            pipeline.addLast(clientHandler);
                        }
                    });
            connect();
        } catch (Exception e) {
            log.error("启动netty客户端出现异常", e);
        }
    }

    private void connect() {
        future = bootstrap.connect(host, port);
        future.removeListener(listener);
        future.addListener(listener);
    }

    protected void reconnect() {
        try {
            if (!(future.channel().isOpen() && future.channel().isActive())) {
                future.sync().channel().close();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        connect();
    }

    public void send(String msg) {
        try {
            future.channel().writeAndFlush(msg + "\0");//\0分隔符防止粘包
            log.debug("发送数据到MeterHub服务端: " + msg);
        } catch (Exception e) {
            log.error(this.getClass().getName().concat(".send has error"), e);
        }
    }

    @PreDestroy
    private void destroy() {
        group.shutdownGracefully();
    }

    class SimpleChannelFutureListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture channelFuture) {
            if (future.isSuccess()) {
                log.info("连接MeterHub服务端成功，端口: {}", port);
            } else {
                log.info("连接MeterHub服务端失败，端口: {}", port);
                future.channel().eventLoop().schedule(() -> {
                    log.info("重新连接MeterHub服务端，端口: {}", port);
                    connect();
                }, 10, TimeUnit.SECONDS);
            }
        }
    }

}

