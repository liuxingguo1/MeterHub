package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.TransDisturbanValueEntity;
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
 * @date 2023-12-20 13:05
 * 传送扰动值
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.TRANSMITTING_DISTURBANCE_VALUES)
@Slf4j
@Service
public class TransDisturbanValueImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<TransDisturbanValueEntity> transDisturbanValueEntityList = new ArrayList<>();
        TransDisturbanValueEntity transDisturbanValueEntity = new TransDisturbanValueEntity();
        //扰动值的类型
        transDisturbanValueEntity.setDisturbValueType(infoElement[1]);
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        transDisturbanValueEntity.setFault(new BigInteger(fault, 16).toString());
        //实际通道序号
        transDisturbanValueEntity.setChannalNumber(infoElement[4]);
        //每个应用服务数据单元有关联扰动值的数目
        transDisturbanValueEntity.setNumber(infoElement[5]);
        //应用服务数据单元的第一个信息元素的序号
        //转变成16进制字符
        String No1Number = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        transDisturbanValueEntity.setNo1Number(new BigInteger(No1Number, 16).toString());
        //获取剩余的字节
        int[] dataArray = new int[infoElement.length - 8];
        for (int i = 0; i < dataArray.length; i++){
            dataArray[i] = infoElement[i + 8];
        }

        List<String> singleDisturbanceValueList = new ArrayList<>();
        for (int i = 0; i < infoElement[5]; i++) {
            //单个扰动值
            //转变成16进制字符
            String singleDisturbanceValue = ByteUtil.analysHex(infoElement[i * 2], infoElement[i * 2 + 1]);
            //16进制转为10进制
            singleDisturbanceValueList.add(new BigInteger(singleDisturbanceValue, 16).toString());
        }
        transDisturbanValueEntity.setSingleDisturbanceValueList(singleDisturbanceValueList);
        transDisturbanValueEntityList.add(transDisturbanValueEntity);
        asduEntity.setInformationBodyList(transDisturbanValueEntityList);
        return asduEntity;
    }

}
