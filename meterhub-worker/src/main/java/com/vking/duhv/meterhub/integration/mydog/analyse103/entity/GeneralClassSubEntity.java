package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucan.liu
 * @date 2023-12-20 10:09
 * ASDU中的信息体(通用分类命令)
 */
@Data
public class GeneralClassSubEntity implements Serializable {

    /**
     * 通用分类标识序号(组)
     */
    private int groupNumber;

    /**
     * 通用分类标识序号(条目)
     */
    private int entryNumber;

    /**
     * 描述类别
     */
    private int dscrType;

    /**
     * 通用分类数据描述(数据类型)
     */
    private int dataType;

    /**
     * 通用分类数据描述(数据宽度)
     */
    private int dataSize;

    /**
     * 通用分类数据描述(数目)
     */
    private int number;

    /**
     * 通用分类数据描述(后续状态位)
     */
    private boolean dataCont;

    /**
     * 通用分类数据(GID)
     */
    private String gidData;




}
