package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @title ASDU应用服务数据单元 实体
 * @author: 刘兴国
 * @Date: 2022/12/15
 */
@Data
public class ASDUBaseEntity<T> implements Serializable {

    /**
     * 类属性标识符 参考 类 TypeIdentifier
     */
    private int typeIdentifierValue;

    /**
     * SQ = 1 信息元素地址顺序
     * SQ=0  信息元素地址非顺序
     */
    private int sq;

    /**
     * 信息总数
     */
    private int infoTotal;

    /**
     * 传送原因 参考类TransferReason
     */
    private int reason;

    /**
     * 传送原因中的 测试状态
     * true:以测试
     * false:为测试
     */
    private boolean reasonT;

    /**
     * 传送原因中的 是否确认/认可
     * true:肯定确认
     * false:否定确认
     */
    private boolean reasonPN;

    /**
     * 应用服务数据单元公共地址
     */
    private int messageAddress;

    /**
     * 功能类型
     */
    private int functionType;

    /**
     * 消息序号
     */
    private int infoNumber;


    /**
     * 信息体
     */
    private List<T> informationBodyList;


}
