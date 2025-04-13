package com.cms.reception.controller;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private UploadManager uploadManager;
    
    @Autowired
    private Auth auth;
    
    @Value("${qiniu.bucket}")
    private String bucket;
    
    @Value("${qiniu.domain}")
    private String domain;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空");
        }

        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String key = UUID.randomUUID().toString() + extension;
            
            // 获取上传凭证
            String upToken = auth.uploadToken(bucket);
            
            // 上传文件到七牛云
            uploadManager.put(file.getBytes(), key, upToken);
            
            // 构建返回数据
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("data", "http://" + domain + "/" + key);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("文件上传失败：" + e.getMessage());
        }
    }
} 