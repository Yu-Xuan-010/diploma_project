package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.Lesson;
import com.cms.reception.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<Lesson>> getLessonsByCourseId(@PathVariable Long courseId) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
            return ApiResponse.success(lessons);
        } catch (Exception e) {
            log.error("获取课时列表失败", e);
            return ApiResponse.error("获取课时列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Lesson> getLessonById(@PathVariable Long id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            if (lesson == null) {
                return ApiResponse.error("课时不存在");
            }
            return ApiResponse.success(lesson);
        } catch (Exception e) {
            log.error("获取课时详情失败", e);
            return ApiResponse.error("获取课时详情失败：" + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Lesson> createLesson(@RequestBody Lesson lesson) {
        try {
            Lesson createdLesson = lessonService.createLesson(lesson);
            return ApiResponse.success(createdLesson);
        } catch (Exception e) {
            log.error("创建课时失败", e);
            return ApiResponse.error("创建课时失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        try {
            lesson.setId(id);
            Lesson updatedLesson = lessonService.updateLesson(lesson);
            return ApiResponse.success(updatedLesson);
        } catch (Exception e) {
            log.error("更新课时失败", e);
            return ApiResponse.error("更新课时失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Void> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("删除课时失败", e);
            return ApiResponse.error("删除课时失败：" + e.getMessage());
        }
    }
} 