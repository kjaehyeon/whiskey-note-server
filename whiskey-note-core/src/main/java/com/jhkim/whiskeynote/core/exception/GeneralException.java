package com.jhkim.whiskeynote.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorCode errorCode;
}
