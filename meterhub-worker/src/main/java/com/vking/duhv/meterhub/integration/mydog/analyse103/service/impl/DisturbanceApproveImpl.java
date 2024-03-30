package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.DisturbanceCommandEntity;
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
 * 扰动数据传输的认可
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.DISTURBANCE_DATA_TRANSMISSION_APPROVE)
@Slf4j
@Service
public class DisturbanceApproveImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<DisturbanceCommandEntity> disturbanceCommandEntityList = new ArrayList<>();
        DisturbanceCommandEntity disturbanceCommandEntity = new DisturbanceCommandEntity();
        //设置类型
        disturbanceCommandEntity.setType(asduEntity.getTypeIdentifierValue());
        //命令类型
        disturbanceCommandEntity.setCommandType(infoElement[0]);
        //扰动值的类型
        disturbanceCommandEntity.setDisturbValueType(infoElement[1]);
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        disturbanceCommandEntity.setFault(new BigInteger(fault, 16).toString());
        //实际通道序号
        disturbanceCommandEntity.setChannalNumber(infoElement[4]);
        disturbanceCommandEntityList.add(disturbanceCommandEntity);
        asduEntity.setInformationBodyList(disturbanceCommandEntityList);
        return asduEntity;
    }

}
