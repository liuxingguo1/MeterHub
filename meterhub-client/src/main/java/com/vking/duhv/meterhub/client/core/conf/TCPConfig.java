package com.vking.duhv.meterhub.client.core.conf;

import com.vking.duhv.meterhub.client.core.ConnectionConfig;
import lombok.Data;

@Data
public class TCPConfig extends ConnectionConfig {

    private String host;
    private Integer port;

}
