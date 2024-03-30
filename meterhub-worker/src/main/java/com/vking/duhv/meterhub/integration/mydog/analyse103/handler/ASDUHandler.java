package com.vking.duhv.meterhub.integration.mydog.analyse103.handler;


import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TransferReasonEnum;
import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferInfoNumberException;
import com.vking.duhv.meterhub.integration.mydog.exception.UnknownTransferReasonException;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 解析 ASDU
 */
@Component
@Slf4j
public class ASDUHandler {

    @Autowired
    private AnalysHandlerContext handlerContest;


    /**
     * @param asdu
     * @param builder
     * @return asduentity
     */
    public ASDUBaseEntity ASDU_analysis(int[] asdu, StringBuilder builder)  {
        ASDUBaseEntity asduEntity = new ASDUBaseEntity();
        try {
            asduEntity.setTypeIdentifierValue(asdu[0]);
            if ((asdu[1] & 0x80) == 128) {
                asduEntity.setSq(1);
            } else if ((asdu[1] & 0x80) == 0) {
                asduEntity.setSq(0);
            }
            asduEntity.setInfoTotal(asdu[1] & 0x7f);
            asduEntity.setReason(asdu[2]);
            asduEntity.setMessageAddress(asdu[3]);
            asduEntity.setFunctionType(asdu[4]);
            asduEntity.setInfoNumber(asdu[5]);
            builder.append("类属性标识符:").append(TypeIdentifier.getDescribe(asdu[0])).append("\n");
            builder.append("可变结构限定词:").append("SQ=").append(asduEntity.getSq()).append("信息个数:").append(asdu[1] & 0x7f).append("\n");
            builder.append("传送原因:").append(asdu[2]).append("\n");
            builder.append("应用服务数据单元公共地址:").append(asdu[3]).append("\n");
            builder.append("功能类型:").append(asdu[4]).append("\n");
            builder.append("信息序号:").append(asdu[5]).append("\n");
            //获取剩余的字符(信息体)
            int info[] = new int[asdu.length - 6];
            for (int i = 0; i < info.length; i++) {
                info[i] = asdu[6 + i];
            }
            //获取报文类型对应的解析器
            AnalysHandlerService analysHandlerService = handlerContest.getHanler(TypeIdentifier.getName(asduEntity.getTypeIdentifierValue()));
            AsduMessageInfo asduMessageInfo = new AsduMessageInfo();
            asduMessageInfo.setAsduEntity(asduEntity);
            asduMessageInfo.setAsduMessage(info);
            asduEntity = analysHandlerService.analysMessage(asduMessageInfo);
            log.info("ASDU实体通用信息:{}",asduEntity);
            System.out.println(builder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return asduEntity;
    }

    /**
     * 解析信息序号
     * @param i
     * @param messgeData
     * @return
     */
    public static String InfoNumberAnalysis(int i, ASDUBaseEntity messgeData) throws UnknownTransferInfoNumberException {
        StringBuilder builder = new StringBuilder();
        int infoNumber = i & 0xFF;
        messgeData.setInfoNumber(infoNumber);
//        builder.append("[信息序号:").append(InfoNumberForMonitorEnum.getDescribe(infoNumber)).append("]");
        return builder.toString();
    }

    /**
     * 解析功能类型
     * @param i
     * @param messgeData
     * @return
     */
    public static String FunAnalysis(int i, ASDUBaseEntity messgeData){
        StringBuilder builder = new StringBuilder();
        int functionType = i & 0xFF;
        messgeData.setFunctionType(functionType);
        return builder.toString();
    }

    /**
     * 解析传送原因
     * @param i
     * @param asduEntity
     * @return
     * @throws UnknownTransferReasonException
     */
    public static String CotAnalysis(int i, ASDUBaseEntity asduEntity) throws UnknownTransferReasonException {
        StringBuilder builder = new StringBuilder();
        int reasonCode = i & 0xFF;
        asduEntity.setReason(reasonCode);
        builder.append("[原因:").append(TransferReasonEnum.getDdescribe(reasonCode)).append("]");
        return builder.toString();
    }


    /**
     * 解析可变结构限定词
     * @param b
     * @param asduEntity
     * @return
     */
    private static String VariTureDete(int b, ASDUBaseEntity asduEntity) {
        String vvString = "可变结构限定词:";
        vvString += ByteUtil.toHexString(b);
        vvString += "   ";
        if ((b & 0x80) == 128) {
            asduEntity.setSq(1);
            vvString += "SQ=1 信息元素地址顺序";
        } else if ((b & 0x80) == 0) {
            asduEntity.setSq(0);
            vvString += "SQ=0  信息元素地址非顺序";
        }
        vvString += "信息元素个数：";
        int infoTotal = b & 0x7f;
        vvString += infoTotal;
        asduEntity.setInfoTotal(infoTotal);
        return vvString;
    }
}
