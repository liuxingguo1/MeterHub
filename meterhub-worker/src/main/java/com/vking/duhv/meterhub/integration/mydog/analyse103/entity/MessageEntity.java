package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用信息实体
 */
@Data
public class MessageEntity implements Serializable {

    //开始符 十六进制的
    private String start;

    //数据单元长度（除去 开始符和数据单元长度字符 后面所有的字符长度）
    private int messageLength;

    //地址域
    private int address;

    // 控制域 实体
    private ControlEntity controlEntity;

    //ASDU应用服务数据单元
    private ASDUBaseEntity asduEntity;
}
