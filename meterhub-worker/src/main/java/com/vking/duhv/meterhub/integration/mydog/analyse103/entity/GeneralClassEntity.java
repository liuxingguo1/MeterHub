package com.vking.duhv.meterhub.integration.mydog.analyse103.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lucan.liu
 * @date 2023-12-20 10:09
 * ASDU中的信息体(通用分类命令)
 */
@Data
public class GeneralClassEntity extends ASDUBaseEntity {

    /**
     * 类型标识
     */
    private int type;

    /**
     * 返回信息标识符
     */
    private int returnFlag;

   /**
     * 通用分类数据集数目
     */
    private int no;

    /**
     * 计数器位
     * 具有相同返回信息标识符(RII)的应用服务数据单元的一位计数器位
     */
    private boolean count;

    /**
     * 后续状态位
     * 0 后面未跟着具有相同返回信息标识符(RII)的应用服务数据单元
     * 1 后面跟着具有相同返回信息标识符(RII)的应用服务数据单元
     */
    private boolean cont;

    /**
     * 通用分类标识序号(组)
     */
    private int groupNumber;

    /**
     * 通用分类标识序号(条目)
     */
    private int entryNumber;


    /**
     * 数据集
     */
   private List<GeneralClassSubEntity> generalClassSubEntityList;


}
