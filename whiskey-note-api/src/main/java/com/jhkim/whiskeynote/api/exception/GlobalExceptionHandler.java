package com.jhkim.whiskeynote.api.exception;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            GeneralException.class
    })
    public ResponseEntity<ErrorResponse> handle(
            GeneralException ex
    ){
        return makeErrorResponse(ex.getErrorCode());
    }

    //validation fail
    @ExceptionHandler(value = {
            BindException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handle(
            BindException ex
    ){
        return makeErrorResponse(ErrorCode.VALIDATION_ERROR);
    }

    //upload file size exceeded
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handle(
            MaxUploadSizeExceededException ex
    ){
        return makeErrorResponse(ErrorCode.FILE_UPLOAD_SIZE_EXCEEDED);
    }

    //AWS S3 Error
    @ExceptionHandler(value = {
            SdkClientException.class,
            AmazonServiceException.class
    })
    public ResponseEntity<ErrorResponse> handle(
            Exception ex
    ){
        return makeErrorResponse(ErrorCode.AWS_S3_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingQueryParam(
            Exception e
    ){
        return makeErrorResponse(ErrorCode.MISSING_QUERY_PARAM);
    }

    @ExceptionHandler(Exception.class) // 나머지 모든 예외를 처리하는 핸들러
    public ResponseEntity<ErrorResponse> handleException(
            Exception e
    ){
        e.printStackTrace();
        return new ResponseEntity<>(
                new ErrorResponse(
                        ErrorCode.INTERNAL_ERROR.getCode(),
                        e.getMessage(),
                        LocalDateTime.now()
                ),
                ErrorCode.INTERNAL_ERROR.getHttpStatus()
        );
    }

    private ResponseEntity<ErrorResponse> makeErrorResponse(
            ErrorCode errorCode
    ){
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), LocalDateTime.now()),
                errorCode.getHttpStatus()
        );
    }
    @Data
    private static class ErrorResponse{
        private final Integer code;
        private final String message;
        private final LocalDateTime timestamp;
    }
}
