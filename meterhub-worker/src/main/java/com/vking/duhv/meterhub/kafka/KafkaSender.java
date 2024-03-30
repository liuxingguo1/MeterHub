package com.vking.duhv.meterhub.kafka;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaSender {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, String data) {
        CompletableFuture<SendResult<String, Object>> completable = kafkaTemplate.send(topic, key, data);
        completable.whenCompleteAsync((result, ex) -> {
            if (null == ex) {
                log.debug("发送消息成功：" + result.toString());
            } else {
                log.debug("发送消息失败：" + ex.getMessage());
            }
        });
    }

}