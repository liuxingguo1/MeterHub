package com.vking.duhv.meterhub.client.runner;

import com.vking.duhv.meterhub.client.serverclient.MeterHubServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private MeterHubServerClient client;

    @Override
    public void run(ApplicationArguments args) {

    }

}
