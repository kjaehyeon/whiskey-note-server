package com.jhkim.whiskeynote.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.jhkim.whiskeynote.core"})
@EnableJpaRepositories(basePackages = {"com.jhkim.whiskeynote.core"})
@SpringBootApplication(scanBasePackages = "com.jhkim.whiskeynote")
@ConfigurationPropertiesScan
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

/**
 *
 * antpatterns는 좀더 찾아보기
 * 테스트 코드 작성 마무리하자
 * 권한 없을 때 GeneralException 아니고 그냥 403던짐
 */