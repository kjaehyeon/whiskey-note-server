package com.jhkim.whiskeynote.api.exception;

import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

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

    //jwt token Expired or Invalid
    @ExceptionHandler(value = {
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorResponse> handle(Exception e){
        final ErrorCode errorCode = ErrorCode.TOKEN_EXPIRED;
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getCode(), errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class) // 나머지 모든 예외를 처리하는 핸들러
    public ResponseEntity<ErrorResponse> handleException(
            Exception e, HttpServletRequest request
    ){
        final ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
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
