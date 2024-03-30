package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.IdentifyEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-18 15:07
 * 标识报文
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.IDENTIFYING)
@Slf4j
@Service
public class IdentifyImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<IdentifyEntity> identifyEntityList = new ArrayList<>();
        IdentifyEntity measuredValue = new IdentifyEntity();
        //设置类型
        measuredValue.setType(asduEntity.getTypeIdentifierValue());
        //设置兼容级别
        measuredValue.setCompatibleLevel(infoElement[0]);
        //设置字符
        measuredValue.setASCII1(infoElement[1]);
        measuredValue.setASCII2(infoElement[2]);
        measuredValue.setASCII3(infoElement[3]);
        measuredValue.setASCII4(infoElement[4]);
        measuredValue.setASCII5(infoElement[5]);
        measuredValue.setASCII6(infoElement[6]);
        measuredValue.setASCII7(infoElement[7]);
        measuredValue.setASCII8(infoElement[8]);
        //自由赋值（制造厂内部软件标识符）
        measuredValue.setFreeAssignment1(infoElement[9]);
        measuredValue.setFreeAssignment2(infoElement[10]);
        measuredValue.setFreeAssignment3(infoElement[11]);
        measuredValue.setFreeAssignment4(infoElement[12]);
        identifyEntityList.add(measuredValue);
        asduEntity.setInformationBodyList(identifyEntityList);
        return asduEntity;
    }

}
