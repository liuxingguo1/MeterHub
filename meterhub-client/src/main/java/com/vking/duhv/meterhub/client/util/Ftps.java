package com.vking.duhv.meterhub.client.util;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.AbstractFtp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
import cn.hutool.extra.ftp.FtpMode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class Ftps extends AbstractFtp {

    private FTPSClient ftpsClient;

    private FtpMode mode;
    /**
     * 执行完操作是否返回当前目录
     * -- GETTER --
     *  是否执行完操作返回当前目录
     *
     *
     */
    @Getter
    private boolean backToPwd;

    /**
     * 设置执行完操作是否返回当前目录
     *
     * @param backToPwd 执行完操作是否返回当前目录
     * @return this
     * @since 4.6.0
     */
    public Ftps setBackToPwd(boolean backToPwd) {
        this.backToPwd = backToPwd;
        return this;
    }

    /**
     * 构造
     *
     * @param host     域名或IP
     * @param port     端口
     * @param user     用户名
     * @param password 密码
     * @param charset  编码
     */
    public Ftps(String host, int port, String user, String password, Charset charset) {
        this(host, port, user, password, charset, null, null);
    }

    /**
     * 构造
     *
     * @param host               域名或IP
     * @param port               端口
     * @param user               用户名
     * @param password           密码
     * @param charset            编码
     * @param serverLanguageCode 服务器语言 例如：zh
     * @param systemKey          服务器标识 例如：org.apache.commons.net.ftp.FTPClientConfig.SYST_NT
     */
    public Ftps(String host, int port, String user, String password, Charset charset, String serverLanguageCode, String systemKey) {
        this(host, port, user, password, charset, serverLanguageCode, systemKey, null);
    }

    /**
     * 构造
     *
     * @param host               域名或IP
     * @param port               端口
     * @param user               用户名
     * @param password           密码
     * @param charset            编码
     * @param serverLanguageCode 服务器语言
     * @param systemKey          系统关键字
     * @param mode               模式
     */
    public Ftps(String host, int port, String user, String password, Charset charset, String serverLanguageCode, String systemKey, FtpMode mode) {
        this(new FtpConfig(host, port, user, password, charset, serverLanguageCode, systemKey), mode);
    }

    /**
     * 构造
     *
     * @param config FTP配置
     * @param mode   模式
     */
    public Ftps(FtpConfig config, FtpMode mode) {
        super(config);
        this.mode = mode;
        this.init();
    }

    /**
     * 初始化连接
     *
     * @return this
     */
    public Ftps init() {
        return this.init(this.ftpConfig, this.mode);
    }

    /**
     * 初始化连接
     *
     * @param host     域名或IP
     * @param port     端口
     * @param user     用户名
     * @param password 密码
     * @return this
     */
    public Ftps init(String host, int port, String user, String password) {
        return this.init(host, port, user, password, null);
    }

    /**
     * 初始化连接
     *
     * @param host     域名或IP
     * @param port     端口
     * @param user     用户名
     * @param password 密码
     * @param mode     模式
     * @return this
     */
    public Ftps init(String host, int port, String user, String password, FtpMode mode) {
        return init(new FtpConfig(host, port, user, password, this.ftpConfig.getCharset(), null, null), mode);
    }

    /**
     * 初始化连接
     *
     * @param config FTP配置
     * @param mode   模式
     * @return this
     */
    @SneakyThrows(Exception.class)
    public Ftps init(FtpConfig config, FtpMode mode) {
        final FTPSClient client = new FTPSClient(true);
        // issue#I3O81Y@Gitee
        client.setRemoteVerificationEnabled(false);

        final Charset charset = config.getCharset();
        if (null != charset) {
            client.setControlEncoding(charset.toString());
        }
        client.setConnectTimeout((int) config.getConnectionTimeout());
        final String systemKey = config.getSystemKey();
        if (StrUtil.isNotBlank(systemKey)) {
            final FTPClientConfig conf = new FTPClientConfig(systemKey);
            final String serverLanguageCode = config.getServerLanguageCode();
            if (StrUtil.isNotBlank(serverLanguageCode)) {
                conf.setServerLanguageCode(config.getServerLanguageCode());
            }
            client.configure(conf);
        }

        // connect
        try {
            // 连接ftp服务器
            client.connect(config.getHost(), config.getPort());
            client.setSoTimeout((int) config.getSoTimeout());
            // 登录ftp服务器
            client.login(config.getUser(), config.getPassword());

            client.execPROT("P");
            client.execPBSZ(0);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        final int replyCode = client.getReplyCode(); // 是否成功登录服务器
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            try {
                client.disconnect();
            } catch (IOException e) {
                // ignore
            }
            throw new FtpException("Login failed for user [{}], reply code is: [{}]", config.getUser(), replyCode);
        }
        this.ftpsClient = client;
        if (mode != null) {
            setMode(mode);
        }
        return this;
    }

    /**
     * 设置FTP连接模式，可选主动和被动模式
     *
     * @param mode 模式枚举
     * @return this
     * @since 4.1.19
     */
    public Ftps setMode(FtpMode mode) {
        this.mode = mode;
        switch (mode) {
            case Active:
                this.ftpsClient.enterLocalActiveMode();
                break;
            case Passive:
                this.ftpsClient.enterLocalPassiveMode();
                break;
        }
        return this;
    }

    @Override
    public Ftps reconnectIfTimeout() {
        String pwd = null;
        try {
            pwd = pwd();
        } catch (IORuntimeException fex) {
            // ignore
        }

        if (pwd == null) {
            return this.init();
        }
        return this;
    }

    @Override
    synchronized public boolean cd(String directory) {
        if (StrUtil.isBlank(directory)) {
            // 当前目录
            return true;
        }

        try {
            return ftpsClient.changeWorkingDirectory(directory);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public String pwd() {
        try {
            return ftpsClient.printWorkingDirectory();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public boolean mkdir(String dir) {
        try {
            return this.ftpsClient.makeDirectory(dir);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public List<String> ls(String path) {
        return ArrayUtil.map(lsFiles(path), FTPFile::getName);
    }

    /**
     * 遍历某个目录下所有文件和目录，不会递归遍历<br>
     * 此方法自动过滤"."和".."两种目录
     *
     * @param path   目录
     * @param filter 过滤器，null表示不过滤，默认去掉"."和".."两种目录
     * @return 文件或目录列表
     * @since 5.3.5
     */
    public List<FTPFile> lsFiles(String path, Filter<FTPFile> filter) {
        final FTPFile[] ftpFiles = lsFiles(path);
        if (ArrayUtil.isEmpty(ftpFiles)) {
            return ListUtil.empty();
        }

        final List<FTPFile> result = new ArrayList<>(ftpFiles.length - 2 <= 0 ? ftpFiles.length : ftpFiles.length - 2);
        String fileName;
        for (FTPFile ftpFile : ftpFiles) {
            fileName = ftpFile.getName();
            if (!StrUtil.equals(".", fileName) && !StrUtil.equals("..", fileName)) {
                if (null == filter || filter.accept(ftpFile)) {
                    result.add(ftpFile);
                }
            }
        }
        return result;
    }

    /**
     * 遍历某个目录下所有文件和目录，不会递归遍历
     *
     * @param path 目录，如果目录不存在，抛出异常
     * @return 文件或目录列表
     * @throws FtpException       路径不存在
     * @throws IORuntimeException IO异常
     */
    public FTPFile[] lsFiles(String path) throws FtpException, IORuntimeException {
        String pwd = null;
        if (StrUtil.isNotBlank(path)) {
            pwd = pwd();
            if (!cd(path)) {
                throw new FtpException("Change dir to [{}] error, maybe path not exist!", path);
            }
        }

        FTPFile[] ftpFiles;
        try {
            ftpFiles = this.ftpsClient.listFiles();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            // 回到原目录
            cd(pwd);
        }

        return ftpFiles;
    }

    @Override
    public boolean delFile(String path) {
        final String pwd = pwd();
        final String fileName = FileUtil.getName(path);
        final String dir = StrUtil.removeSuffix(path, fileName);
        if (!cd(dir)) {
            throw new FtpException("Change dir to [{}] error, maybe dir not exist!", path);
        }

        boolean isSuccess;
        try {
            isSuccess = ftpsClient.deleteFile(fileName);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            // 回到原目录
            cd(pwd);
        }
        return isSuccess;
    }

    @Override
    public boolean delDir(String dirPath) {
        FTPFile[] dirs;
        try {
            dirs = ftpsClient.listFiles(dirPath);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        String name;
        String childPath;
        for (FTPFile ftpFile : dirs) {
            name = ftpFile.getName();
            childPath = StrUtil.format("{}/{}", dirPath, name);
            if (ftpFile.isDirectory()) {
                // 上级和本级目录除外
                if (!".".equals(name) && !"..".equals(name)) {
                    delDir(childPath);
                }
            } else {
                delFile(childPath);
            }
        }

        // 删除空目录
        try {
            return this.ftpsClient.removeDirectory(dirPath);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public boolean upload(String destPath, File file) {
        Assert.notNull(file, "file to upload is null !");
        return upload(destPath, file.getName(), file);
    }

    /**
     * 上传文件到指定目录，可选：
     *
     * <pre>
     * 1. destPath为null或""上传到当前路径
     * 2. destPath为相对路径则相对于当前路径的子路径
     * 3. destPath为绝对路径则上传到此路径
     * </pre>
     *
     * @param file     文件
     * @param destPath     服务端路径，可以为{@code null} 或者相对路径或绝对路径
     * @param fileName 自定义在服务端保存的文件名
     * @return 是否上传成功
     * @throws IORuntimeException IO异常
     */
    public boolean upload(String destPath, String fileName, File file) throws IORuntimeException {
        try (final InputStream in = FileUtil.getInputStream(file)) {
            return upload(destPath, fileName, in);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 上传文件到指定目录，可选：
     *
     * <pre>
     * 1. destPath为null或""上传到当前路径
     * 2. destPath为相对路径则相对于当前路径的子路径
     * 3. destPath为绝对路径则上传到此路径
     * </pre>
     *
     * @param destPath       服务端路径，可以为{@code null} 或者相对路径或绝对路径
     * @param fileName   文件名
     * @param fileStream 文件流
     * @return 是否上传成功
     * @throws IORuntimeException IO异常
     */
    public boolean upload(String destPath, String fileName, InputStream fileStream) throws IORuntimeException {
        try {
            ftpsClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }

        String pwd = null;
        if (this.backToPwd) {
            pwd = pwd();
        }

        if (StrUtil.isNotBlank(destPath)) {
            mkDirs(destPath);
            if (!cd(destPath)) {
                throw new FtpException("Change dir to [{}] error, maybe dir not exist!", destPath);
            }
        }

        try {
            return ftpsClient.storeFile(fileName, fileStream);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (this.backToPwd) {
                cd(pwd);
            }
        }
    }

    /**
     * 递归上传文件（支持目录）<br>
     * 上传时，如果uploadFile为目录，只复制目录下所有目录和文件到目标路径下，并不会复制目录本身<br>
     * 上传时，自动创建父级目录
     *
     * @param remotePath 目录路径
     * @param uploadFile 上传文件或目录
     */
    public void uploadFileOrDirectory(final String remotePath, final File uploadFile) {
        if (!FileUtil.isDirectory(uploadFile)) {
            // 上传文件
            this.upload(remotePath, uploadFile);
            return;
        }

        final File[] files = uploadFile.listFiles();
        if (ArrayUtil.isEmpty(files)) {
            return;
        }

        final List<File> dirs = new ArrayList<>(files.length);
        //第一次只处理文件，防止目录在前面导致先处理子目录，而引发文件所在目录不正确
        for (final File f : files) {
            if (f.isDirectory()) {
                dirs.add(f);
            } else {
                this.upload(remotePath, f);
            }
        }
        //第二次只处理目录
        for (final File f : dirs) {
            final String dir = FileUtil.normalize(remotePath + "/" + f.getName());
            this.uploadFileOrDirectory(dir, f);
        }
    }

    @Override
    public void download(String path, File outFile) {
        final String fileName = FileUtil.getName(path);
        final String dir = StrUtil.removeSuffix(path, fileName);
        download(dir, fileName, outFile);
    }

    /**
     * 下载文件
     *
     * @param path     文件所在路径（远程目录），不包含文件名
     * @param fileName 文件名
     * @param outFile  输出文件或目录，当为目录时使用服务端文件名
     * @throws IORuntimeException IO异常
     */
    public void download(String path, String fileName, File outFile) throws IORuntimeException {
        if (outFile.isDirectory()) {
            outFile = new File(outFile, fileName);
        }
        if (!outFile.exists()) {
            FileUtil.touch(outFile);
        }
        try (OutputStream out = FileUtil.getOutputStream(outFile)) {
            download(path, fileName, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 下载文件到输出流
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param out      输出位置
     */
    public void download(String path, String fileName, OutputStream out) {
        download(path, fileName, out, null);
    }

    /**
     * 下载文件到输出流
     *
     * @param path            服务端的文件路径
     * @param fileName        服务端的文件名
     * @param out             输出流，下载的文件写出到这个流中
     * @param fileNameCharset 文件名编码，通过此编码转换文件名编码为ISO8859-1
     * @throws IORuntimeException IO异常
     * @since 5.5.7
     */
    public void download(String path, String fileName, OutputStream out, Charset fileNameCharset) throws IORuntimeException {
        String pwd = null;
        if (this.backToPwd) {
            pwd = pwd();
        }

        if (!cd(path)) {
            throw new FtpException("Change dir to [{}] error, maybe dir not exist!", path);
        }

        if (null != fileNameCharset) {
            fileName = new String(fileName.getBytes(fileNameCharset), StandardCharsets.ISO_8859_1);
        }
        try {
            ftpsClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpsClient.retrieveFile(fileName, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (backToPwd) {
                cd(pwd);
            }
        }
    }

    @Override
    public void recursiveDownloadFolder(String sourcePath, File destDir) {
        String fileName;
        String srcFile;
        File destFile;
        for (FTPFile ftpFile : lsFiles(sourcePath, null)) {
            fileName = ftpFile.getName();
            srcFile = StrUtil.format("{}/{}", sourcePath, fileName);
            destFile = FileUtil.file(destDir, fileName);

            if (!ftpFile.isDirectory()) {
                // 本地不存在文件或者ftp上文件有修改则下载
                if (!FileUtil.exist(destFile)
                        || (ftpFile.getTimestamp().getTimeInMillis() > destFile.lastModified())) {
                    download(srcFile, destFile);
                }
            } else {
                // 服务端依旧是目录，继续递归
                FileUtil.mkdir(destFile);
                recursiveDownloadFolder(srcFile, destFile);
            }
        }
    }

    /**
     * 获取FTPSClient客户端对象
     *
     * @return {@link FTPSClient}
     */
    public FTPSClient getClient() {
        return this.ftpsClient;
    }

    @Override
    public void close() throws IOException {
        if (null != this.ftpsClient) {
            this.ftpsClient.logout();
            if (this.ftpsClient.isConnected()) {
                this.ftpsClient.disconnect();
            }
            this.ftpsClient = null;
        }
    }
}

