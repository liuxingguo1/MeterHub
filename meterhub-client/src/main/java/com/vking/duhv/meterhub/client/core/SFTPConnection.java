package com.vking.duhv.meterhub.client.core;

import cn.hutool.core.util.StrUtil;
import com.vking.duhv.meterhub.client.core.conf.FTPConfig;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SFTPConnection extends SubSystemConnection {
    private FTPConfig config;
    private MeterHubServerClient client;
    private Class<? extends FTPHandler> handlerClazz;
    private FTPHandler handler;
    private Timer timer = new Timer();


    public SFTPConnection(FTPConfig config, MeterHubServerClient client, Class<? extends FTPHandler> handlerClazz) {
        this.config = config;
        this.client = client;
        this.handlerClazz = handlerClazz;

    }

    @Override
    void start() {
        try {
            handler = handlerClazz.getDeclaredConstructor().newInstance();
            handler.init();
        } catch (Exception e) {
            log.error("处理器初始化失败:{}", handlerClazz.getName());
            setStatus(0);
            return;
        }
        timer.schedule(new TimerTask() {
            SSHClient sshClient;
            SFTPClient sftpClient;

            @Override
            public void run() {
                List<String> filePaths = null;
                try {
                    SSHClient sshClient = getFtpClient();
                    SFTPClient sftpClient = sshClient.newSFTPClient();

                    List<RemoteResourceInfo> files = sftpClient.ls(config.getRemotePath());
                    filePaths = new ArrayList<>(files.size());
                    for (RemoteResourceInfo file : files) {
                        String remotePath = config.getRemotePath() + File.separator + file.getName();
                        String localPath = config.getLocalPath() + File.separator + file.getName();
                        if (file.isRegularFile()) {
                            sftpClient.get(remotePath, localPath);
                            filePaths.add(file.getName());
                        }
                    }
                    client.send(StrUtil.format(Constant.METER_JSON_TEMP, config.getHost(), Constant.DATA_PROTOCOL_FILE_GZLB, filePaths));
                    //todo 外传文件

                } catch (Exception ex) {
                    log.error("{}[{}}], 文件传输错误, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
                } finally {
                    try {
                        sftpClient.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        sshClient.disconnect();
                    } catch (IOException ignored) {

                    }
                    if (null != filePaths) {
                        for (String path : filePaths) {
                            try {
                                FileUtils.delete(new File(path));
                            } catch (IOException ignored) {

                            }
                        }
                    }
                }


            }
        }, config.getCycleSeconds() * 1000);

    }

    @Override
    void test() {

    }

    @Override
    void destroy() {
        timer.cancel();
    }

    @Override
    void rest() throws InterruptedException {

    }

    @Override
    ConnectionConfig getCofig() {
        return null;
    }


    private SSHClient getFtpClient() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(config.getHost(), config.getPort());
        client.useCompression();
        client.authPassword(config.getUserName(), config.getPassword());
        return client;
    }


}
