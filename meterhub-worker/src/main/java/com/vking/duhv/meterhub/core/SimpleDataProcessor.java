package com.vking.duhv.meterhub.core;

import com.vking.duhv.meterhub.common.Meter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleDataProcessor implements DataProcessor{
    @Override
    public void doProcess(Meter meter) {
        log.debug(meter.toString());
        // TODO 这里将各个协议的数据进行进行解析，解析完的数据，更具不同的数据类型，再往下游的MQ发送
        // TODO 比如：剩余电流，直接发到 这个 topic  Constant.TOPIC_SHENGYUDIANLIU, 业务系统订阅这个topic，然后存储到数据库中
    }

}
