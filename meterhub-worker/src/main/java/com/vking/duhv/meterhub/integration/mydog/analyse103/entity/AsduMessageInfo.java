package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @author lucan.liu
 * @date 2023-12-15 11:10
 */
@Data
public class AsduMessageInfo {

    /**
     * ASDU实体
     */
    private ASDUBaseEntity asduEntity;

    /**
     * 消息体
     */
    private int[] asduMessage;
}
