package com.cms.reception.controller;

import com.cms.reception.entity.College;
import com.cms.reception.service.CollegeService;
import com.cms.reception.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hibernate.Hibernate;

import java.util.List;

@RestController
@RequestMapping({"/api/colleges", "/colleges"})
@CrossOrigin(origins = "http://localhost:3000")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping
    public ResponseEntity<?> getAllColleges() {
        try {
            List<College> colleges = collegeService.findAll();
            return ResponseEntity.ok(new ApiResponse(true, "获取学院列表成功", colleges));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "获取学院列表失败: " + e.getMessage(), null));
        }
    }
} 