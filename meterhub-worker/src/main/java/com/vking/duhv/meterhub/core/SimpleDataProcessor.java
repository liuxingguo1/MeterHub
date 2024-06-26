package com.vking.duhv.meterhub.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.common.Meter;
import com.vking.duhv.meterhub.integration.mydog.analyse104.Decoder104;
import com.vking.duhv.meterhub.integration.mydog.analyse104.message.MessageDetail;
import com.vking.duhv.meterhub.integration.mydog.analyse104.message.MessageInfo;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import com.vking.duhv.meterhub.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleDataProcessor implements DataProcessor{

    @Autowired
    private Decoder104 decoder104;

    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void doProcess(Meter meter) throws JsonProcessingException {
        log.debug(meter.toString());
        // TODO 这里将各个协议的数据进行进行解析，解析完的数据，更具不同的数据类型，再往下游的MQ发送
        // TODO 比如：剩余电流，直接发到 这个 topic  Constant.TOPIC_SHENGYUDIANLIU, 业务系统订阅这个topic，然后存储到数据库中
        String protocal = meter.getProtocol();
        switch (protocal){
            case "IEC104":
                MessageDetail messageDetail = decoder104.encoder(ByteUtil.hexStringToBytes(meter.getData()));
                for (MessageInfo info : messageDetail.getMessages()){
                    info.setSubsystemCode(meter.getCode());
                }
                String json = mapper.writeValueAsString(messageDetail);
                kafkaSender.send(Constant.TOPIC_SHENGYUDIANLIU,"shengyudianliu", json);
                System.out.println("104解析结果为:" + messageDetail);
                break;
            case "IEC103":
                break;
            case "JSON":
                break;
            default:
                break;
        }
    }

}
