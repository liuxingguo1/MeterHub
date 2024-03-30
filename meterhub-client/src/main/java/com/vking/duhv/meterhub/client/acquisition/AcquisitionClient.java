package com.vking.duhv.meterhub.client.acquisition;

import io.netty.channel.Channel;

public interface AcquisitionClient {
    void connect();

    Channel channel();

    void close();

    void report();

    Object healthStats();

}
