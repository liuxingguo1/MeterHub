package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lucan.liu
 * @date 2023-12-18 13:09
 * ASDU中的信息体(被记录的扰动表)
 */
@Data
public class RecordedDisturbanceEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 故障
     */
    private String fault;

    /**
     * 故障状态
     */
    private String faultState;

    /**
     * 时间
     */
    private Date time;





}
