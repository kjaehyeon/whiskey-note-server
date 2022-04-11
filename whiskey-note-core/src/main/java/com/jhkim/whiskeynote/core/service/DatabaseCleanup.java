package com.jhkim.whiskeynote.core.service;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("test")
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute(){
        //쓰기 지연 저장소에 남은 SQL 마저 수행
        entityManager.flush();
        //TRUNCATE하기 위해 참조 무결성 해제
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames){
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();

            //ID값을 다시 1부터 시작하도록 기본값 초기화
            entityManager.createNativeQuery("ALTER TABLE " + tableName
                    + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

    }
}
