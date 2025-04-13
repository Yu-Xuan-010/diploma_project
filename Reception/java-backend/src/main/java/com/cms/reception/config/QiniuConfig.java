package com.cms.reception.config;

import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置
 */
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {
    
    @Value("${qiniu.accessKey}")
    private String accessKey;
    
    @Value("${qiniu.secretKey}")
    private String secretKey;
    
    @Value("${qiniu.bucket}")
    private String bucket;
    
    @Value("${qiniu.domain}")
    private String domain;
    
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }
    
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(new com.qiniu.storage.Configuration(Region.autoRegion()));
    }
    
    @Bean
    public String uploadToken() {
        return auth().uploadToken(bucket);
    }
    // Getter 和 Setter
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