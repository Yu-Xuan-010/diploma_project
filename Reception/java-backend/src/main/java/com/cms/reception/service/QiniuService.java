package com.cms.reception.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    public String uploadFile(MultipartFile file) throws IOException {
        logger.info("开始上传文件到七牛云，文件名: {}", file.getOriginalFilename());
        logger.debug("七牛云配置 - accessKey: {}, bucket: {}, domain: {}", accessKey, bucket, domain);

        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        // 创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // 生成文件名
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        logger.debug("生成的文件名: {}", fileName);

        // 获取上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        logger.debug("获取上传凭证成功");

        try {
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            if (response.isOK()) {
                String fileUrl = "http://" + domain + "/" + fileName;
                logger.info("文件上传成功，访问地址: {}", fileUrl);
                return fileUrl;
            } else {
                logger.error("文件上传失败，响应: {}", response.toString());
                throw new IOException("上传文件失败: " + response.error);
            }
        } catch (QiniuException ex) {
            logger.error("七牛云上传异常", ex);
            throw new IOException("上传文件到七牛云失败: " + ex.getMessage(), ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";
        return fileName.substring(lastDotIndex);
    }
} 