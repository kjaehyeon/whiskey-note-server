package com.jhkim.whiskeynote.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
//@PropertySource("classpath:application-jwt.properties")
public class JwtKeyConfig {
    @Value("${jwt-keys.key1}")
    private String key1;
    @Value("${jwt-keys.key2}")
    private String key2;
    @Value("${jwt-keys.key3}")
    private String key3;
}
