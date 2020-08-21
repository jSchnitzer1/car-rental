package com.infor.car.api.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<?> handleEntityExistsException(EntityExistsException exception) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, exception.getErrorCode()),
                new HttpHeaders(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, exception.getErrorCode()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, exception.getErrorCode()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleGlobalException(Exception exception) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.RUNTIME_ERROR),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessageDesc = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.toString();
        return new ResponseEntity<>(new ErrorMessage(errorMessageDesc, ErrorCode.INVALID_ARGUMENT),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
}
