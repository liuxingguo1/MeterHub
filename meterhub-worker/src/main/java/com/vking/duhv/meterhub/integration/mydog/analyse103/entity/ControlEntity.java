package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

/**
 * @title 控制规约实体
 */
@Data
public class ControlEntity {


    /**
     * 物理传输方向
     * 0 主站->子站
     * 1 子站->主站
     */
    private boolean dir;

    /**
     * 源发站地址
     * 0 消息来自子站
     * 1 消息来自主站
     */
    private boolean prm;

    /**
     * 帧计数位，取值0或1，用于避免帧丢失或重复接收，0和1交替出现，通信开始或失败后调用Reset0来同步源发站和响应站的FCB位，是双方FCB=1
     */
    private boolean fcb;

    /**
     * 帧计数有效标志
     * 0 帧计数无效
     * 1 帧计数有效
     */
    private boolean fcv;

    /**
     * 是否需要请求一级数据的标志
     * 0 否
     * 1 是
     */
    private boolean acd;

    /**
     * 数据流控制为，防止子站缓冲区溢出，在发送用户数据到子站将导致其缓冲区溢出时，子站在响应报文中应将DFC位置1。
     * 主站判断DFC=1时，应发REQUEST-RESPOND 报文请求链路状态，知道DFC=0时继续发送用户数据
     */
    private boolean dfc;

    /**
     * 功能编码
     */
    private int functionCode;



}
