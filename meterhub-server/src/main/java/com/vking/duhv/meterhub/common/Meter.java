package com.vking.duhv.meterhub.common;

import lombok.Data;

@Data
public class Meter {

    private String from;
    private String subSystemCode;
    private String protocol;
    private String data;

}
