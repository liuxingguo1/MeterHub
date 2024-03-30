package com.vking.duhv.meterhub.client.acquisition.protocol.iec104;

import com.vking.duhv.meterhub.client.acquisition.AcquisitionClient;
import com.vking.duhv.meterhub.client.acquisition.AcquisitionMeta;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Iec104Client implements AcquisitionClient {
    private Integer retryNum = 0;
    private Channel channel;
    @Getter
    private AcquisitionConf104 acquisitionMeta;

    public Iec104Client(AcquisitionMeta acquisitionMeta) {
        this.acquisitionMeta = (AcquisitionConf104) acquisitionMeta;
    }

    @Override
    public void connect() {
        Iec104Handler iec104Handler = new Iec104Handler(this);
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, acquisitionMeta.getT0() * 1000);

            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ByteArrayEncoder());
                            pipeline.addLast(new ByteArrayDecoder());
                            pipeline.addLast(iec104Handler);
                        }
                    });
            //连接服务端
            ChannelFuture connectFuture = bootstrap.connect(acquisitionMeta.getIp(), acquisitionMeta.getPort()).sync();
            channel = connectFuture.channel();
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    retryNum = 0;
                }
            });

            //对通道关闭进行监听
            ChannelFuture closeFuture = channel.closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    log.error("{}:连接中断请求重试", acquisitionMeta.getName());
                    //关闭线程组
                    eventExecutors.shutdownGracefully();
                    reconnect();
                }
            });
        } catch (Exception e) {
            log.error("{}:连接失败请求重试", acquisitionMeta.getName());
            //关闭线程组
            eventExecutors.shutdownGracefully();
            reconnect();
        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public void close() {
        try {
            channel.close().sync();
        } catch (InterruptedException ignore) {
        }
    }

    @Override
    public void report() {

    }

    @Override
    public Object healthStats() {
        return "good";
    }

    public void reconnect() {
        retryNum++;
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ignore) {
        }
        connect();
    }

}
