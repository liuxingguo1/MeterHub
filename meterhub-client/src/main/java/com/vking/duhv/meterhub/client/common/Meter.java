package com.vking.duhv.meterhub.client.common;

import lombok.Data;

@Data
public class Meter {

    private String from;
    private String code;
    private String protocol;
    private Long time;
    private String data;

}
