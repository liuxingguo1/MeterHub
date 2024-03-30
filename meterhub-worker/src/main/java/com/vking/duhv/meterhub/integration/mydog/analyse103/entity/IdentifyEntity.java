package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @author lucan.liu
 * @date 2023-12-14 16:09
 * ASDU中的信息体(被测值I)
 */
@Data
public class IdentifyEntity extends ASDUBaseEntity {

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
     * 兼容级别
     */
    private int compatibleLevel;

    /**
     * ASCII字符1
     */
    private int ASCII1;

    /**
     * ASCII字符2
     */
    private int ASCII2;

    /**
     * ASCII字符3
     */
    private int ASCII3;

    /**
     * ASCII字符4
     */
    private int ASCII4;

    /**
     * ASCII字符5
     */
    private int ASCII5;

    /**
     * ASCII字符6
     */
    private int ASCII6;

    /**
     * ASCII字符7
     */
    private int ASCII7;

    /**
     * ASCII字符8
     */
    private int ASCII8;

    /**
     * 自由赋值1
     */
    private int freeAssignment1;

    /**
     * 自由赋值2
     */
    private int freeAssignment2;

    /**
     * 自由赋值3
     */
    private int freeAssignment3;

    /**
     * 自由赋值4
     */
    private int freeAssignment4;


}
