package com.cms.reception.controller;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/teacher-applications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeacherApplicationController {

    @Autowired
    private TeacherApplicationService teacherApplicationService;

    @GetMapping
    public ResponseEntity<List<TeacherApplication>> getAllApplications() {
        return ResponseEntity.ok(teacherApplicationService.getAllApplications());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TeacherApplication>> getPendingApplications() {
        return ResponseEntity.ok(teacherApplicationService.getPendingApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherApplication> getApplication(@PathVariable Long id) {
        return ResponseEntity.ok(teacherApplicationService.getApplicationById(id));
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<TeacherApplication> reviewApplication(
            @PathVariable Long id,
            @RequestParam boolean approved,
            @RequestParam String comment,
            @RequestParam Long reviewerId) {
        return ResponseEntity.ok(teacherApplicationService.reviewApplication(id, approved, comment, reviewerId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<TeacherApplication> getUserApplication(@PathVariable Long userId) {
        return ResponseEntity.ok(teacherApplicationService.getApplication(userId));
    }
} 