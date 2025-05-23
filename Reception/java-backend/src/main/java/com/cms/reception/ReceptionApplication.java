package com.cms.reception;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @BelongsProject: CMS
 * @BelongsPackage: com.cms.reception
 * @Author: gaogao
 * @CreateTime: 2025-03-12  19:34
 * @Description: Spring Boot 启动类
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan("com.cms.reception.mapper")
@EntityScan("com.cms.reception.entity")
public class ReceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceptionApplication.class, args);
    }
}
