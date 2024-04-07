package com.vking.duhv.meterhub.client.core.conf;

import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Data
public class FTPGZLBConfig extends FTPConfig {
    private final Long cycleSeconds = 60L;
    private final String remotePath = "/root/data/COMTRADE";//ftp服务器上文件目录
    private final String localPath = System.getProperty("user.dir") + File.separator + "COMTRADE";//本地暂存目录
    private final Boolean cleanRemote = false;//清理使用过的远程文件
    private final String destinationPath = "/COMTRADE";//传输到sftp的路径

    public FTPGZLBConfig() {
        if (!new File(localPath).exists()) {
            try {
                FileUtils.forceMkdir(new File(localPath));
            } catch (IOException e) {
                throw new RuntimeException("本地文件路径不存在" + localPath, e);
            }
        }
    }

}
