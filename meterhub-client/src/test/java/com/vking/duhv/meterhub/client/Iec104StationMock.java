package com.vking.duhv.meterhub.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Iec104StationMock {


    /**
     * 主线程组数量
     */
    private static int bossThread = 1;

    private static int port = 8088;


    public static void main(String[] args) {
        start();
    }

    /**
     * 初始化netty配置
     */
    private static void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThread);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        IEC104StationHandler socketHandler = new IEC104StationHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    /*
                     * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，
                     * 这套机制才会被激活
                     */.option(ChannelOption.SO_KEEPALIVE, true)
                    /*
                     * 1.在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                     * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，
                     * 避免网络中充斥着许多小数据块。
                     * 2.TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                     * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                     */.option(ChannelOption.TCP_NODELAY, true)
                    /*
                     * The timeout period of the connection.
                     * If this time is exceeded or the connection cannot be established, the connection fails.
                     */.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    //给pipeline 设置处理器
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                    pipeline.addLast(socketHandler);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("MeterHub服务器端启动成功，端口: {}", port);
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("MeterHub服务器端启动失败，端口: {}", port, e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


}
