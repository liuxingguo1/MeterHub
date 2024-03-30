package com.vking.duhv.meterhub.client.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.client.common.Meter;
import com.vking.duhv.meterhub.client.core.conf.KafkaConfig;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Data
@Slf4j
public class KafkaConnection extends SubSystemConnection {

    @JsonIgnore
    private Properties properties = new Properties();

    @JsonIgnore
    private ObjectMapper mapper;

    @JsonIgnore
    private KafkaConsumer<String, String> consumer;

    private KafkaConfig config;

    @JsonIgnore
    private Class<? extends MQHandler> handlerClazz;

    @JsonIgnore
    private MQHandler mqHandler;

    @JsonIgnore
    private MeterHubServerClient meterHubServerClient;

    @JsonIgnore
    private Reader reader;

    public KafkaConnection(KafkaConfig config, MeterHubServerClient client, ObjectMapper mapper, Class<? extends MQHandler> handlerClazz) {
        this.meterHubServerClient = client;
        this.config = config;
        this.mapper = mapper;
        this.handlerClazz = handlerClazz;
        this.setId(config.getCode());
        this.setConfig();
    }

    private void setConfig() {
        this.properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getHost() + ":" + config.getPort());
        this.properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //设置拉取信息后是否自动提交（kafka记录当前app是否已经获取到此信息），false 手动提交 ；true 自动提交
        this.properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        /**
         * earliest 第一条数据开始拉取（当前应该没有获取过此topic信息）
         * latest 获取最新的数据（当前没有获取过此topic信息）
         * group消费者分组的概念
         */
        this.properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        this.properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroup());
    }

    @Override
    void start() {
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singleton(config.getTopic()));
        try {
            mqHandler = handlerClazz.getDeclaredConstructor().newInstance();
            mqHandler.init(this);
        } catch (Exception e) {
            log.error("处理器初始化失败:{}", handlerClazz.getName());
            setStatus(0);
            return;
        }
        reader = new Reader();
        reader.start();
    }

    @Override
    void test() {

    }

    @Override
    void destroy() {
        this.consumer.wakeup();
    }

    @Override
    void rest() {
        destroy();
        start();
    }

    @Override
    ConnectionConfig getCofig() {
        return this.config;
    }

    @Override
    public void rest(ConnectionConfig config) {
        this.setId(config.getCode());
        this.config = (KafkaConfig) config;
        this.setConfig();
        this.rest();
    }

    class Reader extends Thread {

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    setStatus(1);
                    for (ConsumerRecord<String, String> record : records) {
                        long timestamp = record.timestamp();
                        Meter meter = new Meter();
                        meter.setFrom(config.getHost());
                        meter.setCode(config.getCode());
                        meter.setProtocol(config.getDataProtocol());
                        meter.setData(record.value());
                        String msg = mapper.writeValueAsString(meter);
                        mqHandler.receive(msg);
                    }
                    if (records.count() > 0) {
                        consumer.commitSync();
                    }
                } catch (JsonProcessingException e) {

                } catch (WakeupException e) {
                    consumer.close();
                    log.info("{}[{}}], 连接关闭, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
                    setStatus(0);
                    break;
                }
            }
        }

    }

}
