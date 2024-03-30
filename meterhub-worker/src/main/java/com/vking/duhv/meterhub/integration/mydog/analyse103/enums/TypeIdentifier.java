package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;


import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferReasonException;
import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTypeIdentifierException;

/**
 * 类型标识
 */
public enum TypeIdentifier {
    MESSAGE_TIME(1, "带时标的报文"),
    RELATIVE_TIME_MESSAGE_TIME(2, "具有相对时间的带时标的报文"),
    MEASURED_VALUE_ONE(3, "被测值I"),
    RELATIVE_TIME_MEASURED_VALUE(4, "具有相对时间的带时标的被测值"),
    IDENTIFYING(5, "标识"),
    TIME_SYNCHRONIZATION(6, "时间同步"),
    TOTAL_QUERY(7, "总查询(总召唤)"),
    TOTAL_QUERY_STOP(8, "总查询(总召唤)终止"),
    MEASURED_VALUE_TWO(9, "被测值Ⅱ"),
    GENERAL_CLASS_DATA(10, "通用分类数据"),
    GENERAL_CLASS_SIGN(11, "通用分类标识"),
    GENERAL_COMMANDS(20, "一般命令"),
    GENERAL_CLASS_COMMANDS(21, "通用分类命令"),
    RECORDED_DISTURBANCE_TABLE(23, "被记录的扰动表"),
    DISTURBANCE_DATA_TRANSMISSION_COMMANDS(24, "扰动数据传输的命令"),
    DISTURBANCE_DATA_TRANSMISSION_APPROVE(25, "扰动数据传输的认可"),
    DISTURBANCE_DATA_TRANSMISSION_READY(26, "扰动数据传输准备就绪"),
    RECORDED_CHANNEL_TRANSMISSION_READY(27, "被记录的通道传输准备就绪"),
    MARKED_TRANSMISSION_READY(28, "带标志的状态变位传输准备就绪"),
    FLAG_STATE_TRANSMISSION_STATE(29, "传送带标志的状态变位"),
    TRANSMITTING_DISTURBANCE_VALUES(30, "传送扰动值"),
    END_OF_TRANSMISSION(31, "传送结束");


    private int code;
    private String describe;

    TypeIdentifier(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDescribe(int code) throws UnknownTypeIdentifierException {
        for (TypeIdentifier value : TypeIdentifier.values()) {
            if (value.code == code) return value.describe;
        }

        throw new UnknownTypeIdentifierException();
    }

    public static String getName(int code)  throws UnknownTransferReasonException {
        for (TypeIdentifier value : TypeIdentifier.values()) {
            if (value.code == code) return value.name();
        }
        throw  new UnknownTransferReasonException();
    }
}
