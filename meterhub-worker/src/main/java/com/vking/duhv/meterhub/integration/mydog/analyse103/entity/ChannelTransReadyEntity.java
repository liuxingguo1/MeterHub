package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @author lucan.liu
 * @date 2023-12-19 17:40
 * ASDU中的信息体(被记录的通道传输准备就绪)
 */
@Data
public class ChannelTransReadyEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 扰动值类型
     */
    private int disturbValueType;

    /**
     * 故障序号
     */
    private String Fault;

    /**
     * 实际通道序号
     */
    private int channalNumber;

    /**
     * 一次额定值
     */
    private float primaryRatedValue;

    /**
     * 二次额定值
     */
    private float secondaryRatedValue;

    /**
     * 参比因子
     */
    private float referenceFactor;









}
