package com.jhkim.whiskeynote.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    OK(0, HttpStatus.OK, "OK"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    ALREADY_EXISTS_USER(10003, HttpStatus.CONFLICT, "User Already Exists"),
    USER_NOT_FOUND(10004, HttpStatus.BAD_REQUEST, "User Not Found"),
    PASSWORD_NOT_MATCH(10005, HttpStatus.BAD_REQUEST, "Password Not Matches"),
    TOKEN_EXPIRED(10006, HttpStatus.UNAUTHORIZED, "Token Expired"),
    INVALID_TOKEN(10007, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    FORBIDDEN(10008, HttpStatus.FORBIDDEN, "Forbidden Access"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
