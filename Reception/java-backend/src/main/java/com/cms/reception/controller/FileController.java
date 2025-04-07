package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.service.QiniuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final QiniuService qiniuService;

    public FileController(QiniuService qiniuService) {
        this.qiniuService = qiniuService;
    }

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = qiniuService.uploadFile(file);
            return ResponseEntity.ok(new ApiResponse(true, "文件上传成功", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "文件上传失败: " + e.getMessage(), null));
        }
    }

    /**
     * 上传多个文件
     */
    @PostMapping("/uploads")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            Map<String, Object> response = new HashMap<>();
            String[] urls = new String[files.length];
            String[] fileNames = new String[files.length];
            
            for (int i = 0; i < files.length; i++) {
                urls[i] = QiniuUtils.upload(files[i]);
                fileNames[i] = files[i].getOriginalFilename();
            }
            
            response.put("urls", urls);
            response.put("fileNames", fileNames);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("文件上传失败: " + e.getMessage());
        }
    }
} 