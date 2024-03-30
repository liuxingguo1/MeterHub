package com.vking.duhv.meterhub.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.mq.KafkaSender;
import com.vking.duhv.meterhub.server.SocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 监听Spring容器启动完成，完成后启动Netty服务器
 **/
@Component
public class NettyRunner implements ApplicationRunner {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaSender kafkaSender;

    @Value("${netty.port:9999}")
    private int port;

    @Override
    public void run(ApplicationArguments args) {
        new SocketServer(this.port, this.objectMapper, this.kafkaSender).start();
    }

}
