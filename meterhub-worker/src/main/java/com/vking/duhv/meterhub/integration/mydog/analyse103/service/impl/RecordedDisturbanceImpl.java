package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.RecordedDisturbanceEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-19 16:44
 * 被记录的扰动表
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.RECORDED_DISTURBANCE_TABLE)
@Slf4j
@Service
public class RecordedDisturbanceImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<RecordedDisturbanceEntity> recordedDisturbanceEntityList = new ArrayList<>();
        for (int i = 1; i <= asduEntity.getInfoTotal(); i++) {
            RecordedDisturbanceEntity recordedDisturbance = new RecordedDisturbanceEntity();
            //设置类型
            recordedDisturbance.setType(asduEntity.getTypeIdentifierValue());
            //故障序号
            //转变成16进制字符
            String fault = ByteUtil.analysHex(infoElement[(i-1) * 10],infoElement[(i-1) * 10 + 1]);
            //16进制转为10进制
            recordedDisturbance.setFault(new BigInteger(fault, 16).toString());
            //故障状态
            recordedDisturbance.setFaultState(String.valueOf(infoElement[(i-1) * 10 + 2]));
            //解析绝对时间（7个字节时间组）
            int time[] = new int[7];
            for (int j = 0; j < 7; j++) {
                time[j] = infoElement[(i - 1) * 10 + j];
            }
            val date = ByteUtil.TimeScaleForSeven(time);
            recordedDisturbance.setTime(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println(simpleDateFormat.format(date));
            builder.append("[时标CP56Time2a]"+ simpleDateFormat.format(date));
            recordedDisturbanceEntityList.add(recordedDisturbance);
        }
        asduEntity.setInformationBodyList(recordedDisturbanceEntityList);
        return asduEntity;
    }



}
