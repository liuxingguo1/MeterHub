package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;


import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferReasonException;

/**
 * 解析传送原因
 */
public enum TransferReasonEnum {
    SPONTANEOUS(1, "自发(突发)"),
    CIRCULATE(2, "循环 "),
    FCB(3, "复位帧计数位(FCB)"),
    CU(4, "复位通信单元(CU)"),
    FIRING(5, "启动/重新启动"),
    POWER_ON(6, "电源合上"),
    test_mode(7, "测试模式"),
    TIME_SYNCHRONIZATION(8, "时间同步"),
    TOTAL_QUERY(9, "总查询(总召唤)"),
    TOTAL_QUERY_STOP(10, "总查询(总召唤)终止"),
    LOCAL_OPERATIONS(11, "当地操作"),
    REMOTE_OPERATION(12, "远方操作"),
    POSITIVE_RECOGNITION_OF_COMMANDS(20, "命令的肯定认可"),
    NEGATION_RECOGNITION_OF_COMMANDS(21, "命令的否定认可"),
    TRANSMISSION_OF_DISTURBANCE_DATA(31, "扰动数据的传送"),
    POSITIVE_UNIVERSAL_CLASS_WRITING_COMMANDS(40, "通用分类写命令的肯定认可"),
    NEGATIVE_UNIVERSAL_CLASS_WRITING_COMMANDS(41, "通用分类写命令的否定认可"),
    EFFECTIVE_GENERAL_CLASS_READ_COMMANDS(42, "对通用分类读命令有效数据响应"),
    INVALID_GENERAL_CLASS_READ_COMMAND(43, "对通用分类读命令无效数据响应"),
    UNIVERSAL_CLASS_WRITE_CONFIRMATION(44, "通用分类写确认");
    private int code;
    private String describe;

    TransferReasonEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDdescribe(int code) throws UnknownTransferReasonException {
        for (TransferReasonEnum value : TransferReasonEnum.values()) {
            if (value.code == code) return value.describe;
        }
        throw  new UnknownTransferReasonException();
    }

    public static String getName(int code)  throws UnknownTransferReasonException {
        for (TransferReasonEnum value : TransferReasonEnum.values()) {
            if (value.code == code) return value.name();
        }
        throw  new UnknownTransferReasonException();
    }
}
