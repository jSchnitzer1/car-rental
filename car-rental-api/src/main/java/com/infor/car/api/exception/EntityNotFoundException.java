package com.infor.car.api.exception;

public class EntityNotFoundException extends RootException {
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
