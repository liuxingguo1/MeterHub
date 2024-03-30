package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.MeasuredValueEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.MeasuredValueOneEntity;
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
 * @date 2023-12-18 10:44
 * 具有相对时间的带时标的被测值
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.RELATIVE_TIME_MEASURED_VALUE)
@Slf4j
@Service
public class MeasuredValueImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        StringBuilder builder = new StringBuilder();
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<MeasuredValueEntity> measuredValueEntityList = new ArrayList<>();
        for (int i = 1; i <= asduEntity.getInfoTotal(); i++) {
            MeasuredValueEntity measuredValue = new MeasuredValueEntity();
            //设置类型
            measuredValue.setType(asduEntity.getTypeIdentifierValue());
//            measuredValue.setFunctionType(infoElement[(i-1) * 14] & 0xFF);
//            builder.append("[功能类型:").append(TransferFun.getDdescribe(infoElement[(i-1) * 14] & 0xFF)).append("]");
//            measuredValue.setInfoNumber(infoElement[(i-1) * 14 + 1] & 0xFF);
//        builder.append("[信息序号:").append(InfoNumberForMonitorEnum.getDescribe(infoNumber)).append("]");

            //短路位置
            //浮点数1
            String floatOne = ByteUtil.toHexStringNo0x(infoElement[(i-1) * 12]);
            //浮点数2
            String floatTwo = ByteUtil.toHexStringNo0x(infoElement[(i-1) * 12 + 1]);
            //浮点数3
            String floatThree = ByteUtil.toHexStringNo0x(infoElement[(i-1) * 12 + 2]);
            //浮点数4
            String floatFour = ByteUtil.toHexStringNo0x(infoElement[(i-1) * 12 + 3]);
            //计算最终的短浮点值
            measuredValue.setShortCircuitPosition(Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));

            //相对时间
            //转变成16进制字符
            String relateTime = ByteUtil.analysHex(infoElement[(i-1) * 12 + 4],infoElement[(i-1) * 12 + 5]);
            //16进制转为10进制
            measuredValue.setRelateTime(new BigInteger(relateTime, 16).toString());
            //故障序号
            //转变成16进制字符
            String fault = ByteUtil.analysHex(infoElement[(i-1) * 12 + 6],infoElement[(i-1) * 12 + 7]);
            //16进制转为10进制
            measuredValue.setFault(new BigInteger(fault, 16).toString());
            //解析绝对时间（4个字节时间组）
            int time[] = new int[4];
            for (int j = 0; j < 4; j++) {
                time[j] = infoElement[(i - 1) * 12 + 8 + j];
            }
            val date = ByteUtil.TimeScaleForFour(time);
            measuredValue.setTime(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println(simpleDateFormat.format(date));
            builder.append("[时标CP56Time2a]"+ simpleDateFormat.format(date));
            measuredValueEntityList.add(measuredValue);
        }
        asduEntity.setInformationBodyList(measuredValueEntityList);
        return asduEntity;
    }


    /**
     * 获得被测值
     * @param hexString
     * @param measuredValue
     * @param builder
     * @return
     */
    private static String getMeasuredValue(String hexString, MeasuredValueOneEntity measuredValue, StringBuilder builder) {
        //是否溢出
        if ((new BigInteger(hexString,16).intValue() & 0x0001) == 1){
            measuredValue.setOverflow(true);
            builder.append("[B相电流:溢出]");
        }else if((new BigInteger(hexString,16).intValue() & 0x0001) == 0){
            measuredValue.setOverflow(false);
            builder.append("[B相电流:无溢出]");
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

    /**
     * 短浮点数
     * @param data 从低位到高位按顺序
     * @return
     */
    public static float Bytes2Float_IEEE754(String data) {
        return Float.intBitsToFloat(Integer.valueOf(data, 16));
    }

}
