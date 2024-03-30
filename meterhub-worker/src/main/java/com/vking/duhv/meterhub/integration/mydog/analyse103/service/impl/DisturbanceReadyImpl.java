package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.DisturbanceReadyEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-19 17:44
 * 扰动数据传输准备就绪
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.DISTURBANCE_DATA_TRANSMISSION_READY)
@Slf4j
@Service
public class DisturbanceReadyImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<DisturbanceReadyEntity> disturbanceReadyEntityList = new ArrayList<>();
        DisturbanceReadyEntity disturbanceReadyEntity = new DisturbanceReadyEntity();
        //设置类型
        disturbanceReadyEntity.setType(asduEntity.getTypeIdentifierValue());
        //扰动值的类型
        disturbanceReadyEntity.setDisturbValueType(infoElement[1]);
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        disturbanceReadyEntity.setFault(new BigInteger(fault, 16).toString());
        //电网故障序号
        //转变成16进制字符
        String powerGridFaultNumber = ByteUtil.analysHex(infoElement[3],infoElement[4]);
        //16进制转为10进制
        disturbanceReadyEntity.setPowerGridFaultNumber(new BigInteger(powerGridFaultNumber, 16).toString());
        //通道数目
        disturbanceReadyEntity.setChannalNumber(infoElement[5]);
        //一个通道信息元素的数目
        //转变成16进制字符
        String channalInfoNumber = ByteUtil.analysHex(infoElement[6],infoElement[7]);
        //16进制转为10进制
        disturbanceReadyEntity.setChannalInfoNumber(new BigInteger(channalInfoNumber, 16).toString());
        //信息元素间的间隔
        //转变成16进制字符
        String inforEleSpacNumber = ByteUtil.analysHex(infoElement[8],infoElement[9]);
        //16进制转为10进制
        disturbanceReadyEntity.setInforEleSpacNumber(new BigInteger(inforEleSpacNumber, 16).toString());
        //4个字节时间组
        int time[] = new int[4];
        for (int i = 0; i < 4; i++) {
            time[i] = infoElement[10 + i];
        }
        disturbanceReadyEntity.setTime(ByteUtil.TimeScaleForFour(time));
        disturbanceReadyEntityList.add(disturbanceReadyEntity);
        asduEntity.setInformationBodyList(disturbanceReadyEntityList);
        return asduEntity;
    }

}
