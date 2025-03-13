package com.cms.reception.config;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.cms.reception.config
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-13  16:19
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // 允许跨域请求的路径
                .allowedOrigins("http://localhost:3000")  // 允许的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

