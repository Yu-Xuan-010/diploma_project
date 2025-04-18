package com.cms.reception.controller;

import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.service.UserStudyRecordService;
import com.cms.reception.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/study-records")
public class UserStudyRecordController {
    
    @Autowired
    private UserStudyRecordService studyRecordService;
    
    @GetMapping("/user/{userId}")
    public ApiResponse<List<UserStudyRecord>> getUserStudyRecords(@PathVariable Long userId) {
        List<UserStudyRecord> records = studyRecordService.getUserStudyRecords(userId);
        return ApiResponse.success(records);
    }
    
    @GetMapping("/user/{userId}/recent")
    public ApiResponse<List<UserStudyRecord>> getRecentStudyRecords(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<UserStudyRecord> records = studyRecordService.getRecentStudyRecords(userId, limit);
        return ApiResponse.success(records);
    }
    
    @GetMapping("/user/{userId}/course/{courseId}/total-time")
    public ApiResponse<Integer> getTotalStudyTime(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        Integer totalTime = studyRecordService.getTotalStudyTime(userId, courseId);
        return ApiResponse.success(totalTime);
    }
    
    @PostMapping
    public ApiResponse<Void> addStudyRecord(@RequestBody UserStudyRecord record) {
        studyRecordService.addStudyRecord(record);
        return ApiResponse.success();
    }
    
    @GetMapping("/user/{userId}/date-range")
    public ApiResponse<List<UserStudyRecord>> getStudyRecordsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<UserStudyRecord> records = studyRecordService.getStudyRecordsByDateRange(userId, startDate, endDate);
        return ApiResponse.success(records);
    }
} 