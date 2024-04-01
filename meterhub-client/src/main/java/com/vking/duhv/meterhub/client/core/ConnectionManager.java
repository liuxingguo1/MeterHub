package com.vking.duhv.meterhub.client.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vking.duhv.meterhub.client.api.dto.ConfigDTO;
import com.vking.duhv.meterhub.client.core.decoder.IEC104Decoder;
import com.vking.duhv.meterhub.client.core.handler.*;
import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ConnectionManager {

    private final String dbFile = "MeterHubClientConnections.json";

    @Autowired
    private ObjectMapper mapper;

    private Map<String, SubSystemConnection> connectionMap = new HashMap<>();

    @Autowired
    private MeterHubServerClient meterHubServerClient;

    private ExecutorService executorService = new ThreadPoolExecutor(5, 50, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());

    @PostConstruct
    public void init() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadData();
    }

    private void loadData() {

        try {
            String json = new String(Files.readAllBytes(Paths.get(dbFile)));
            List<ConfigDTO> configs = mapper.readValue(json, new TypeReference<>() {
            });
            for (ConfigDTO config : configs) {
                createConnection(config);
            }
        } catch (IOException e) {
            log.info("文件加载异常：" + e.getMessage());
        }

    }

    private Boolean save() {
        List<ConnectionConfig> list = new ArrayList<>();
        for (SubSystemConnection conn : connectionMap.values()) {
            list.add(conn.getCofig());
        }
        try {
            String json = mapper.writeValueAsString(list);
            FileWriter fileWriter = new FileWriter(dbFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private SubSystemConnection createConnection(ConfigDTO config) {
        SubSystemConnection conn = connectionMap.get(config.getCode());
        if (conn != null) {
            return conn;
        }

        if (Constant.COMM_PROTOCOL_KAFKA.equals(config.getCommProtocol()) && Constant.DATA_PROTOCOL_JSON.equals(config.getDataProtocol())) {
            conn = new KafkaConnection(config.toKafkaConfig(), meterHubServerClient, mapper, KafkaHandler.class);
        } else if (Constant.COMM_PROTOCOL_TCP.equals(config.getCommProtocol()) && Constant.DATA_PROTOCOL_IEC104.equals(config.getDataProtocol())) {
            conn = new TCPConnection(config.toTCPIEC104Config(), meterHubServerClient, IEC104Handler.class, IEC104Decoder.class);
        } else if (Constant.COMM_PROTOCOL_TCP.equals(config.getCommProtocol()) && Constant.DATA_PROTOCOL_IEC103.equals(config.getDataProtocol())) {
            conn = new TCPConnection(config.toTCPIEC103Config(), meterHubServerClient, IEC103Handler.class);
        } else if (Constant.COMM_PROTOCOL_TCP.equals(config.getCommProtocol()) && Constant.DATA_PROTOCOL_IEC000.equals(config.getDataProtocol())) {
            conn = new TCPConnection(config.toTCPConfig(), meterHubServerClient, CommonTCPHandler.class);
        } else if (Constant.COMM_PROTOCOL_FTP.equals(config.getCommProtocol()) && Constant.DATA_PROTOCOL_FILE_GZLB.equals(config.getDataProtocol())) {
            conn = new SFTPConnection(config.toFTPConfig(), meterHubServerClient, CommonFTPHandler.class);
        }

        if (conn != null) {
            SubSystemConnection finalConn = conn;
            executorService.submit(() -> finalConn.start());
            connectionMap.put(conn.getId(), conn);
        }
        return conn;
    }

    public SubSystemConnection addConnection(ConfigDTO config) {
        SubSystemConnection conn = createConnection(config);
        save();
        return conn;
    }

    public Collection<SubSystemConnection> getAllConnection() {
        return connectionMap.values();
    }

    public Boolean deleteConnection(String id) {
        SubSystemConnection conn = connectionMap.get(id);
        if (null == conn) {
            return true;
        }
        conn.destroy();
        connectionMap.remove(id);
        save();
        return true;
    }

    public SubSystemConnection modifyConnection(ConfigDTO config) {
        SubSystemConnection conn = connectionMap.get(config.getId());
        conn.destroy();
        connectionMap.remove(conn.getId());
        conn = createConnection(config);
        save();
        return conn;
    }


}


