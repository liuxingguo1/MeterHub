package com.vking.duhv.meterhub.client.controller;

import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "TEST")
@RestController
public class TestController {

    @Autowired
    private MeterHubServerClient meterHubServerClient;

    @GetMapping("/sendOneData")
    public String sendOneData() {
        int content = (int) (Math.random() * 10000);
        String ip = "127.0.0.1";
        String msg = "{\"from\": \"" + ip + "\", \"protocol\": \"104\", \"data\": \"" + content + "\"}";
        meterHubServerClient.send(msg);
        return "ok";
    }

}
