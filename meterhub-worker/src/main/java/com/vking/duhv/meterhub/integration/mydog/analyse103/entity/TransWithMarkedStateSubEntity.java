package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucan.liu
 * @date 2023-12-19 19:09
 * ASDU中的信息体(传送带标志的状态变位)
 */
@Data
public class TransWithMarkedStateSubEntity implements Serializable {


    /**
     * 功能类型
     */
    private int functionType;

    /**
     * 消息序号
     */
    private int infoNumber;

    /**
     * 双点信息
     * 1 分状态
     * 2 合状态
     */
    private int dpi;

}
