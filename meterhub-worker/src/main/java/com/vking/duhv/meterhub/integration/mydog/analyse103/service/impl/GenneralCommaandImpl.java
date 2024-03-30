package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.InformationBodyEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-19 15:30
 * 一般命令
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.GENERAL_COMMANDS)
@Slf4j
@Service
public class GenneralCommaandImpl implements AnalysHandlerService {
    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        //信息体数组对象
        List<InformationBodyEntity> informationBodyEntityList = new ArrayList<>();
        InformationBodyEntity informationBodyEntity = new InformationBodyEntity();
        //设置类型
        informationBodyEntity.setType(asduEntity.getTypeIdentifierValue());
        //双点信息
        informationBodyEntity.setSin(infoElement[0] & 0x03);
        //返回信息标识符
        informationBodyEntity.setReturnFlag(infoElement[1]);
        informationBodyEntityList.add(informationBodyEntity);
        asduEntity.setInformationBodyList(informationBodyEntityList);
        return asduEntity;
    }

}
