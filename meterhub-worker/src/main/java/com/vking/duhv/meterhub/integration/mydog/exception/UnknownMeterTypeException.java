package com.vking.duhv.meterhub.integration.mydog.exception;


public class UnknownMeterTypeException extends Exception {

    public UnknownMeterTypeException() {
        super();
    }

    @Override
    public String toString() {
        return "未知类型的协议！";
    }
}
