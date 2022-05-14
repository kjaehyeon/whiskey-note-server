package com.jhkim.whiskeynote.api.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {
    public static final int EXPIRATION_TIME = 6000000; // 100분
    public static final String KEY_NAME = "Jwt-Token";
}
