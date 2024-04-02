package com.vking.duhv.meterhub.client.core.conf;

import com.vking.duhv.meterhub.client.core.ConnectionConfig;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Data
public class FTPConfig extends ConnectionConfig {
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private Long cycleSeconds = 60L;
    private String remotePath = "/root/data/COMTRADE";//ftp服务器上文件目录
    private String localPath = System.getProperty("user.dir") + File.separator + "COMTRADE";//本地暂存目录
    private Boolean cleanRemote = true;//清理使用过的远程文件
    private String destinationPath = "/COMTRADE";//传输到sftp的路径

    public FTPConfig() {
        if (!new File(localPath).exists()) {
            try {
                FileUtils.forceMkdir(new File(localPath));
            } catch (IOException e) {
                throw new RuntimeException("本地文件路径不存在" + localPath, e);
            }
        }
    }

}
