package com.vking.duhv.meterhub.integration.mydog.exception;


public class UnknownTypeIdentifierException extends Exception {
    public UnknownTypeIdentifierException() {
        super();
    }

    @Override
    public String toString() {
        return "未知类型标识符";
    }
}
