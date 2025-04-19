package com.cms.reception.controller;

import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher-applications")
public class TeacherApplicationController {

    @Autowired
    private TeacherApplicationService teacherApplicationService;

    @PostMapping
    public ResponseEntity<?> submitApplication(@RequestBody TeacherApplication application) {
        try {
            TeacherApplication savedApplication = teacherApplicationService.submitApplication(application);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "申请提交成功");
            response.put("data", savedApplication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("false", false);
            response.put("message", "申请提交失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getApplicationsByUserId(@PathVariable Long userId) {
        try {
            List<TeacherApplication> applications = teacherApplicationService.getApplicationsByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", applications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取申请失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getApplicationsByStatus(@PathVariable TeacherApplication.ApplicationStatus status) {
        try {
            List<TeacherApplication> applications = teacherApplicationService.getApplicationsByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", applications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取申请失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<?> reviewApplication(
            @PathVariable Long id,
            @RequestParam Long reviewerId,
            @RequestParam TeacherApplication.ApplicationStatus status,
            @RequestParam(required = false) String reviewComment) {
        try {
            TeacherApplication application = teacherApplicationService.reviewApplication(id, reviewerId, status, reviewComment);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "审核完成");
            response.put("data", application);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "审核失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 