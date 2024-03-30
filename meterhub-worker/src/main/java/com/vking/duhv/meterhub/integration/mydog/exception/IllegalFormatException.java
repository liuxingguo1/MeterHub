package com.vking.duhv.meterhub.integration.mydog.exception;


public class IllegalFormatException extends Exception {
    public IllegalFormatException() {
        super();
    }

    @Override
    public String toString() {
        return "非法报文。报文应为16进制数字组成，并且由0x68或者0x10为报文头，0x16为报文尾。";
    }
}
