package com.vking.duhv.meterhub.client.core;

import lombok.Data;

@Data
public abstract class SubSystemConnection {

    private String id;
    private Integer status;

    abstract void start();

//    abstract void heartbeat();

    abstract void test();

    abstract void destroy();

    abstract void rest() throws InterruptedException;

    abstract ConnectionConfig getCofig();

    public void rest(ConnectionConfig config) {

    }

}
