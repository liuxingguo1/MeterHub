package com.vking.duhv.meterhub.client.acquisition.protocol.iec104;

import com.vking.duhv.meterhub.client.acquisition.AcquisitionMeta;
import lombok.Data;

@Data
public class AcquisitionConf104 extends AcquisitionMeta {
    private Integer k = 12;//未被确认的I帧最大数目
    private Integer w = 8;//最迟确认的I帧最大数目
    private Integer t0 = 30;//建立连接的超时
    private Integer t1 = 15;//发送或测试APDU的超时
    private Integer t2 = 10;//无数据报文时确认的超时
    private Integer t3 = 20;//长期空闲状态下的超时 需要发送测试帧

    public AcquisitionConf104() {
        super();
    }

    public AcquisitionConf104(String name, String protocol, String ip, Integer port) {
        super(name, protocol, ip, port);
    }
}
