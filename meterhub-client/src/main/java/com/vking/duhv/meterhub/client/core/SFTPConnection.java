package com.vking.duhv.meterhub.client.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vking.duhv.meterhub.client.core.conf.FTPConfig;
import com.vking.duhv.meterhub.client.core.conf.FTPGZLBConfig;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SFTPConnection extends SubSystemConnection {
    private FTPConfig config;
    @JsonIgnore
    private MeterHubServerClient client;
    @JsonIgnore
    private Class<? extends FTPHandler> handlerClazz;
    @JsonIgnore
    private FTPHandler handler;


    public SFTPConnection(FTPGZLBConfig config, MeterHubServerClient client, Class<? extends FTPHandler> handlerClazz) {
        this.config = config;
        this.client = client;
        this.handlerClazz = handlerClazz;

    }

    @Override
    void start() {
        try {
            handler = handlerClazz.getDeclaredConstructor().newInstance();
            handler.init(this);
        } catch (Exception e) {
            log.error("处理器初始化失败:{}", handlerClazz.getName());
            setStatus(0);
            return;
        }

        handler.run();
    }

    @Override
    void test() {

    }

    @Override
    void destroy() {
        handler.destroy();
    }

    @Override
    void rest() {
        destroy();
        start();
    }

    @Override
    public void rest(ConnectionConfig config) {
        this.config = (FTPGZLBConfig) config;
        this.setId(config.getCode());
        this.rest();
    }

    @Override
    ConnectionConfig getCofig() {
        return this.config;
    }


}
