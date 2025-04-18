package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.service.UserStudyRecordService;
import com.cms.reception.utils.SecurityUtils;
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
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            // 设置用户ID为当前登录用户
            record.setUserId(SecurityUtils.getCurrentUserId());
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
            if (startDate == null) {
                startDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000); // 默认30天前
            }
            if (endDate == null) {
                endDate = new Date(); // 默认当前时间
            }
            List<UserStudyRecord> records = studyRecordService.getStudyRecordsByDateRange(userId, startDate, endDate);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error("获取学习记录失败：" + e.getMessage());
        }
    }

    @GetMapping("/records/recent")
    public ApiResponse<List<UserStudyRecord>> getRecentStudyRecords(
            @RequestParam(defaultValue = "5") int limit) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
            List<UserStudyRecord> records = studyRecordService.getRecentStudyRecords(userId, limit);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error("获取最近学习记录失败：" + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ApiResponse<Boolean> checkStudyStatus(@RequestParam Long lessonId) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
            boolean hasStudied = studyRecordService.hasStudied(userId, lessonId);
            return ApiResponse.success(hasStudied);
        } catch (Exception e) {
            return ApiResponse.error("检查学习状态失败：" + e.getMessage());
        }
    }

    @GetMapping("/total-time")
    public ApiResponse<Integer> getTotalStudyTime(@RequestParam Long lessonId) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
            Integer totalTime = studyRecordService.getTotalStudyTime(userId, lessonId);
            return ApiResponse.success(totalTime);
        } catch (Exception e) {
            return ApiResponse.error("获取学习时长失败：" + e.getMessage());
        }
    }

    @PostMapping("/progress")
    public ApiResponse<Void> updateStudyProgress(
            @RequestParam Long lessonId,
            @RequestParam Integer duration) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
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