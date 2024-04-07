package com.vking.duhv.meterhub.client.api.dto;

import com.vking.duhv.meterhub.client.core.conf.*;
import lombok.Data;

import java.util.Map;

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
    private String userName;
    private String password;
    private Map<String, Object> data;

    public TCPConfig toTCPConfig() {
        TCPConfig config = new TCPConfig();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);
        config.setData(data);
        return config;
    }

    public FTPConfig toFTPConfig() {
        FTPConfig config = new FTPConfig();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);

        config.setUserName(userName);
        config.setPassword(password);
        config.setData(data);
        return config;
    }

    public FTPGZLBConfig toFTPGZLBConfig() {
        FTPGZLBConfig config = new FTPGZLBConfig();
        config.setName(name);
        config.setCode(code);
        config.setCommProtocol(commProtocol);
        config.setDataProtocol(dataProtocol);
        config.setHost(host);
        config.setPort(port);

        config.setUserName(userName);
        config.setPassword(password);
        config.setData(data);
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
        config.setData(data);
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
        config.setData(data);
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
        config.setData(data);
        return config;
    }

}
