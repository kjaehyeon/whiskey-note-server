package com.jhkim.whiskeynote.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:jwt-secret-keys.properties")
public class JwtKeyConfig {
    @Value("${keys.key1}")
    private String key1;
    @Value("${keys.key2}")
    private String key2;
    @Value("${keys.key3}")
    private String key3;
}
