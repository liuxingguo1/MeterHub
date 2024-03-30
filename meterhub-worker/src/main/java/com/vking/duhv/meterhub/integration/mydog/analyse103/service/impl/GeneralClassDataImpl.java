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
 * @date 2023-12-18 17:15
 * 通用分类数据
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.GENERAL_CLASS_DATA)
@Slf4j
@Service
public class GeneralClassDataImpl implements AnalysHandlerService {

    @Autowired
    private GIDHandler getGidData;

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<GeneralClassEntity> generalClassEntityList = new ArrayList<>();
        GeneralClassEntity generalClassEntity = new GeneralClassEntity();
        //设置类型
        generalClassEntity.setType(asduEntity.getTypeIdentifierValue());
        //返回信息标识符
        generalClassEntity.setReturnFlag(infoElement[0]);
        //通用分类数据集数目
        int genClassNum = infoElement[1] & 0x3F;
        generalClassEntity.setNo(genClassNum);
        //计数器位
        if ((infoElement[1] & 0x40) == 1){
            generalClassEntity.setCont(true);
        } else if ((infoElement[1] & 0x40) == 0) {
            generalClassEntity.setCont(false);
        }
        //后续状态位
        if ((infoElement[1] & 0x80) == 1){
            generalClassEntity.setCont(true);
        } else if ((infoElement[1] & 0x80) == 0) {
            generalClassEntity.setCont(false);
        }
        //获取到剩余的字节，除去 返回信息标识符（1字节）、通用分类数据集数目（1字节）
        int genClassData[] = new int[infoElement.length - 2];
        for (int j = 0; j < infoElement.length - 2; j++) {
            genClassData[j] = infoElement[2 + j];
        }

        //<!----------------------------以下逻辑是解析GID------------------------------->

        List<GeneralClassSubEntity> generalClassSubEntityList = new ArrayList<>();
        GeneralClassSubEntity generalClassSubEntity = new GeneralClassSubEntity();
        for (int i = 0; i < genClassData.length ; i++) {
            //通用分类标识序号
            generalClassSubEntity.setGroupNumber(genClassData[0]);
            generalClassSubEntity.setEntryNumber(genClassData[1]);
            //描述类别
            generalClassSubEntity.setDscrType(genClassData[2]);
            //通用分类数据描述
            //数据类型
            int dataType = genClassData[3];
            generalClassSubEntity.setDataType(genClassData[3]);
            //数据宽度
            int dataSize = genClassData[4];
            generalClassSubEntity.setDataSize(dataSize);
            //数目
            int number = genClassData[5] & 0x7f;
            generalClassSubEntity.setNumber(number);
            //后续状态位
            if ((genClassData[5] & 0x80) == 1){
                generalClassSubEntity.setDataCont(true);
            } else if ((genClassData[5] & 0x80) ==  0) {
                generalClassSubEntity.setDataCont(false);
            }
            //组装GID相关信息
            int space = 6;
            try{
                genClassData = getGidData.getGidData(space, dataSize, number, dataType, genClassData, generalClassSubEntity);
            }catch (Exception e){
                log.error("GID解析出错");
                e.printStackTrace();
            }
            generalClassSubEntityList.add(generalClassSubEntity);
        }
        generalClassEntity.setGeneralClassSubEntityList(generalClassSubEntityList);
        generalClassEntityList.add(generalClassEntity);
        asduEntity.setInformationBodyList(generalClassEntityList);
        return asduEntity;
    }
}
