package com.vking.duhv.meterhub.client.core.conf;

import lombok.Data;

@Data
public class TCPIEC104Config extends TCPConfig {

    /**
     * 未被确认的I帧最大数目
     */
    private Integer k = 12;

    /**
     * 最迟确认的I帧最大数目
     */
    private Integer w = 8;

    /**
     * 建立连接的超时
     */
    private Integer t0 = 30;

    /**
     * 发送或测试APDU的超时
     */
    private Integer t1 = 15;

    /**
     * 无数据报文时确认的超时
     */
    private Integer t2 = 10;

    /**
     * 长期空闲状态下的超时 需要发送测试帧
     */
    private Integer t3 = 20;

}
