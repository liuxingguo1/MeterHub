package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.GeneralClassEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.GeneralClassSubEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.handler.GIDHandler;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-20 14:15
 * 通用分类标识
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.GENERAL_CLASS_SIGN)
@Slf4j
@Service
public class GeneralClassSignImpl implements AnalysHandlerService {

    @Autowired
    private GIDHandler getGidData;

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<GeneralClassEntity> generalClassEntityList = new ArrayList<>();
        GeneralClassEntity measuredValue = new GeneralClassEntity();
        //设置类型
        measuredValue.setType(asduEntity.getTypeIdentifierValue());
        //返回信息标识符
        measuredValue.setReturnFlag(infoElement[0]);
        //通用分类标识序号
        measuredValue.setGroupNumber(infoElement[1]);
        measuredValue.setEntryNumber(infoElement[2]);
        //描述元素的数目
        int descrElementCount = infoElement[3] & 0x3F;
        measuredValue.setNo(descrElementCount);
        //计数器位
        if ((infoElement[3] & 0x02) == 1){
            measuredValue.setCont(true);
        } else if ((infoElement[1] & 0x02) == 0) {
            measuredValue.setCont(false);
        }
        //后续状态位
        if ((infoElement[3] & 0x80) == 1){
            measuredValue.setCont(true);
        } else if ((infoElement[1] & 0x80) == 0) {
            measuredValue.setCont(false);
        }

        //获取到剩余的字节，除去 返回信息标识符(1字节)、通用分类标识序号(2字节)、通用分类数据集数目(1字节)
        int descrElementData[] = new int[infoElement.length - 4];
        for (int j = 0; j < infoElement.length - 4; j++) {
            descrElementData[j] = infoElement[4 + j];
        }


        //<!----------------------------以下逻辑是解析GID------------------------------->

        List<GeneralClassSubEntity> generalClassSubEntityList = new ArrayList<>();
        GeneralClassSubEntity generalClassSubEntity = new GeneralClassSubEntity();
        for (int i = 0; i < descrElementData.length ; i++) {
            //描述类别
            generalClassSubEntity.setDscrType(descrElementData[0]);
            //通用分类数据描述
            //数据类型
            int dataType = descrElementData[1];
            generalClassSubEntity.setDataType(dataType);
            //数据宽度
            int dataSize = descrElementData[2];
            generalClassSubEntity.setDataSize(dataSize);
            //数目
            int number = descrElementData[3] & 0x7f;
            generalClassSubEntity.setNumber(number);
            //后续状态位
            if ((descrElementData[3] & 0x80) == 1){
                generalClassSubEntity.setDataCont(true);
            } else if ((descrElementData[3] & 0x80) ==  0) {
                generalClassSubEntity.setDataCont(false);
            }
            //组装GID相关信息
            int space = 4;
            try{
                descrElementData = getGidData.getGidData(space, dataSize, number, dataType, descrElementData, generalClassSubEntity);
            }catch (Exception e){
                log.error("GID解析出错");
                e.printStackTrace();
            }
            generalClassSubEntityList.add(generalClassSubEntity);
        }
        measuredValue.setGeneralClassSubEntityList(generalClassSubEntityList);
        generalClassEntityList.add(measuredValue);
        asduEntity.setInformationBodyList(generalClassEntityList);
        return asduEntity;
    }
}
