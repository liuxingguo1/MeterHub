package com.vking.duhv.meterhub.integration.mydog.analyse103.service.impl;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ChannelTransReadyEntity;
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
 * @date 2023-12-19 17:58
 * 被记录的通道传输准备就绪
 */
@AnalysHanlerAnnotation(type = TypeIdentifier.RECORDED_CHANNEL_TRANSMISSION_READY)
@Slf4j
@Service
public class ChannelTransReadyImpl implements AnalysHandlerService {

    @Override
    public ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo) {
        int[] infoElement = asduMessageInfo.getAsduMessage();
        //ASDU实体对象
        ASDUBaseEntity asduEntity = asduMessageInfo.getAsduEntity();
        List<ChannelTransReadyEntity> channelTransReadyEntityList = new ArrayList<>();
        ChannelTransReadyEntity channelTransReady = new ChannelTransReadyEntity();
        //设置类型
        channelTransReady.setType(asduEntity.getTypeIdentifierValue());
        //扰动值的类型
        channelTransReady.setDisturbValueType(infoElement[1]);
        //故障序号
        //转变成16进制字符
        String fault = ByteUtil.analysHex(infoElement[2],infoElement[3]);
        //16进制转为10进制
        channelTransReady.setFault(new BigInteger(fault, 16).toString());
        //实际通道序号
        channelTransReady.setChannalNumber(infoElement[4]);

        //一次额定值
        //浮点数1
        String floatOne = ByteUtil.toHexStringNo0x(infoElement[5]);
        //浮点数2
        String floatTwo = ByteUtil.toHexStringNo0x(infoElement[6]);
        //浮点数3
        String floatThree = ByteUtil.toHexStringNo0x(infoElement[7]);
        //浮点数4
        String floatFour = ByteUtil.toHexStringNo0x(infoElement[8]);
        //计算最终的短浮点值
        channelTransReady.setPrimaryRatedValue(ByteUtil.Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));

        //二次额定值
        //浮点数1
        String secondFloatOne = ByteUtil.toHexStringNo0x(infoElement[9]);
        //浮点数2
        String secondFloatTwo = ByteUtil.toHexStringNo0x(infoElement[10]);
        //浮点数3
        String secondFloatThree = ByteUtil.toHexStringNo0x(infoElement[11]);
        //浮点数4
        String secondFloatFour = ByteUtil.toHexStringNo0x(infoElement[12]);
        //计算最终的短浮点值
        channelTransReady.setSecondaryRatedValue(ByteUtil.Bytes2Float_IEEE754(secondFloatOne+ secondFloatTwo+ secondFloatThree +secondFloatFour));

        //参比因子
        //浮点数1
        String referenceFactorFloatOne = ByteUtil.toHexStringNo0x(infoElement[13]);
        //浮点数2
        String referenceFactorFloatTwo = ByteUtil.toHexStringNo0x(infoElement[14]);
        //浮点数3
        String referenceFactorFloatThree = ByteUtil.toHexStringNo0x(infoElement[15]);
        //浮点数4
        String referenceFactorFloatFour = ByteUtil.toHexStringNo0x(infoElement[16]);
        //计算最终的短浮点值
        channelTransReady.setReferenceFactor(ByteUtil.Bytes2Float_IEEE754(referenceFactorFloatOne+ referenceFactorFloatTwo+ referenceFactorFloatThree +referenceFactorFloatFour));
        channelTransReadyEntityList.add(channelTransReady);
        asduEntity.setInformationBodyList(channelTransReadyEntityList);
        return asduEntity;
    }

}
