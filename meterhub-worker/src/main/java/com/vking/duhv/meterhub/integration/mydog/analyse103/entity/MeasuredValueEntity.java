package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lucan.liu
 * @date 2023-12-18 13:09
 * ASDU中的信息体(具有相对时间的带时标的被测值)
 */
@Data
public class MeasuredValueEntity extends ASDUBaseEntity {

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
     *
     */
    private float shortCircuitPosition;

    /**
     * 相对时间（毫秒）
     */
    private String relateTime;

    /**
     * 故障
     */
    private String fault;


    /**
     * 时间
     */
    private Date time;

    /**
     * 备用
     */
    private String res;


}
