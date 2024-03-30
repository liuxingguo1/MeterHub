package com.vking.duhv.meterhub.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.common.Meter;
import com.vking.duhv.meterhub.core.DataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaReceiver {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DataProcessor processor;

    @KafkaListener(topics = {"meterhub-server_collect_IEC000"}, groupId = "meterhub-server")
    public void receiveIEC000(ConsumerRecord<?, ?> record, Acknowledgment ack){
        String json = record.value().toString();
        log.debug("收到消息[IEC000]: {}", json);
        try {
            Meter meter = mapper.readValue(json, Meter.class);
            processor.doProcess(meter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ack.acknowledge();
    }

    @KafkaListener(topics = {"meterhub-server_collect_IEC103"}, groupId = "meterhub-server")
    public void receiveIEC103(ConsumerRecord<?, ?> record, Acknowledgment ack){
        String json = record.value().toString();
        log.debug("收到消息[IEC103]: {}", json);
        try {
            Meter meter = mapper.readValue(json, Meter.class);
            processor.doProcess(meter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ack.acknowledge();
    }

    @KafkaListener(topics = {"meterhub-server_collect_IEC104"}, groupId = "meterhub-server")
    public void receiveIEC104(ConsumerRecord<?, ?> record, Acknowledgment ack){
        String json = record.value().toString();
        log.debug("收到消息[IEC104]: {}", json);
        try {
            Meter meter = mapper.readValue(json, Meter.class);
            processor.doProcess(meter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ack.acknowledge();
    }

    @KafkaListener(topics = {"meterhub-server_collect_JSON"}, groupId = "meterhub-server")
    public void receiveJSON(ConsumerRecord<?, ?> record, Acknowledgment ack){
        String json = record.value().toString();
        log.debug("收到消息[JSON]: {}", json);
        try {
            Meter meter = mapper.readValue(json, Meter.class);
            processor.doProcess(meter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ack.acknowledge();
    }

}