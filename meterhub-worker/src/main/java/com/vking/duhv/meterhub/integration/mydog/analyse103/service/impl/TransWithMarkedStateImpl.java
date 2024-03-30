package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.TransWithMarkedStateEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.TransWithMarkedStateSubEntity;
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
 * @date 2023-12-19 19:05
 * 传送带标志的状态变位
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.FLAG_STATE_TRANSMISSION_STATE)
@Slf4j
@Service
public class TransWithMarkedStateImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<TransWithMarkedStateEntity> transWithMarkedStateEntityList = new ArrayList<>();
        TransWithMarkedStateEntity transWithMarkedStateEntity = new TransWithMarkedStateEntity();
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[0],infoElement[1]);
        //16进制转为10进制
        transWithMarkedStateEntity.setFault(new BigInteger(fault, 16).toString());
        //带标志的状态变位的数目
        transWithMarkedStateEntity.setStatedShiftNumber(infoElement[2]);
        //标志的位置
        //转变成16进制字符
        String position = ByteUtil.analysHex(infoElement[3],infoElement[4]);
        //16进制转为10进制
        transWithMarkedStateEntity.setPosition(new BigInteger(position, 16).toString());
        //状态变位信息解析
        List<TransWithMarkedStateSubEntity> transWithMarkedStateSubEntityList = new ArrayList<>();
        //获取剩余的字节
        int[] stateData = new int[infoElement.length - 5];
        for (int i = 0; i < stateData.length; i++){
            stateData[i] = infoElement[i + 5];
        }

        for (int i = 0; i < infoElement[2]; i++) {
            TransWithMarkedStateSubEntity transWithMarkedStateSubEntity = new TransWithMarkedStateSubEntity();
            //功能类型
            transWithMarkedStateSubEntity.setFunctionType(stateData[i * 3]);
            //信息序号
            transWithMarkedStateSubEntity.setInfoNumber(stateData[i * 3 + 1]);
            //双点信息
            transWithMarkedStateSubEntity.setDpi(stateData[i * 3 + 2] & 0x03);
            transWithMarkedStateSubEntityList.add(transWithMarkedStateSubEntity);
            transWithMarkedStateEntity.setTransWithMarkedStateSubEntity(transWithMarkedStateSubEntityList);
        }
        transWithMarkedStateEntityList.add(transWithMarkedStateEntity);
        asduEntity.setInformationBodyList(transWithMarkedStateEntityList);
        return asduEntity;
    }

}
