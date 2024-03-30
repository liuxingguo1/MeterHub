package com.vking.duhv.meterhub.integration.mydog.exception;


public class UnknownTransferReasonException extends Exception {

    public UnknownTransferReasonException() {
        super();
    }

    @Override
    public String toString() {
        return "未定义传送原因。";
    }
}
