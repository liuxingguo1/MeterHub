package com.vking.duhv.meterhub.integration.mydog.analyse104;


import com.vking.duhv.meterhub.integration.mydog.analyse104.enums.TypeIdentifierEnum;
import com.vking.duhv.meterhub.integration.mydog.analyse104.message.MessageDetail;
import com.vking.duhv.meterhub.integration.mydog.analyse104.message.MessageInfo;
import com.vking.duhv.meterhub.integration.mydog.utils.ByteUtil;
import com.vking.duhv.meterhub.integration.mydog.utils.Iec104Util;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: Decoder104
* @Description: 104 协议转码工具 
* @author 刘兴国
* @date 2024年3月31日
 */
@Component
public class Decoder104 {

	/**
	 * 将bytes 转换成指定的数据结构
	 * @param bytes
	 * @return
	 */
	public static MessageDetail encoder(byte[] bytes) {
		MessageDetail ruleDetail104 = new MessageDetail();
		int index = 0;
		ruleDetail104.setStart(bytes[index++]);
		ruleDetail104.setApuuLength(bytes[index++] & 0xFF);
		ruleDetail104.setControl(ByteUtil.getByte(bytes, index, 4));
		index += 4;
		if (ruleDetail104.getApuuLength() <= 4) {
			ruleDetail104.setMessages(new ArrayList<>());
			// 如果只有APCI 就此返回
			return ruleDetail104;
		} 
		// 下面是返回ASDU的结构
		ruleDetail104.setTypeIdentifier(TypeIdentifierEnum.getTypeIdentifierEnum(bytes[index++]));
		// 添加可变结构限定词
		Iec104Util.setChanged(ruleDetail104, bytes[index++]);
		ruleDetail104.setTransferReason(ByteUtil.byteArrayToShort(ByteUtil.getByte(bytes, index, 2)));
		index += 2;
		// 
		ruleDetail104.setTerminalAddress(Iec104Util.getTerminalAddressShort(ByteUtil.getByte(bytes, index, 2)));
		index += 2;
		Iec104Util.setMeaageAttribute(ruleDetail104);
		setMessage(ruleDetail104, bytes, index);
		return ruleDetail104;
	}

	/**
	 * 
	* @Title: setMessage  
	* @Description: 对消息进行编码  
	* @param @param ruleDetail104
	* @param @param bytes
	* @param @param index 
	* @return void   
	* @throws
	 */
	public static void setMessage(MessageDetail ruleDetail104, byte[] bytes, int index) {
		int mesageIndex = index;
		if (ruleDetail104.isContinuous()) {
			setContinuoustMessage(ruleDetail104, bytes, mesageIndex);
		}  else {
			setNoContinuoustMessage(ruleDetail104, bytes, mesageIndex);
		}
	} 
	
	
	/**
	 * 
	* @Title: setContinuoustMessage  
	* @Description: 设置连续地址的消息 
	* @param ruleDetail104
	* @param bytes
	* @param index 
	* @return void   
	* @throws
	 */
	public static void setContinuoustMessage(MessageDetail ruleDetail104, byte[] bytes, int index) {
		List<MessageInfo> messages = new ArrayList<>();
		int mesageIndex = index;
		// 连续的 前三个字节是地址 
		// 如果是地址联系的只需要设置一个初始地址就可以了
		// TODO 此处不处理地址
		int messageAddress = Iec104Util.messageAddressToInt(ByteUtil.getByte(bytes, mesageIndex, 3));
		ruleDetail104.setMessageAddress(messageAddress);
		mesageIndex += 3;
		if (ruleDetail104.isMessage()) {
			// 获取每个消息的长度
			int messageLength = getMessageLength(ruleDetail104);
			int messageSize = 0;
			while (messageSize < ruleDetail104.getMeasgLength()) {
				MessageInfo messageObj = new MessageInfo();
				messageObj.setMessageAddress(messageAddress);
				byte[] messageInfos = ByteUtil.getByte(bytes, mesageIndex, messageLength);
				mesageIndex += messageLength;
				messageObj.setMessageInfos(messageInfos);
				//对数据的值进行解析
				setMessageValue(ruleDetail104, messageObj);
//				if (ruleDetail104.isQualifiers()) {
//					// 判断是否有限定词
//					messageObj.setQualifiers(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[mesageIndex++]));
//				}
//				if (ruleDetail104.isTimeScaleExit()) {
//					ruleDetail104.setTimeScale(ByteUtil.byte2Hdate(ByteUtil.getByte(bytes, mesageIndex, 7)));
//				}
				messageSize++;
				messageAddress++;
				messages.add(messageObj);
			}
		} 
		ruleDetail104.setMessages(messages);
	} 
	
	
	/**
	 * 
	* @Title: setNoContinuoustMessage  
	* @Description: 设置不连续地址的消息 
	* @param ruleDetail104
	* @param bytes
	* @param index 
	* @return void   
	* @throws
	 */
	public static void setNoContinuoustMessage(MessageDetail ruleDetail104, byte[] bytes, int index) {
		List<MessageInfo> messages = new ArrayList<>();
		int mesageIndex = index;
		// 获取每个消息的长度
		int messageLength = getMessageLength(ruleDetail104);
		int messageSize = 0;
		while (messageSize < ruleDetail104.getMeasgLength()) {
			MessageInfo messageObj = new MessageInfo();
			// 消息地址
			messageObj.setMessageAddress(Iec104Util.messageAddressToInt(ByteUtil.getByte(bytes, mesageIndex, 3)));
			mesageIndex += 3;
			
			if (ruleDetail104.isMessage()) {
				// 消息集合
				byte[] messageInfos = ByteUtil.getByte(bytes, mesageIndex, messageLength);
				mesageIndex += messageLength;
				messageObj.setMessageInfos(messageInfos);
				//对数据的值进行解析
				setMessageValue(ruleDetail104, messageObj);
			} else {
				messageObj.setMessageInfos(new byte[] {});
			}
//			if (ruleDetail104.isQualifiers()) {
//				// 判断是否有限定词
//				messageObj.setQualifiers(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[mesageIndex++]));
//			}
//			if (ruleDetail104.isTimeScaleExit()) {
//				messageObj.setTimeScale(ByteUtil.byte2Hdate(ByteUtil.getByte(bytes, mesageIndex, 7)));
//			}
			messageSize++;
			messages.add(messageObj);
		}
		ruleDetail104.setMessages(messages);
	}

