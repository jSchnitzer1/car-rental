package com.infor.car.api.exception;

public class LongPeriodException extends RootException {
    public LongPeriodException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
