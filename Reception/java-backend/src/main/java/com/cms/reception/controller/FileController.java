package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.service.QiniuService;
import com.cms.reception.util.QiniuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final QiniuService qiniuService;

    public FileController(QiniuService qiniuService) {
        this.qiniuService = qiniuService;
    }

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("接收到文件上传请求，文件名: {}", file.getOriginalFilename());
        
        if (file.isEmpty()) {
            logger.warn("上传的文件为空");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "文件不能为空", null));
        }

        try {
            String fileUrl = qiniuService.uploadFile(file);
            logger.info("文件上传成功，URL: {}", fileUrl);
            return ResponseEntity.ok(new ApiResponse(true, "文件上传成功", fileUrl));
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "文件上传失败: " + e.getMessage(), null));
        }
    }

    /**
     * 上传多个文件
     */
    @PostMapping("/uploads")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        logger.info("接收到多文件上传请求，文件数量: {}", files.length);
        
        if (files == null || files.length == 0) {
            logger.warn("上传的文件数组为空");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "文件不能为空", null));
        }

        try {
            Map<String, Object> response = new HashMap<>();
            String[] urls = new String[files.length];
            String[] fileNames = new String[files.length];
            
            for (int i = 0; i < files.length; i++) {
                urls[i] = QiniuUtils.upload(files[i]);
                fileNames[i] = files[i].getOriginalFilename();
                logger.info("文件 {} 上传成功，URL: {}", fileNames[i], urls[i]);
            }
            
            response.put("urls", urls);
            response.put("fileNames", fileNames);
            return ResponseEntity.ok(new ApiResponse(true, "文件上传成功", response));
        } catch (Exception e) {
            logger.error("多文件上传失败", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "文件上传失败: " + e.getMessage(), null));
        }
    }
} 