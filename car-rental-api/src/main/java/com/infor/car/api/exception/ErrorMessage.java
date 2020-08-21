package com.infor.car.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ErrorMessage {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private ErrorCode errorCode;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, ErrorCode errorCode) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errorCode = errorCode;
    }
}
