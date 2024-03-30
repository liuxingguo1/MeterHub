package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @author lucan.liu
 * @date 2023-12-19 17:09
 * ASDU中的信息体(扰动数据传输的命令)
 */
@Data
public class DisturbanceCommandEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 命令类型
     */
    private int commandType;

    /**
     * 扰动值类型
     */
    private int disturbValueType;

    /**
     * 故障
     */
    private String Fault;

    /**
     * 实际通道序号
     */
    private int channalNumber;





}
