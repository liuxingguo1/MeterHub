package com.vking.duhv.meterhub.client.api.dto;

import com.vking.duhv.meterhub.client.core.conf.KafkaConfig;
import com.vking.duhv.meterhub.client.core.conf.TCPConfig;
import com.vking.duhv.meterhub.client.core.conf.TCPIEC103Config;
import com.vking.duhv.meterhub.client.core.conf.TCPIEC104Config;
import lombok.Data;

@Data
public class ConfigDTO {

    private String id;
    private String name;
    private String code;
    private String commProtocol;
    private String dataProtocol;
    private String host;
    private Integer port;
    private String topic;
    private String group;

    public TCPConfig toTCPConfig() {
        TCPConfig config = new TCPConfig();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);
        return config;
    }

    public TCPIEC104Config toTCPIEC104Config() {
        TCPIEC104Config config = new TCPIEC104Config();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);
        return config;
    }

    public TCPIEC103Config toTCPIEC103Config() {
        TCPIEC103Config config = new TCPIEC103Config();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);
        return config;
    }

    public KafkaConfig toKafkaConfig() {
        KafkaConfig config = new KafkaConfig();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);
        config.setTopic(topic);
        config.setGroup(group);
        return config;
    }

}
