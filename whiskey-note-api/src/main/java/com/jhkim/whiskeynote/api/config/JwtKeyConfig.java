package com.jhkim.whiskeynote.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("keys")
@RequiredArgsConstructor
public class JwtKeyConfig {
    private final String key1;
    private final String key2;
    private final String key3;
}
