package com.cms.reception;

import com.cms.reception.property.CorsProperties;
import com.cms.reception.property.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, CorsProperties.class})
public class ReceptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceptionApplication.class, args);
    }
} 