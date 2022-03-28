package com.jhkim.whiskeynote.api.exception;

import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            GeneralException.class
    })
    public ResponseEntity<ErrorResponse> handle(GeneralException ex){
        final ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getCode(),errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }

    //validation fail
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex){
        final ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getCode(), errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }

    @Data
    public static class ErrorResponse{
        private final Integer code;
        private final String message;
    }
}
