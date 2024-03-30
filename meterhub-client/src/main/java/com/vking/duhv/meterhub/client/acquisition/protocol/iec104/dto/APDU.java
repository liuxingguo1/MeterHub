package com.vking.duhv.meterhub.client.acquisition.protocol.iec104.dto;

import lombok.Getter;

@Getter
public class APDU {
    private APCI apci;
    private ASDU asdu;

    public APDU(byte[] bytes) {
        if (bytes.length < 6) {
            throw new RuntimeException("APDU长度错误");
        }
        apci = new APCI(bytes);
    }
}
