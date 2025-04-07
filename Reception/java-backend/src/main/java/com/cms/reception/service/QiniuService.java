package com.cms.reception.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    public String uploadFile(MultipartFile file) throws IOException {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        // 创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // 生成文件名
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());

        // 获取上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            if (response.isOK()) {
                // 返回文件访问路径
                return "http://" + domain + "/" + fileName;
            }
        } catch (QiniuException ex) {
            throw new IOException("上传文件到七牛云失败", ex);
        }

        throw new IOException("上传文件失败");
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";
        return fileName.substring(lastDotIndex);
    }
} 