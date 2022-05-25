package com.jhkim.whiskeynote.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    OK(0, HttpStatus.OK, "OK"),

    //General Client Error
    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
    RESOURCE_NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    RESOURCE_ALREADY_EXISTS(10003, HttpStatus.CONFLICT, "Requested resource is already exists"),
    FILE_UPLOAD_SIZE_EXCEEDED(10004, HttpStatus.BAD_REQUEST, "File upload size exceeded"),
    INVALID_FILE_FORMAT(10005, HttpStatus.BAD_REQUEST, "Invalid file format"),
    ENUM_TYPE_MISMATCH(10006, HttpStatus.BAD_REQUEST, "Enum type mismatch"),

    //General Server Error
    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    FILE_UPLOAD_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "File upload error"),

    //Auth Error
    USER_ALREADY_EXISTS(30000, HttpStatus.CONFLICT, "User Already Exists"),
    USER_NOT_FOUND(30001, HttpStatus.BAD_REQUEST, "User Not Found"),
    PASSWORD_NOT_MATCH(30002, HttpStatus.BAD_REQUEST, "Password Not Matches"),
    TOKEN_EXPIRED(30003, HttpStatus.UNAUTHORIZED, "Token Expired"),
    INVALID_TOKEN(30004, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    UNAUTHENTICATED(30005, HttpStatus.UNAUTHORIZED, "Unauthenticated User"),
    FORBIDDEN(30006, HttpStatus.FORBIDDEN, "Forbidden Access"),

    //External Service Error
    AWS_S3_ERROR(40000, HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 Error")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
