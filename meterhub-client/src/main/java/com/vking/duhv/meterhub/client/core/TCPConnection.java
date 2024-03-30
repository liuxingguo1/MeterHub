package com.vking.duhv.meterhub.client.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vking.duhv.meterhub.client.core.conf.TCPConfig;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class TCPConnection extends SubSystemConnection {
    @JsonIgnore
    private Class<? extends ByteToMessageDecoder> decoderClazz;
    @JsonIgnore
    private ByteToMessageDecoder decoder;

    @JsonIgnore
    private TCPConnection.SimpleChannelFutureListener listener = new TCPConnection.SimpleChannelFutureListener();

    @JsonIgnore
    private TCPConnection.SimpleChannelCloseFutureListener closeListener = new TCPConnection.SimpleChannelCloseFutureListener();

    @JsonIgnore
    private EventLoopGroup group;

    @JsonIgnore
    private ChannelFuture future;

    @JsonIgnore
    private Boolean destroy = false;

    @JsonIgnore
    private Channel channel;

    @JsonIgnore
    private Class<? extends TCPHandler> handlerClazz;

    @JsonIgnore
    private TCPHandler tcpHandler = null;

    @JsonIgnore
    private MeterHubServerClient meterHubServerClient;

    private TCPConfig config;

    private Integer retryNum = 0;

    public TCPConnection(TCPConfig config, MeterHubServerClient client, Class<? extends TCPHandler> handlerClazz) {
        this(config, client, handlerClazz, null);

    }

    public TCPConnection(TCPConfig config, MeterHubServerClient client, Class<? extends TCPHandler> handlerClazz, Class<? extends ByteToMessageDecoder> decoderClazz) {
        this.meterHubServerClient = client;
        this.config = config;
        this.handlerClazz = handlerClazz;
        this.setId(config.getCode());
        this.decoderClazz = decoderClazz;
    }

    @Override
    public void start() {
        if (destroy) {
            log.info("{}[{}}], 主动销毁,无需重连, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
            return;
        }
        try {
            tcpHandler = handlerClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("处理器初始化失败:{}", handlerClazz.getName());
            setStatus(0);
            return;
        }
        tcpHandler.init(this);
        if (null != decoderClazz) {
            try {
                decoder = decoderClazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("解码器初始化失败:{}", handlerClazz.getName());
                setStatus(0);
                return;
            }
        }
        doStart();
    }

    private void doStart() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            group = new NioEventLoopGroup();
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
                            if (null != decoder) {
                                pipeline.addLast(decoder);
                            }
                            pipeline.addLast(new ByteArrayEncoder());
                            pipeline.addLast(new ByteArrayDecoder());
                            pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                            pipeline.addLast(tcpHandler);
                        }
                    });
            future = bootstrap.connect(config.getHost(), config.getPort()).sync();
            channel = future.channel();
            future.addListener(listener);
            //对通道关闭进行监听
            ChannelFuture closeFuture = channel.closeFuture().sync();
            closeFuture.addListener(closeListener);
        } catch (Exception e) {
            log.error("{}[{}}], 连接失败请求重试, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
            //关闭线程组
            group.shutdownGracefully();
            restart();
        }
    }

    public void restart() {
        if (destroy) {
            log.info("{}[{}}], 主动销毁,无需重连, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
            return;
        }
        retryNum++;
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ignore) {
        }
        start();
    }

    public void send(String msg) {
        try {
            channel.writeAndFlush(msg);
            log.info("Send Msg: " + msg);
        } catch (Exception e) {
            log.error(this.getClass().getName().concat(".send has error"), e);
        }
    }

    @Override
    void test() {
    }

    @Override
    public void destroy() {
        destroy = true;
        if (null != group) {
            group.shutdownGracefully();
        }
        if (null != channel) {
            try {
                channel.close().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("{}[{}]连接关闭", config.getName(), config.getCode());
    }

    @Override
    public void rest() {
        destroy();
        destroy = false;
        start();
    }

    @Override
    ConnectionConfig getCofig() {
        return this.config;
    }

    @Override
    public void rest(ConnectionConfig config) {
        this.config = (TCPConfig) config;
        this.setId(config.getCode());
        this.rest();
    }

    class SimpleChannelFutureListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws InterruptedException {
            setStatus(1);
            log.info("{}[{}], 连接成功, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
        }
    }

    class SimpleChannelCloseFutureListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture channelFuture) {
            setStatus(0);
            log.info("{}[{}], 连接中断重新连接, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
            restart();
        }
    }

}
