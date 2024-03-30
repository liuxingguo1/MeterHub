package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;


import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferFunctionTypeException;

/**
 * 解析功能类型(兼容部分)
 *
 * @author lucan.liu 2023.12.13
 */
public enum TransferFun {
    DISTANCE_PROTECTION(128, "距离保护"),
    OVERCURRENT_PROTECTION(160, "过流保护"),
    TRANSFORMER_DIFFERENTIAL_PROTECTION(176, "变压器差动保护"),
    LINE_DIFFERENTIAL_PROTECTION(192, "线路差动保护"),
    GENERAL_CLASSIFICATION_FUNCTION_TYPES(254, "通用分类功能类型"),
    GLOBAL_FUNCTION_TYPES(255, "全局功能类型)");
    private int code;
    private String describe;

    TransferFun(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDdescribe(int code) throws UnknownTransferFunctionTypeException {
        for (TransferFun value : TransferFun.values()) {
            if (value.code == code) return value.describe;
        }
        throw  new UnknownTransferFunctionTypeException();
    }
}
