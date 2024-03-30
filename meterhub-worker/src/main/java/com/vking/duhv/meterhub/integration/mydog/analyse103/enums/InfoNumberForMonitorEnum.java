package com.vking.duhv.meterhub.integration.mydog.analyse103.enums;


import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferInfoNumberException;

/**
 * @author lucan.liu
 * @date 2023-12-14 9:33
 * 信息序号（监视方向）
 */
public enum InfoNumberForMonitorEnum {

    //在监视方向的系统功能
    SYSTEM_FUNCTION(0, "总查询(总召唤)结束"),
    STATE(1, "时间同步\n"),
    MONITOR(2, "复位帧计数位(FCB)\n"),
    GROUND_FAULT(3, "复位通信单元(CU)\n"),
    SHORT_CIRCUIT(4, "启动/重新启动\n"),
    AUTOMATIC_RECLOSING(5, "电源合上\n");

    private int code;
    private String describe;

    InfoNumberForMonitorEnum(int code,String describe){
        this.code = code;
        this.describe = describe;
    }

    public static String getDescribe(int code) throws UnknownTransferInfoNumberException {
        for (InfoNumberForMonitorEnum value : InfoNumberForMonitorEnum.values()){
            if (value.code == code) return value.describe;
        }
        throw new UnknownTransferInfoNumberException();
    }

}
