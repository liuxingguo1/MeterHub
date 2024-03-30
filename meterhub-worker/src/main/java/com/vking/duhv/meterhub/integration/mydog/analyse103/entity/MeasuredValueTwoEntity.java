package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @author lucan.liu
 * @date 2023-12-18 16:09
 * ASDU中的信息体(被测值I)
 */
@Data
public class MeasuredValueTwoEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 功能类型
     */
    private int functionType;

    /**
     * 消息序号
     */
    private int infoNumber;

    /**
     * 是否溢出
     * 0 无溢出
     * 1 溢出
     */
    private boolean isOverflow;

    /**
     * 差错位
     * 0 被测值有效
     * 1 被测值无效
     */
    private boolean erroneousBit;


    /**
     * A相电流（被测值）
     */
    private String currentA;

    /**
     * B相电流（被测值）
     */
    private String currentB;

    /**
     * C相电流（被测值）
     */
    private String currentC;

    /**
     * A相电压（被测值）
     */
    private String voltageA;

    /**
     * B相电压（被测值）
     */
    private String voltageB;

    /**
     * C相电压（被测值）
     */
    private String voltageC;

    /**
     * 有功功率（被测值）
     */
    private String activePower;

    /**
     * 无功功率（被测值）
     */
    private String reactivePower;

    /**
     * 频率
     */
    private String frequency;


}
