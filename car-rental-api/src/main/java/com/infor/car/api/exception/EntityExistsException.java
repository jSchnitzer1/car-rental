package com.infor.car.api.exception;

public class EntityExistsException extends RootException {
    public EntityExistsException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
