package com.vking.duhv.meterhub.client.core;

public abstract class MQHandler {
    public void init(SubSystemConnection con) {
    }

    public abstract void receive(Object obj);
}