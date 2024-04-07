package com.vking.duhv.meterhub.client.config;

import cn.hutool.extra.ftp.FtpConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration(proxyBeanMethods = false)
public class SftpConfig {

    @Value("${meterhub.server.sftp.host:127.0.0.1}")
    private String sftp_host;
    @Value("${meterhub.server.sftp.port:20}")
    private Integer sftp_port;
    @Value("${meterhub.server.sftp.user:root}")
    private String sftp_user;
    @Value("${meterhub.server.sftp.password:123456}")
    private String sftp_password;
    @Getter
    private static FtpConfig config;

    @PostConstruct
    public void init() {
        config = new FtpConfig(sftp_host, sftp_port, sftp_user, sftp_password, Charset.defaultCharset());
    }
}
