package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lucan.liu
 * @date 2023-12-14 16:09
 * ASDU中的信息体
 */
@Data
public class InformationBodyEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 带时标时的 时间值
     */
    private Date time;

    /**
     * 双点信息
     * 1 分状态
     * 2 合状态
     */
    private int dpi;

    /**
     * 备用
     */
    private int res;

    /**
     * 扫描序号
     */
    private int sin;

    /**
     * 相对时间（毫秒）
     */
    private String relateTime;

    /**
     * 故障
     */
    private String fault;

    /**
     * 返回信息标识符
     */
    private int returnFlag;



}
