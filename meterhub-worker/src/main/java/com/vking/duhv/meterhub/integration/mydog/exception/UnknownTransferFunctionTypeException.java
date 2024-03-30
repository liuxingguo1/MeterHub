package com.vking.duhv.meterhub.integration.mydog.exception;

/**
 * @author lucan.liu
 * @date 2023-12-14 10:27
 */
public class UnknownTransferFunctionTypeException extends Exception{

    public UnknownTransferFunctionTypeException(){
        super();
    }

    @Override
    public String toString(){
        return "未定义功能类型。";
    }
}
