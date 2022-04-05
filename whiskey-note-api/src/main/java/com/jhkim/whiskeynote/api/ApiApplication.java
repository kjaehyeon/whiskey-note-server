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
 * 현재 jwt authentication은 완료 but 유효하지 않은 토큰일때 GeneralException이 아닌 그냥 403으로 응답함
 *
 * 아직 Authority는 테스트 안해봄
 *
 * antpatterns는 좀더 찾아보기
 *
 * 테스트 코드 작성 마무리하자
 */