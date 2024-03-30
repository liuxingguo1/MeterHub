package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-20 13:40
 * ASDU中的信息体(传送扰动值)
 */
@Data
public class TransDisturbanValueEntity extends ASDUBaseEntity {

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
    private String fault;

    /**
     * 实际通道序号
     */
    private int channalNumber;

    /**
     * 每个应用数据服务单元有关扰动值的数目
     */
    private int number;

    /**
     * 应用服务单元的第一个信息元素的序号
     */
    private String No1Number;

    /**
     * 单个扰动值
     */
    private List<String> singleDisturbanceValueList;


}
