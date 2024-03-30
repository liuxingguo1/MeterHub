package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;


import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferInfoNumberException;

/**
 * @author lucan.liu
 * @date 2023-12-14 9:33
 * 信息序号（控制方向）
 */
public enum InfoNumberForcontrolEnum {
    SYSTEM_FUNCTION(15, "系统功能"),
    STATE(31, "状态"),
    MONITOR(47, "监视"),
    GROUND_FAULT(63, "接地故障"),
    SHORT_CIRCUIT(127, "短路"),
    AUTOMATIC_RECLOSING(143, "自动重合闸"),
    MEASURED_VALUE(159, "被测值"),
    UNUSED(239, "未用"),
    UNIVERSAL_CLASSIFICATION_FUNCTION(255, "通用分类功能");

    private int code;
    private String describe;

    InfoNumberForcontrolEnum(int code, String describe){
        this.code = code;
        this.describe = describe;
    }

    public static String getDescribe(int code) throws UnknownTransferInfoNumberException {
        for (InfoNumberForcontrolEnum value : InfoNumberForcontrolEnum.values()){
            if (value.code == code) return value.describe;
        }
        throw new UnknownTransferInfoNumberException();
    }

}
