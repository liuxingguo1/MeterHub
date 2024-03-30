package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.common.Meter;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AnalyInfoEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ControlEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.MessageEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.MeterTypeEnum;
import com.vking.duhv.meterhub.integration.mydog.analyse103.handler.ASDUHandler;
import com.vking.duhv.meterhub.integration.mydog.exception.IllegalFormatException;
import com.vking.duhv.meterhub.integration.mydog.exception.LengthException;
import com.vking.duhv.meterhub.integration.mydog.service.AnalysInfo;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service(value = "103")
@Slf4j
public class AnalysInfo103Impl implements AnalysInfo {

    @Autowired
    private ASDUHandler asduHandler;

    /**
     * 解析103数据为 实体
     * @param meter
     * @return
     */
    @Override
    public AnalyInfoEntity analysInfo(Meter meter) {
        AnalyInfoEntity analyInfoEntity = new AnalyInfoEntity();
        try {
            analyInfoEntity.setMeterType(MeterTypeEnum.METER_103.getCode());
            MessageEntity messageEntity = new MessageEntity();
            StringBuilder contentbuilder = new StringBuilder();
            log.info("当前传入的报文是:{}",meter.getData());
            String mes = meter.getData().replaceAll(" ", "");
            if ((mes.length() == 0)) {
                throw new RuntimeException("无报文信息！");
            }
            // 将报文转化成int数组
            int msgArray[] = ByteUtil.hexStringToIntArray(mes);
            int length = msgArray.length;
            if (msgArray[0] == 0x68 && msgArray[length - 1] == 0x16
                    && msgArray[0] == msgArray[3] && msgArray[1] == msgArray[2]) {
                contentbuilder.append("*APCI应用规约控制信息*").append("\n");
                contentbuilder.append("启动字符: 0x" + msgArray[0]).append("\n");
                messageEntity.setStart("68");
            } else {
                throw new IllegalFormatException();
            }
            // 验证数据单元长度
            if (length - 6 != msgArray[1] || msgArray[1] != msgArray[2]) {
                throw new LengthException(msgArray[1] + 4, length);
            }
            messageEntity.setMessageLength(msgArray[1]);
            //解析控制域数据
            ControlEntity controlEntity;
            contentbuilder.append("应用规约数据单元(APDU)长度:").append(msgArray[1]).append("\n");
            contentbuilder.append("控制域:").append("\n");
            controlEntity = Control(new int[]{msgArray[4]}, contentbuilder);
            messageEntity.setControlEntity(controlEntity);
            //解析地址域
            messageEntity.setAddress(msgArray[5]);
            //解析ASDU 只有长帧格式才有 ASDU 数据传输
            if (msgArray[0] == 0x68) {
                //解析ASDU
                contentbuilder.append("*ASDU应用服务数据单元*\n");
                //获取到剩余的字节，除出起始符（2字节）、长度值（2字节）、1个字节的控制域、1个字节的地址域、1个字节的结尾符、1个字节的cs
                int asdu[] = new int[length - 8];
                for (int j = 0; j < length - 8; j++) {
                    asdu[j] = msgArray[6 + j];
                }
                ASDUBaseEntity asduEntity = asduHandler.ASDU_analysis(asdu, contentbuilder);
                messageEntity.setAsduEntity(asduEntity);
            }
            analyInfoEntity.setMeterInfo(messageEntity);
            log.info("最终被解析的内容是:{}",analyInfoEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return analyInfoEntity;
    }

    /**
     * 解析103规约的控制域
     * @param con
     * @return
     */
    private static ControlEntity Control ( int[] con, StringBuilder conBuilder){
        ControlEntity controlEntity = new ControlEntity();
        if ((con[0] & 0x80) == 128) {
            controlEntity.setDir(true);
            conBuilder.append("\t物理传输方向由子站->主站\n");
        } else if ((con[0] & 0x80) == 0) {
            controlEntity.setDir(false);
            conBuilder.append("\t物理传输方向由主站->子站\n");
        }
        if ((con[0] & 0x40) == 64) {
            controlEntity.setPrm(true);
            conBuilder.append("\t消息来自于主站\n");
        } else if ((con[0] & 0x40) == 0) {
            controlEntity.setPrm(false);
            conBuilder.append("\t消息来自于子站\n");
        }
        if ((con[0] & 0x40) == 64 && (con[0] & 0x20) == 32) {
            controlEntity.setFcb(true);
            conBuilder.append("\t帧计数位为1\n");
        } else if ((con[0] & 0x40) == 64 && (con[0] & 0x20) == 0) {
            controlEntity.setFcb(false);
            conBuilder.append("\t帧计数位为0\n");
        }
        if ((con[0] & 0x40) == 64 && (con[0] & 0x0a) == 16) {
            controlEntity.setFcv(true);
            conBuilder.append("\t帧计数位有效\n");
        } else if ((con[0] & 0x40) == 64 && (con[0] & 0x0a) == 0) {
            controlEntity.setFcv(false);
            conBuilder.append("\t帧计数位无效\n");
        }

        if ((con[0] & 0x40) == 0 && (con[0] & 0x20) == 32) {
            controlEntity.setAcd(true);
            conBuilder.append("\t需要获取一级数据\n");
        } else if ((con[0] & 0x40) == 0 && (con[0] & 0x20) == 0) {
            controlEntity.setAcd(false);
            conBuilder.append("\t不需要获取一级数据\n");
        }

        if ((con[0] & 0x40) == 0 && (con[0] & 0x10) == 16) {
            controlEntity.setDfc(true);
            conBuilder.append("\t继电保护设备可以接收数据\n");
        } else if ((con[0] & 0x40) == 0 && (con[0] & 0x10) == 0) {
            controlEntity.setDfc(false);
            conBuilder.append("\t继电保护设备的缓冲区已满，无法接收新数据\n");
        }

        controlEntity.setFunctionCode(con[0] & 0x0f);
        return controlEntity;
    }
}
