package com.vking.duhv.meterhub.client.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ConnectionConfig {

    private String name;
    private String code;
    private String commProtocol;
    private String dataProtocol;
    private Map<String, Object> data;

    public void cachePut(String key, Object value) {
        if (null == data) {
            data = new HashMap<>();
        }
        data.put(key, value);
        ConnectionManager.save();
    }

    public Object cacheGet(String key) {
        if (null == data) {
            data = new HashMap<>();
        }
        return data.get(key);
    }
}
