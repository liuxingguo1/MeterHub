package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.MeterTypeEnum;
import lombok.Data;

@Data
public class AnalyInfoEntity<T> {

    /**
     * 协议类型
     */
    private int meterType;

    /**
     * 协议内容
     */
    private T meterInfo;

}
