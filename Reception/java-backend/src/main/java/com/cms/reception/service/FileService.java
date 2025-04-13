package com.cms.reception.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * 上传文件到七牛云
     *
     * @param file 文件
     * @return 文件访问URL
     * @throws Exception 上传异常
     */
    String uploadFile(MultipartFile file) throws Exception;
} 