	/**
	 * 根据类型对数据的值进行解析
	 * */
	private static void setMessageValue(MessageDetail ruleDetail104, MessageInfo messageObj) {
		switch (ruleDetail104.getTypeIdentifier().getValue()) {
			case  0x09:
				// 测量值-归一化值(遥测)
				float f = Iec104Util.Bytes2Float_NVA(messageObj.getMessageInfos()[0],messageObj.getMessageInfos()[1]);
				messageObj.setValue(String.valueOf(f));
				break;
			case  0x0B:
				// 测量值-标度化值(遥测)
				float f1 = Iec104Util.Bytes2Float_SVA(messageObj.getMessageInfos()[0],messageObj.getMessageInfos()[1]);
				messageObj.setValue(String.valueOf(f1));
				break;
			case  0x0D:
				// 测量值-浮点数(遥测)
				byte[] valueHex = ByteUtil.getByte(messageObj.getMessageInfos(),0,4);
				String hexmsg = ByteUtil.byteArrayToHexString(ByteUtil.reByte(valueHex));
				float f2 = Iec104Util.Bytes2Float_IEEE754(hexmsg);
				messageObj.setValue(String.valueOf(f2));
				//品质描述
				byte IV = messageObj.getMessageInfos()[4];
				messageObj.setIV(Integer.valueOf(String.valueOf(IV),16));
				break;
			case  0x24:
				// 测量值-浮点数(遥测)带时标
				byte[] valueHex1 = ByteUtil.getByte(messageObj.getMessageInfos(),0,4);
				String hexmsg1 = ByteUtil.byteArrayToHexString(ByteUtil.reByte(valueHex1));
				float f3 = Iec104Util.Bytes2Float_IEEE754(hexmsg1);
				messageObj.setValue(String.valueOf(f3));
				//品质描述
				byte IV1 = messageObj.getMessageInfos()[4];
				messageObj.setIV(Integer.valueOf(String.valueOf(IV1),16));
				//解析绝对时间（7个字节时间组）
				byte[] time = ByteUtil.getByte(messageObj.getMessageInfos(), 5, 7);
				int[] ints = ByteUtil.hexStringToIntArray(ByteUtil.byteArrayToHexString(time));
				messageObj.setTime(ByteUtil.TimeScaleForSeven(ints));
				break;
			case  0x66:
				// 读单个参数
				break;
			case (byte) 0x84:
				//  读多个参数
				break;
			case  0x30:
				// 预置单个参数命令
				break;
			case (byte) 0x88:
				// 预置多个个参数
				break;
			default :
		}
	}


	/**
	 * 根据类型标识返回消息长度
	 */
	private static int getMessageLength(MessageDetail ruleDetail104) {
		return ruleDetail104.getTypeIdentifier().getMessageLength();
	}
}
