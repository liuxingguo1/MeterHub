package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;

import com.vking.duhv.meterhub.integration.mydog.exception.UnknownMeterTypeException;
import lombok.Getter;

@Getter
public enum MeterTypeEnum {

    METER_103(1,"103协议"),
    METER_104(2,"104协议");

    private int code;
    private String describe;


    MeterTypeEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDdescribe(int code) throws UnknownMeterTypeException {
        for (MeterTypeEnum value : MeterTypeEnum.values()) {
            if (value.code == code) return value.describe;
        }
        throw  new UnknownMeterTypeException();
    }

}
