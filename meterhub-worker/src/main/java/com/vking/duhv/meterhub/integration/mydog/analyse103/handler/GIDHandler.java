package com.vking.duhv.meterhub.integration.mydog.analyse103.handler;

import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.GeneralClassSubEntity;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import lombok.val;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GIDHandler {

    /**
     * 组装GID信息
     * @param space
     * @param dataSize
     * @param number
     * @param dataType
     * @param genClassData
     * @param generalClassSubEntity
     * @return
     */
    public static int[] getGidData(int space, int dataSize, int number, int dataType, int[] genClassData, GeneralClassSubEntity generalClassSubEntity) {
        StringBuilder sbuilder = new StringBuilder();
        //当前数据集的字节长度
        int cruDataNum = space + dataSize * number;
        int gidNum = 0;
        int num = 0;
        switch (dataType){
            case 1: //ASCII8位码
                //GID的长度
                gidNum = dataSize * number;
                //一个汉字两个字节，求汉字数量
                num = gidNum/2;
                sbuilder = new StringBuilder();
                for (int j = 0; j < num ; j++) {
                    //转变成16进制字符
                    String hexString = ByteUtil.analysHex(genClassData[j * 2 + space], genClassData[j * 2 + space + 1]);
                    //将16进制转换成汉字
                    byte[] bytes = ByteUtil.hexStringToBytes(hexString);
                    String chineseString = new String(bytes, StandardCharsets.UTF_8); // 将字节数组转换为汉字字符串
//                    int decimal = Integer.parseInt(hexString, 16); // 将16进制转换为十进制
//                    char asciiChar = (char) decimal;
                    sbuilder.append(chineseString);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 2: //成组8位串   gid一般是一个字节
                int gidData = genClassData[space];
                int gidData7 =  gidData & 0x80;
                int gidData6 =  gidData & 0x40;
                int gidData5 =  gidData & 0x20;
                int gidData4 =  gidData & 0x10;
                int gidData3 =  gidData & 0x08;
                int gidData2 =  gidData & 0x04;
                int gidData1 =  gidData & 0x02;
                int gidData0 =  gidData & 0x01;
                sbuilder.append(gidData7).append(gidData6).append(gidData5).append(gidData4)
                        .append(gidData3).append(gidData2).append(gidData1).append(gidData0);
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
            case 3: //无符号整数
                //GID的长度
                gidNum = dataSize * number;
                sbuilder = new StringBuilder();
                for (int j = 1; j <= gidNum; j++) {
                    sbuilder.append(genClassData[space + j]);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 4: //整数
                //GID的长度
                gidNum = dataSize * number;
                sbuilder = new StringBuilder();
                for (int j = 1; j <= gidNum; j++) {
                    sbuilder.append(genClassData[space + j]);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 5: //无符号浮点数,即没有正负数的区分
                //GID的长度
                gidNum = dataSize * number;
                //一个浮点数占四个字节，求浮点值数量
                num = gidNum/4;
                sbuilder = new StringBuilder();
                for (int j = 0; j < num ; j++) {
                    //浮点数1
                    String floatOne = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space]);
                    //浮点数2
                    String floatTwo = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 1]);
                    //浮点数3
                    String floatThree = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 2]);
                    //浮点数4
                    String floatFour = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 3]);
                    //计算最终的短浮点值
                    String floatValue = String.valueOf(ByteUtil.Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
                    sbuilder.append(floatValue);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 6: //浮点数
                //GID的长度
                gidNum = dataSize * number;
                //一个浮点数占四个字节，求浮点值数量
                num = gidNum/4;
                sbuilder = new StringBuilder();
                for (int j = 0; j < num ; j++) {
                    //浮点数1
                    String floatOne = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space]);
                    //浮点数2
                    String floatTwo = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 1]);
                    //浮点数3
                    String floatThree = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 2]);
                    //浮点数4
                    String floatFour = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 3]);
                    //计算最终的短浮点值
                    String floatValue = String.valueOf(ByteUtil.Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
                    sbuilder.append(floatValue);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 7: //IEEE标准754短实数
                //GID的长度
                gidNum = dataSize * number;
                //一个浮点数占四个字节，求浮点值数量
                num = gidNum/4;
                sbuilder = new StringBuilder();
                for (int j = 0; j < num ; j++) {
                    //浮点数1
                    String floatOne = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space]);
                    //浮点数2
                    String floatTwo = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 1]);
                    //浮点数3
                    String floatThree = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 2]);
                    //浮点数4
                    String floatFour = ByteUtil.toHexStringNo0x(genClassData[j * 4 + space + 3]);
                    //计算最终的短浮点值
                    String floatValue = String.valueOf(ByteUtil.Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
                    sbuilder.append(floatValue);
                }
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 9: //双点信息  gid应该是只有一个字节
                gidData = genClassData[space] & 0x0b;
                sbuilder.append(gidData);
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 10: //单点信息     gid应该是只有一个字节
                gidData = genClassData[space] & 0x01;
                sbuilder.append(gidData);
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 11: //带瞬变和差错的双点信息  gid应该是只有一个字节
                gidData = genClassData[space] & 0x0b;
                sbuilder.append(gidData);
                generalClassSubEntity.setGidData(sbuilder.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 12: //带品质描述词的被测值
                //GID的长度
                gidNum = dataSize * number;
                //一个汉字两个字节，求汉字数量
                num = gidNum/2;
                sbuilder = new StringBuilder();
                List<Map<String,Object>> list = new ArrayList<>();
                for (int j = 0; j < num ; j++) {
                    //转变成16进制字符
                    String hexString = ByteUtil.analysHex(genClassData[j * 2 + space], genClassData[j * 2 + space + 1]);
                    Map<String,Object> map = new HashMap<>();
                    //是否溢出
                    if ((new BigInteger(hexString,16).intValue() & 0x0001) == 1){
                        map.put("overflow",true);
                    }else if((new BigInteger(hexString,16).intValue() & 0x0001) == 0){
                        map.put("overflow",false);
                    }
                    //被测值是否有效
                    if ((new BigInteger(hexString,16).intValue() & 0x0002) == 1){
                        map.put("erroneousBit",false);
                    }else if ((new BigInteger(hexString,16).intValue() & 0x0002) == 0){
                        map.put("erroneousBit",true);
                    }
                    //被测值
                    map.put("value",(new BigInteger(hexString,16).intValue() & 0xfff8));
                    list.add(map);
                }
                generalClassSubEntity.setGidData(list.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 14: //二进制时间 （7个字节时间组）
                int time[] = new int[7];
                time[0] = genClassData[space];
                time[1] = genClassData[space + 1];
                time[2] = genClassData[space + 2];
                time[3] = genClassData[space + 3];
                time[4] = genClassData[space + 4];
                time[5] = genClassData[space + 5];
                time[6] = genClassData[space + 6];
                val date = ByteUtil.TimeScaleForSeven(time);
                generalClassSubEntity.setGidData(date.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 15: //通用分类标识序号
                //GID的长度
                gidNum = dataSize * number;
                //一个序号占两个字节，求序号数量
                num = gidNum/2;
                sbuilder = new StringBuilder();
                List<Map<String,Object>> list1 = new ArrayList<>();
                for (int j = 0; j < num ; j++) {
                    Map<String,Object> map = new HashMap<>();
                    int groupNumber = genClassData[j * 2 + space] & 0xff;
                    int entryNumber = genClassData[j * 2 + space + 1] & 0xff;
                    map.put("groupNumber",groupNumber);
                    map.put("entryNumber",entryNumber);
                    list1.add(map);
                }
                generalClassSubEntity.setGidData(list1.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 16: //相对时间
                //转变成16进制字符
                String relateTime = ByteUtil.analysHex(genClassData[space], genClassData[space + 1]);
                generalClassSubEntity.setGidData(new BigInteger(relateTime, 16).toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 17: //功能类型和信息序号
                Map<String,Object> map = new HashMap<>();
                map.put("functionType", genClassData[space]);
                map.put("infoNumber", genClassData[space + 1]);
                generalClassSubEntity.setGidData(map.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 18: //带时标的报文
                Map<String,Object> map1 = new HashMap<>();
                //双点信息
                int dpi = genClassData[space] & 0x0b;
                map1.put("dpi",dpi);
                //解析绝对时间（4个字节时间组）
                int time1[] = new int[4];
                time1[0] = genClassData[space + 1];
                time1[1] = genClassData[space + 2];
                time1[2] = genClassData[space + 3];
                time1[3] = genClassData[space + 4];
                map1.put("time",ByteUtil.TimeScaleForFour(time1));
                generalClassSubEntity.setGidData(map1.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 19: //带相对时间的时标报文
                Map<String,Object> map2 = new HashMap<>();
                //双点信息
                int dpi2 = genClassData[space] & 0x0b;
                map2.put("dpi",dpi2);
                //相对时间
                String relateTime2 = ByteUtil.analysHex(genClassData[space + 1], genClassData[space + 2]);
                map2.put("relateTime",new BigInteger(relateTime2, 16).toString());
                //故障序号
                //转变成16进制字符
                String fault = ByteUtil.analysHex(genClassData[space + 3], genClassData[space + 4]);
                //16进制转为10进制
                map2.put("fault",new BigInteger(fault, 16).toString());
                //解析绝对时间（4个字节时间组）
                int time2[] = new int[4];
                time2[0] = genClassData[space + 5];
                time2[1] = genClassData[space + 6];
                time2[2] = genClassData[space + 7];
                time2[3] = genClassData[space + 8];
                map2.put("time",ByteUtil.TimeScaleForFour(time2));
                //附加信息
                map2.put("sin", genClassData[space + 9]);
                generalClassSubEntity.setGidData(map2.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 20: //带相对时间的时标的被测值
                Map<String,Object> map3 = new HashMap<>();
                //被测值
                //浮点数1
                String floatOne = ByteUtil.toHexStringNo0x(genClassData[space]);
                //浮点数2
                String floatTwo = ByteUtil.toHexStringNo0x(genClassData[space + 1]);
                //浮点数3
                String floatThree = ByteUtil.toHexStringNo0x(genClassData[space + 2]);
                //浮点数4
                String floatFour = ByteUtil.toHexStringNo0x(genClassData[space + 3]);
                //计算最终的短浮点值
                String floatValue = String.valueOf(ByteUtil.Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
                map3.put("value",floatValue);
                //相对时间
                String relateTime3 = ByteUtil.analysHex(genClassData[space + 4], genClassData[space + 5]);
                map3.put("relateTime",new BigInteger(relateTime3, 16).toString());
                //故障序号
                //转变成16进制字符
                String fault3 = ByteUtil.analysHex(genClassData[space + 6], genClassData[space + 7]);
                //16进制转为10进制
                map3.put("fault",new BigInteger(fault3, 16).toString());
                //解析绝对时间（4个字节时间组）
                int time3[] = new int[4];
                time3[0] = genClassData[space + 8];
                time3[1] = genClassData[space + 9];
                time3[2] = genClassData[space + 10];
                time3[3] = genClassData[space + 11];
                map3.put("time",ByteUtil.TimeScaleForFour(time3));
                generalClassSubEntity.setGidData(map3.toString());
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            case 21: //外部文本序号
                break;
            case 22: //通用分类回答码
                generalClassSubEntity.setGidData(String.valueOf(genClassData[space]));
                genClassData = getNewDataArray(genClassData,cruDataNum);
                break;
            default:
                break;
        }
        return genClassData;
    }

    public static int[] getNewDataArray(int[] newArray ,int x){
        int[] array = new int[newArray.length-x];
        for (int i = 0; i < newArray.length-x; i++) {
            array[i] = newArray[x+i];
        }
        return array;
    }
}
