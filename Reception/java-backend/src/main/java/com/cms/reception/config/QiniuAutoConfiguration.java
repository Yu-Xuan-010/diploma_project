package com.cms.reception.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.cms.reception.util.QiniuUtils;

import javax.annotation.PostConstruct;

/**
 * 七牛云自动配置
 */
@Configuration
public class QiniuAutoConfiguration {
    
    @Autowired
    private QiniuConfig qiniuConfig;

    @PostConstruct
    public void init() {
        QiniuUtils.init(qiniuConfig);
    }
} 