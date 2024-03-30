package com.vking.duhv.meterhub.client.acquisition.protocol.iec104.dto;

import com.vking.duhv.meterhub.client.acquisition.protocol.iec104.Iec104Format;
import lombok.Getter;

@Getter
public class APCI {
    private byte startChar;
    private byte apduLen;
    private byte ctl1;
    private byte ctl2;
    private byte ctl3;
    private byte ctl4;

    private String format;//U S I

    public APCI(byte[] bytes) {
        startChar = bytes[0];
        apduLen = bytes[1];
        ctl1 = bytes[2];
        ctl2 = bytes[3];
        ctl3 = bytes[4];
        ctl4 = bytes[5];

        int ctl_bit1 = ctl1 & 1;
        int ctl_bit2 = ctl1 >> 1 & 1;

        if (ctl_bit1 == 0) {
            format = Iec104Format.I;
        } else if (ctl_bit2 == 0) {
            format = Iec104Format.S;
        } else {
            format = Iec104Format.U;
        }
    }


}
