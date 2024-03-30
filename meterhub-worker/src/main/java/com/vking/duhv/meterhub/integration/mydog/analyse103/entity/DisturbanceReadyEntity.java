package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lucan.liu
 * @date 2023-12-19 17:40
 * ASDU中的信息体(扰动数据传输准备就绪)
 */
@Data
public class DisturbanceReadyEntity extends ASDUBaseEntity {

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
     * 电网故障序号
     */
    private String powerGridFaultNumber;

    /**
     * 通道数目
     */
    private int channalNumber;

    /**
     * 一个通道信息元素数目
     */
    private String channalInfoNumber;

    /**
     * 信息元素间的间隔
     */
    private String inforEleSpacNumber;

    /**
     * 时间
     */
    private Date time;








}
