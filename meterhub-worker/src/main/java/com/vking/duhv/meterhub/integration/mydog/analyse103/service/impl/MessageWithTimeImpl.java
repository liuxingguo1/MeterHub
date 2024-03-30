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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-15 11:06
 * 解析带时标的报文
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.MESSAGE_TIME)
@Slf4j
@Service
public class MessageWithTimeImpl implements AnalysHandlerService {
    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        //信息体数组对象
        List<InformationBodyEntity> informationBodyEntityList = new ArrayList<>();
        for (int i = 1; i <= asduEntity.getInfoTotal(); i++) {
            InformationBodyEntity informationBodyEntity = new InformationBodyEntity();
            //设置类型
            informationBodyEntity.setType(asduEntity.getTypeIdentifierValue());
            builder.append("信息元素");
            builder.append(i);
            builder.append("的内容如下：\n");
//            builder.append("功能类型:").append(FunAnalysis(infoElement[(i-1) * 8], messgeDataEntity)).append("\n");
//            builder.append("信息序号:").append(InfoNumberAnalysis(infoElement[(i-1) * 8 + 1], messgeDataEntity)).append("\n");
            //双点信息
            informationBodyEntity.setDpi(infoElement[(i-1) * 6] & 0x03);
            //4个字节时间组
            int time[] = new int[4];
            for (int j = 0; j < 4; j++) {
                time[j] = infoElement[(i - 1) * 6 + 1 + j];
            }
            informationBodyEntity.setTime(ByteUtil.TimeScaleForFour(time));
            //附加信息SIN,仅总查询有效通SCN,否则无意义
            int sin = infoElement[(i-1) * 6 + 5] & 0xff;
            informationBodyEntity.setSin(sin);
            informationBodyEntityList.add(informationBodyEntity);
            builder.append("信息DPI是：");
            builder.append(infoElement[0]).append("\n");
            builder.append("绝对时间是：");
            builder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(ByteUtil.TimeScaleForFour(time))).append("\n");
            builder.append("附加信息SIN是：");
            builder.append(infoElement[infoElement.length-1]).append("\n");
        }
        System.out.println(builder);
        asduEntity.setInformationBodyList(informationBodyEntityList);
        return asduEntity;
    }

}
