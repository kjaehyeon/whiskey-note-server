package com.jhkim.whiskeynote.api.exception;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(
            MethodArgumentNotValidException ex
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

    @ExceptionHandler(Exception.class) // 나머지 모든 예외를 처리하는 핸들러
    public ResponseEntity<ErrorResponse> handleException(
            Exception e
    ){
       return makeErrorResponse(ErrorCode.INTERNAL_ERROR);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponse(
            ErrorCode errorCode
    ){
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getCode(), errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }
    @Data
    private static class ErrorResponse{
        private final Integer code;
        private final String message;
    }
}
