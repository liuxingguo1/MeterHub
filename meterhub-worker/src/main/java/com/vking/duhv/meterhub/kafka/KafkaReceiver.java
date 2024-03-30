package com.vking.duhv.meterhub.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaReceiver {

    @KafkaListener(topics = {"meterhub-server_collect_IEC000"}, groupId = "meterhub-server")
    public void receiveIEC000(ConsumerRecord<?, ?> record){
        log.debug("收到消息[IEC000]: {}", record.value());
    }

    @KafkaListener(topics = {"meterhub-server_collect_IEC103"}, groupId = "meterhub-server")
    public void receiveIEC103(ConsumerRecord<?, ?> record){
        log.debug("收到消息[IEC103]: {}", record.value());
    }

    @KafkaListener(topics = {"meterhub-server_collect_IEC104"}, groupId = "meterhub-server")
    public void receiveIEC104(ConsumerRecord<?, ?> record){
        log.debug("收到消息[IEC104]: {}", record.value());
    }

    @KafkaListener(topics = {"meterhub-server_collect_JSON"}, groupId = "meterhub-server")
    public void receiveJSON(ConsumerRecord<?, ?> record){
        log.debug("收到消息[JSON]: {}", record.value());
    }

}