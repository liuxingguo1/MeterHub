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

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-18 15:30
 * 解析时间同步的报文
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.TIME_SYNCHRONIZATION)
@Slf4j
@Service
public class TimeSynchronizImpl implements AnalysHandlerService {
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
        //7个字节时间组
        informationBodyEntity.setTime(ByteUtil.TimeScaleForSeven(infoElement));
        informationBodyEntityList.add(informationBodyEntity);
        asduEntity.setInformationBodyList(informationBodyEntityList);
        return asduEntity;
    }

}
