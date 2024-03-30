package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.MeasuredValueOneEntity;
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
 * @date 2023-12-15 14:44
 * 被测值I
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.MEASURED_VALUE_ONE)
@Slf4j
@Service
public class MeasuredValueOneImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<MeasuredValueOneEntity> measuredValueOneEntityList = new ArrayList<>();
        for (int i = 1; i <= asduEntity.getInfoTotal(); i++) {
            MeasuredValueOneEntity measuredValue = new MeasuredValueOneEntity();
            //设置类型
            measuredValue.setType(asduEntity.getTypeIdentifierValue());
//            measuredValue.setFunctionType(infoElement[(i-1) * 10] & 0xFF);
//            builder.append("[功能类型:").append(TransferFun.getDdescribe(infoElement[0] & 0xFF)).append("]");
//            measuredValue.setInfoNumber(infoElement[(i-1) * 10 + 1] & 0xFF);
//        builder.append("[信息序号:").append(InfoNumberForMonitorEnum.getDescribe(infoNumber)).append("]");

            //B相电流
            //转变成16进制字符
            String CurrentBHexString = ByteUtil.analysHex(infoElement[(i-1) * 8],infoElement[(i-1) * 8 + 1]);
            //被测值
            measuredValue.setCurrentB(getMeasuredValue(CurrentBHexString, measuredValue, builder));

            //AB相线电压
            //转变成16进制字符1
            String voltageABHexString = ByteUtil.analysHex(infoElement[(i-1) * 8 + 2],infoElement[(i-1) * 8 + 3]);
            //被测值
            measuredValue.setVoltageAB(getMeasuredValue(voltageABHexString, measuredValue, builder));

            //有功功率
            //转变成16进制字符
            String activePowerHexString = ByteUtil.analysHex(infoElement[(i-1) * 8 + 4],infoElement[(i-1) * 8 + 5]);
            //被测值
            measuredValue.setActivePower(getMeasuredValue(activePowerHexString, measuredValue, builder));

            //无功功率
            //转变成16进制字符
            String reactivePowerHexString = ByteUtil.analysHex(infoElement[(i-1) * 8 + 6],infoElement[(i-1) * 8 + 7]);
            //被测值
            measuredValue.setReactivePower(getMeasuredValue(reactivePowerHexString, measuredValue, builder));
            measuredValueOneEntityList.add(measuredValue);
        }
        asduEntity.setInformationBodyList(measuredValueOneEntityList);
        return asduEntity;
    }


    /**
     * 获得被测值
     * @param hexString
     * @param measuredValue
     * @param builder
     * @return
     */
    public static String getMeasuredValue(String hexString, MeasuredValueOneEntity measuredValue, StringBuilder builder) {
        //是否溢出
        if ((new BigInteger(hexString,16).intValue() & 0x0001) == 1){
            measuredValue.setOverflow(true);
            builder.append("[溢出状态:溢出]");
        }else if((new BigInteger(hexString,16).intValue() & 0x0001) == 0){
            measuredValue.setOverflow(false);
            builder.append("[溢出状态:无溢出]");
        }
        //被测值是否有效
        if ((new BigInteger(hexString,16).intValue() & 0x0002) == 1){
            measuredValue.setErroneousBit(false);
            builder.append("[被测值:无效]");
        }else if ((new BigInteger(hexString,16).intValue() & 0x0002) == 0){
            measuredValue.setErroneousBit(true);
            builder.append("[被测值:有效]");
        }


        //注意：这个被测值是拿GDD类型为12的描述值，需要后面咨询厂家，根据配置修改取值方式
        return String.valueOf((new BigInteger(hexString,16).intValue() & 0xfff8));
    }
}
