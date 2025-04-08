package com.cms.reception.controller;

import com.cms.reception.entity.Major;
import com.cms.reception.service.MajorService;
import com.cms.reception.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hibernate.Hibernate;

import java.util.List;

@RestController
@RequestMapping({"/api/majors", "/majors"})
@CrossOrigin(origins = "http://localhost:3000")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMajors() {
        try {
            List<Major> majors = majorService.findAll();
            // 初始化 college 字段，避免延迟加载问题
            for (Major major : majors) {
                Hibernate.initialize(major.getCollege());
            }
            return ResponseEntity.ok(new ApiResponse(true, "获取专业列表成功", majors));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "获取专业列表失败: " + e.getMessage(), null));
        }
    }
} 