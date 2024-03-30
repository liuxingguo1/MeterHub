package com.vking.duhv.meterhub.client.core.conf;

import com.vking.duhv.meterhub.client.core.ConnectionConfig;
import lombok.Data;

@Data
public class KafkaConfig extends ConnectionConfig {

    private String host;
    private Integer port;
    private String topic;
    private String group;

}
