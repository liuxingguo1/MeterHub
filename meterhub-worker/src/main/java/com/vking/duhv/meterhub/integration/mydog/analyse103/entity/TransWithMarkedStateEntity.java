package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-19 19:09
 * ASDU中的信息体(传送带标志的状态变位)
 */
@Data
public class TransWithMarkedStateEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 故障序号
     */
    private String Fault;

    /**
     * 带标志的状态变位的数目
     */
    private int statedShiftNumber;

    /**
     * 标志的位置
     */
    private String position;


    /**
     * 状态变位信息集合
     */
    private List<TransWithMarkedStateSubEntity> transWithMarkedStateSubEntity;
}
