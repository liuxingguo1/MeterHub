package com.vking.duhv.meterhub.client.api.dto;

import com.vking.duhv.meterhub.client.core.conf.*;
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
    private String userName;
    private String password;
    private Long cycleSeconds;//FTP 文件采集周期
    private String remotePath;//ftp服务器上文件目录
    private String localPath;//本地暂存目录
    private Boolean cleanRemote;//清理使用过的远程文件
    private String destinationPath;//传输到sftp的路径

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
        config.setCycleSeconds(cycleSeconds);
        config.setRemotePath(remotePath);
        config.setLocalPath(localPath);
        config.setCleanRemote(cleanRemote);
        config.setDestinationPath(destinationPath);
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
