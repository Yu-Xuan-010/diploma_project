package com.cms.reception.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云配置
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {
    /** 访问密钥 */
    private String accessKey;
    
    /** 密钥 */
    private String secretKey;
    
    /** 存储空间名 */
    private String bucket;
    
    /** 访问域名 */
    private String domain;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
} 