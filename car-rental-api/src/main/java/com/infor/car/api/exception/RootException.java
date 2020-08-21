package com.infor.car.api.exception;

public abstract class RootException extends RuntimeException{
    public static final long serialVersionID = 1L;
    private final ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public RootException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
