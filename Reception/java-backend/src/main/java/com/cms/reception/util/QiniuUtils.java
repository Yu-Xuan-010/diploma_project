package com.cms.reception.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.cms.reception.config.QiniuConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 七牛云工具类
 */
public class QiniuUtils {
    private static final Logger log = LoggerFactory.getLogger(QiniuUtils.class);

    private static QiniuConfig qiniuConfig;
    private static Auth auth;
    private static UploadManager uploadManager;

    public static void init(QiniuConfig config) {
        qiniuConfig = config;
        auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        Configuration cfg = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(cfg);
    }

    /**
     * 上传文件到七牛云
     *
     * @param file 文件
     * @return 文件访问路径
     */
    public static String upload(MultipartFile file) {
        try {
            // 生成文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + 
                    "." + getFileExtension(file.getOriginalFilename());
            
            // 获取上传凭证
            String upToken = auth.uploadToken(qiniuConfig.getBucket());
            
            // 上传文件
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            
            if (response.isOK()) {
                // 返回文件访问路径
                return qiniuConfig.getDomain() + "/" + fileName;
            } else {
                log.error("七牛云上传失败：{}", response.error);
                throw new RuntimeException("文件上传失败");
            }
        } catch (QiniuException e) {
            log.error("七牛云上传异常", e);
            throw new RuntimeException("文件上传失败");
        } catch (IOException e) {
            log.error("文件读取异常", e);
            throw new RuntimeException("文件上传失败");
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
} 