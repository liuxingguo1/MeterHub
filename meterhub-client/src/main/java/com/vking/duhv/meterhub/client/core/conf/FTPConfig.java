package com.vking.duhv.meterhub.client.core.conf;

import com.vking.duhv.meterhub.client.core.ConnectionConfig;
import lombok.Data;

@Data
public class FTPConfig extends ConnectionConfig {
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private Long cycleSeconds;

    private String remotePath;//ftp服务器上文件目录
    private String localPath;//本地暂存目录



}
