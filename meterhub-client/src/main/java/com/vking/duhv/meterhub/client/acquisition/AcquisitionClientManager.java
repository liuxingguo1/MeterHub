package com.vking.duhv.meterhub.client.acquisition;

import com.vking.duhv.meterhub.client.acquisition.protocol.iec104.AcquisitionConf104;
import com.vking.duhv.meterhub.client.acquisition.protocol.iec104.Iec104Client;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.vking.duhv.meterhub.client.acquisition.Protocol.IEC_104;

@Slf4j
//@Component
public class AcquisitionClientManager {
    private List<AcquisitionMeta> acquisitionMetaList = Arrays.asList(new AcquisitionConf104("104测试站", IEC_104, "127.0.0.1", 2404));

    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private List<AcquisitionClient> clientList = new ArrayList<>();

    public void addClient(AcquisitionClient client) {
        clientList.add(client);
        executorService.submit(() -> client.connect());

    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        try {
            for (AcquisitionMeta info : acquisitionMetaList) {
                switch (info.getProtocol()) {
                    case IEC_104 -> {
                        addClient(new Iec104Client(info));
                    }
                    default -> log.error("协议不支持：{}", info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            while (true) {
                System.out.println("check");
                for (AcquisitionClient client : clientList) {
                    System.out.println(client.healthStats());
                }
                try {
                    Thread.sleep(30 * 1000L);
                } catch (InterruptedException ignore) {
                }
            }
        }).start();

    }
}
