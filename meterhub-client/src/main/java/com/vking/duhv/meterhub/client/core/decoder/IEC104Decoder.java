package com.vking.duhv.meterhub.client.core.decoder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class IEC104Decoder extends LengthFieldBasedFrameDecoder {
    //标记长度区域 [启动字符1字节，长度字符一字节，数据。。。。。。。。。]
    public IEC104Decoder() {
        super(255, 1, 1);
    }
}
