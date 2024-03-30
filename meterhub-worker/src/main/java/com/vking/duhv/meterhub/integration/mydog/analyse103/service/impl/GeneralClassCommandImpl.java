package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.GeneralClassEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.GeneralClassSubEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-20 10:50
 * 通用分类命令
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.GENERAL_CLASS_COMMANDS)
@Slf4j
@Service
public class GeneralClassCommandImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<GeneralClassEntity> generalClassEntityList = new ArrayList<>();
        GeneralClassEntity generalClassEntity = new GeneralClassEntity();
        //返回信息标识符
        generalClassEntity.setReturnFlag(infoElement[0]);
        //通用分类标识数目
        generalClassEntity.setNo(infoElement[1]);

        List<GeneralClassSubEntity> generalClassSubEntityList = new ArrayList<>();
        //获取剩余的字节
        int[] dataArray = new int[infoElement.length - 2];
        for (int i = 0; i < dataArray.length; i++){
            dataArray[i] = infoElement[i + 2];
        }
        GeneralClassSubEntity generalClassSubEntity = new GeneralClassSubEntity();
        for (int i = 0; i < infoElement[1]; i++) {
            //通用分类标识序号
            generalClassSubEntity.setGroupNumber(infoElement[i * 3]);
            generalClassSubEntity.setEntryNumber(infoElement[i * 3 + 1]);
            //描述类别
            generalClassSubEntity.setDscrType(infoElement[i * 3 + 2]);
            generalClassSubEntityList.add(generalClassSubEntity);
            generalClassEntity.setGeneralClassSubEntityList(generalClassSubEntityList);
        }
        generalClassEntityList.add(generalClassEntity);
        asduEntity.setInformationBodyList(generalClassEntityList);
        return asduEntity;
    }

}
