package com.vking.duhv.meterhub.integration.mydog.analyse104.message;

import com.vking.duhv.meterhub.integration.mydog.analyse104.enums.QualifiersEnum;
import lombok.Data;

import java.util.Date;

/**
 * 报文中 的消息部分
 */
@Data
public class MessageInfo {
	/**
	 * 消息地址 字节
	 */
	private int messageAddress;
	
	/**
	 * 信息元素集合 1 2 4 个字节
	 */
	private byte[] messageInfos;

	/**
	 * 限定词
	 */
	private QualifiersEnum qualifiers;
	/**
	 * 
	 * 时标
	 */
	private  Date timeScale;

	/**
	 * 消息详情
	 */
	private int messageInfoLength;

	/**
	 * 对应的值
	 */
	private String value;

	/**
	 *
	 */
	private String time;

	/**
	 * 品质描述（IV=0,表示值有效）
	 */
	private int IV;
}
