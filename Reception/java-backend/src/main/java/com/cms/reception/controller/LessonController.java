package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.dto.LessonDTO;
import com.cms.reception.entity.Lesson;
import com.cms.reception.service.LessonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    
    @Resource
    private LessonService lessonService;
    
    @GetMapping("/course/{courseId}")
    public Result<List<LessonDTO>> listByCourse(@PathVariable Long courseId) {
        return Result.success(lessonService.getLessonsByCourseId(courseId));
    }
    
    @GetMapping("/{id}")
    public Result<LessonDTO> detail(@PathVariable Long id) {
        return Result.success(lessonService.getLessonDetail(id));
    }
    
    @PostMapping
    public Result<Lesson> create(@RequestBody Lesson lesson) {
        return Result.success(lessonService.createLesson(lesson));
    }
    
    @PutMapping("/{id}")
    public Result<Lesson> update(@PathVariable Long id, @RequestBody Lesson lesson) {
        lesson.setId(id);
        return Result.success(lessonService.updateLesson(lesson));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return Result.success();
    }
    
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        lessonService.updateLessonStatus(id, status);
        return Result.success();
    }
} 