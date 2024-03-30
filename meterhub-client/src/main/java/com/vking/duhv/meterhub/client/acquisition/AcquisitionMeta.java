package com.vking.duhv.meterhub.client.acquisition;


import lombok.Data;

@Data
public abstract class AcquisitionMeta {
    private String name;
    private String protocol;
    private String ip;
    private Integer port;

    public AcquisitionMeta() {
    }

    public AcquisitionMeta(String name, String protocol, String ip, Integer port) {
        this.name = name;
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "AcquisitionInfo{" +
                "name='" + name + '\'' +
                ", protocol='" + protocol + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
