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

    //General Server Error
    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),

    //Auth Error
    USER_ALREADY_EXISTS(30000, HttpStatus.CONFLICT, "User Already Exists"),
    USER_NOT_FOUND(30001, HttpStatus.BAD_REQUEST, "User Not Found"),
    PASSWORD_NOT_MATCH(30002, HttpStatus.BAD_REQUEST, "Password Not Matches"),
    TOKEN_EXPIRED(30003, HttpStatus.UNAUTHORIZED, "Token Expired"),
    INVALID_TOKEN(30004, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    FORBIDDEN(30005, HttpStatus.FORBIDDEN, "Forbidden Access"),


    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
