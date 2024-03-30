package com.vking.duhv.meterhub.integration.mydog.exception;

/**
 * @author lucan.liu
 * @date 2023-12-14 10:27
 */
public class UnknownTransferInfoNumberException extends Exception{

    public UnknownTransferInfoNumberException(){
        super();
    }

    @Override
    public String toString(){
        return "未定义的信息序号。";
    }
}
