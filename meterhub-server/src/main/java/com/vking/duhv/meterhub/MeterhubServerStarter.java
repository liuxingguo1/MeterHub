package com.vking.duhv.meterhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.vking.duhv")
@SpringBootApplication
public class MeterhubServerStarter {

    public static void main(String[] args) {
        SpringApplication.run(MeterhubServerStarter.class, args);
    }

}
