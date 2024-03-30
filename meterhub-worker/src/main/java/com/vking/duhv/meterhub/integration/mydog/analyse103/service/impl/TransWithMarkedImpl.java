package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.InformationBodyEntity;
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
 * @date 2023-12-19 18:58
 * 带标志的状态变位传输准备就绪
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.MARKED_TRANSMISSION_READY)
@Slf4j
@Service
public class TransWithMarkedImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<InformationBodyEntity> informationBodyEntityList = new ArrayList<>();
        InformationBodyEntity messgeData = new InformationBodyEntity();
        //设置类型
        messgeData.setType(asduEntity.getTypeIdentifierValue());
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        messgeData.setFault(new BigInteger(fault, 16).toString());
        informationBodyEntityList.add(messgeData);
        asduEntity.setInformationBodyList(informationBodyEntityList);
        return asduEntity;
    }

}
