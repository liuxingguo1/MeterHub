package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.MeasuredValueTwoEntity;
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
 * @date 2023-12-18 15:30
 * 被测值Ⅱ
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.MEASURED_VALUE_TWO)
@Slf4j
@Service
public class MeasuredValueTwoImpl implements AnalysHandlerService {
    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        //信息体数组对象
        List<MeasuredValueTwoEntity> messgeDataEntityList = new ArrayList<>();
        for (int i = 1; i <= asduEntity.getInfoTotal(); i++) {
            MeasuredValueTwoEntity measuredValueTwoEntity = new MeasuredValueTwoEntity();
            //设置类型
            measuredValueTwoEntity.setType(asduEntity.getTypeIdentifierValue());
//            //功能类型
//            totalQueryStopEntity.setFunctionType(infoElement[(i-1) * 20] & 0xFF);
//            //信息序号
//            totalQueryStopEntity.setInfoNumber(infoElement[(i-1) * 20 + 1] & 0xFF);

            //A相电流
            //转变成16进制字符
            String CurrentAHexString = ByteUtil.analysHex(infoElement[(i-1) * 18],infoElement[(i-1) * 18 + 1]);
            //被测值
            measuredValueTwoEntity.setCurrentA(getMeasuredValue(CurrentAHexString, measuredValueTwoEntity, builder));

            //B相电流
            //转变成16进制字符
            String CurrentBHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 2],infoElement[(i-1) * 18 + 3]);
            //被测值
            measuredValueTwoEntity.setCurrentB(getMeasuredValue(CurrentBHexString, measuredValueTwoEntity, builder));

            //C相电流
            //转变成16进制字符
            String CurrentCHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 4],infoElement[(i-1) * 18 + 5]);
            //被测值
            measuredValueTwoEntity.setCurrentC(getMeasuredValue(CurrentCHexString, measuredValueTwoEntity, builder));

            //A相电压
            //转变成16进制字符
            String voltageAHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 6],infoElement[(i-1) * 18 + 7]);
            //被测值
            measuredValueTwoEntity.setVoltageA(getMeasuredValue(voltageAHexString, measuredValueTwoEntity, builder));

            //B相电压
            //转变成16进制字符
            String voltageBHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 8],infoElement[(i-1) * 18 + 9]);
            //被测值
            measuredValueTwoEntity.setVoltageB(getMeasuredValue(voltageBHexString, measuredValueTwoEntity, builder));

            //C相电压
            //转变成16进制字符
            String voltageCHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 10],infoElement[(i-1) * 18 + 11]);
            //被测值
            measuredValueTwoEntity.setVoltageC(getMeasuredValue(voltageCHexString, measuredValueTwoEntity, builder));

            //有功功率
            //转变成16进制字符
            String activePowerHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 12],infoElement[(i-1) * 18 + 13]);
            //被测值
            measuredValueTwoEntity.setActivePower(getMeasuredValue(activePowerHexString, measuredValueTwoEntity, builder));

            //无功功率
            //转变成16进制字符
            String reactivePowerHexString = ByteUtil.analysHex(infoElement[(i-1) * 18 + 14],infoElement[(i-1) * 18 + 15]);
            //被测值
            measuredValueTwoEntity.setReactivePower(getMeasuredValue(reactivePowerHexString, measuredValueTwoEntity, builder));

            //频率
            //转变成16进制字符
            String frequency = ByteUtil.analysHex(infoElement[(i-1) * 18 + 16],infoElement[(i-1) * 18 + 17]);
            //被测值
            measuredValueTwoEntity.setFrequency(getMeasuredValue(frequency, measuredValueTwoEntity, builder));
            messgeDataEntityList.add(measuredValueTwoEntity);
        }
        asduEntity.setInformationBodyList(messgeDataEntityList);
        return asduEntity;
    }


    /**
     * 获得被测值
     * @param hexString
     * @param measuredValue
     * @param builder
     * @return
     */
    public static String getMeasuredValue(String hexString, MeasuredValueTwoEntity measuredValue, StringBuilder builder) {
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
            measuredValue.setOverflow(false);
            builder.append("[被测值:无效]");
        }else if ((new BigInteger(hexString,16).intValue() & 0x0002) == 0){
            measuredValue.setOverflow(true);
            builder.append("[被测值:有效]");
        }
        return String.valueOf((new BigInteger(hexString,16).intValue() & 0xfff8));
    }
}
