package com.vking.duhv.meterhub.client.core;

import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class TCPHandler extends ChannelInboundHandlerAdapter {

    public void init(TCPConnection con) {
    }

}