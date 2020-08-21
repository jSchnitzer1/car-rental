package com.infor.car.api.exception;

public class BadRequestException extends RootException {
    public BadRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
