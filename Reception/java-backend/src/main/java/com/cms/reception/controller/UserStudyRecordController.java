package com.cms.reception.controller;


import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.service.UserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/study")
public class UserStudyRecordController {

    @Autowired
    private UserStudyRecordService studyRecordService;

    @PostMapping("/records")
    public ApiResponse<Void> addStudyRecord(@RequestBody UserStudyRecord record) {
        try {
            // 设置学习日期为当前时间
            record.setStudyDate(new Date());
            studyRecordService.addStudyRecord(record);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("保存学习记录失败：" + e.getMessage());
        }
    }

    @GetMapping("/records")
    public ApiResponse<List<UserStudyRecord>> getStudyRecords(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<UserStudyRecord> records = studyRecordService.getStudyRecordsByDateRange(userId, startDate, endDate);
        return ApiResponse.success(records);
    }

    @GetMapping("/records/recent")
    public ApiResponse<List<UserStudyRecord>> getRecentStudyRecords(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        List<UserStudyRecord> records = studyRecordService.getRecentStudyRecords(userId, limit);
        return ApiResponse.success(records);
    }

    @GetMapping("/status")
    public ApiResponse<Boolean> checkStudyStatus(
            @RequestParam Long userId,
            @RequestParam Long lessonId) {
        boolean hasStudied = studyRecordService.hasStudied(userId, lessonId);
        return ApiResponse.success(hasStudied);
    }

    @GetMapping("/total-time")
    public ApiResponse<Integer> getTotalStudyTime(
            @RequestParam Long userId,
            @RequestParam Long lessonId) {
        Integer totalTime = studyRecordService.getTotalStudyTime(userId, lessonId);
        return ApiResponse.success(totalTime);
    }

    @PostMapping("/progress")
    public ApiResponse<Void> updateStudyProgress(
            @RequestParam Long userId,
            @RequestParam Long lessonId,
            @RequestParam Integer duration) {
        try {
            UserStudyRecord record = new UserStudyRecord();
            record.setUserId(userId);
            record.setLessonId(lessonId);
            record.setDuration(duration);
            studyRecordService.addStudyRecord(record);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("更新学习进度失败：" + e.getMessage());
        }
    }
} 