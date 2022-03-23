package com.jhkim.whiskeynote.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.jhkim.whiskeynote.core"})
@EnableJpaRepositories(basePackages = {"com.jhkim.whiskeynote.core"})
@SpringBootApplication(scanBasePackages = "com.jhkim.whiskeynote")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
