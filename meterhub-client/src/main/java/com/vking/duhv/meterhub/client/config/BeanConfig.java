package com.vking.duhv.meterhub.client.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration(proxyBeanMethods = false)
public class BeanConfig {

    @Value("${meterhub.server.socket.host:127.0.0.1}")
    private String host;

    @Value("${meterhub.server.socket.port:9999}")
    private Integer port;

    @Bean
    public MeterHubServerClient meterHubServerClient() {
        return new MeterHubServerClient(this.host, this.port);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return builder;
    }

}
