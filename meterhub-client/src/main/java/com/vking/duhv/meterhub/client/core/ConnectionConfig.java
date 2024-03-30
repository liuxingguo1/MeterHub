package com.vking.duhv.meterhub.client.core;

import lombok.Data;

@Data
public class ConnectionConfig {

    private String name;
    private String code;
    private String commProtocol;
    private String dataProtocol;

}
