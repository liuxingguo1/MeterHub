package com.vking.duhv.meterhub.client.core.handler;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.FtpMode;
import com.vking.duhv.meterhub.client.config.SftpConfig;
import com.vking.duhv.meterhub.client.core.Constant;
import com.vking.duhv.meterhub.client.core.FTPHandler;
import com.vking.duhv.meterhub.client.core.SFTPConnection;
import com.vking.duhv.meterhub.client.core.conf.FTPGZLBConfig;
import com.vking.duhv.meterhub.client.util.Ftps;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class SFtpGZLBHandller extends FTPHandler {
    private SFTPConnection con;
    private FTPGZLBConfig config;
    private Timer timer = new Timer("故障录波定时任务");
    TimerTask task = new TimerTask() {
        SSHClient sshClient;
        SFTPClient sftpClient;

        @Override
        public void run() {
            List<String> fileNames = null;
            try {
                SSHClient sshClient = getFtpClient();
                SFTPClient sftpClient = sshClient.newSFTPClient();

                List<RemoteResourceInfo> files = sftpClient.ls(config.getRemotePath());
                fileNames = new ArrayList<>(files.size());
                for (RemoteResourceInfo file : files) {

                    String localPath = config.getLocalPath() + File.separator + file.getName();
                    if (file.isRegularFile()) {
                        sftpClient.get(file.getPath(), localPath);
                        fileNames.add(file.getName());
                    }
                }
                log.info("故障录波文件下载完成,数量：{}", fileNames.size());
                if (fileNames.size() != 0) {
                    Set<String> successSet = sendToSftp(config.getDestinationPath(), fileNames);
                    log.info("故障录波文件传输完成,数量：{}", successSet.size());
                    if (config.getCleanRemote()) {
                        //传输完成删除远程服务器数据
                        for (RemoteResourceInfo file : files) {
                            if (successSet.contains(file.getName())) {
                                sftpClient.rm(file.getPath());
                            }
                        }
                    }
                    con.getClient().send(StrUtil.format(Constant.METER_JSON_TEMP,
                            config.getHost(),
                            config.getCode(),
                            Constant.DATA_PROTOCOL_FILE_GZLB,
                            System.currentTimeMillis(),
                            fileNames
                    ));
                    log.info("故障录波文件通知完成");
                }
            } catch (Exception ex) {
                log.error("{}[{}}], 故障录波文件传输错误, IP:{}, PORT:{}", config.getName(), config.getCode(), config.getHost(), config.getPort());
                ex.printStackTrace();
            } finally {
                if (null != sftpClient) {
                    try {
                        sftpClient.close();
                    } catch (IOException ignored) {
                    }
                }
                if (null != sshClient) {
                    try {
                        sshClient.disconnect();
                    } catch (IOException ignored) {
                    }
                }
                if (null != fileNames) {
                    for (String name : fileNames) {
                        try {
                            FileUtils.delete(new File(config.getLocalPath() + File.separator + name));
                        } catch (IOException ignored) {
                        }
                    }
                }
            }


        }
    };

    @Override
    public void init(SFTPConnection con) {
        this.con = con;
        this.config = (FTPGZLBConfig) con.getConfig();
    }


    @Override
    public void run() {
        //定时发送
        timer.schedule(task, 0, config.getCycleSeconds() * 1000);
    }

    @Override
    public void destroy() {
        task.cancel();
        timer.cancel();
    }

    private SSHClient getFtpClient() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(config.getHost(), config.getPort());
        client.useCompression();
        client.authPassword(config.getUserName(), config.getPassword());
        return client;
    }

    private Set<String> sendToSftp(String path, List<String> files) {
        Ftps ftps = new Ftps(SftpConfig.getConfig(), FtpMode.Passive);
        Set<String> success = new HashSet<>();
        StrBuilder sb = new StrBuilder();
        for (String file : files) {
            try {
                ftps.upload(path, new File(config.getLocalPath() + File.separator + file));
                success.add(file);
            } catch (Exception e) {
                sb.append(file).append(";");
            }
        }
        if (!sb.isEmpty()) {
            log.error("文件传输失败：{}", sb.toString());
        }
        try {
            ftps.close();
        } catch (IOException e) {
        }
        return success;
    }


}
