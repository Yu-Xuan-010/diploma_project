package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.dto.StudyRecordDTO;
import com.cms.reception.entity.User;
import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.repository.UserStudyRecordRepository;
import com.cms.reception.service.UserService;
import com.cms.reception.service.UserStudyRecordService;
import com.cms.reception.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/study")
public class UserStudyRecordController {

    @Autowired
    private UserStudyRecordService studyRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStudyRecordRepository studyRecordRepository;

    @PostMapping("/records")
    public ApiResponse<Void> addStudyRecord(@RequestBody UserStudyRecord record, Principal principal) {
        System.out.println("收到学习记录请求：" + record);
        System.out.println("当前登录用户：" + principal.getName());
        try {
            if (principal == null) {
                return ApiResponse.error("用户未登录");
            }
            // 从用户名获取用户信息
            String username = principal.getName();
            User user = userService.findByUsername(username);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            record.setUserId(user.getId());
            record.setLastStudyTime(LocalDateTime.now());
            studyRecordService.addStudyRecord(record);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("保存学习记录失败：" + e.getMessage());
        }
    }


    @GetMapping("/records")
    public ResponseEntity<List<StudyRecordDTO>> getUserRecords(@RequestParam Long userId) {
        System.out.println("Received userId: " + userId);
        List<Object[]> rawList = studyRecordRepository.findStudyRecordRaw(userId);
        List<StudyRecordDTO> result = rawList.stream().map(obj -> new StudyRecordDTO(
                ((Number) obj[0]).longValue(),       // sr.id
                ((Number) obj[1]).longValue(),       // sr.user_id
                ((Number) obj[2]).longValue(),       // sr.lesson_id
                (String) obj[3],                     // lesson_title
                ((Number) obj[4]).longValue(),       // course_id
                (String) obj[5],                     // course_name
                ((Number) obj[6]).intValue(),        // total_duration
                ((Timestamp) obj[7]).toLocalDateTime(), // last_study_time
                ((Number) obj[8]).intValue()         // status
        )).collect(Collectors.toList());

        return ResponseEntity.ok(result);
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
            @RequestParam Integer duration,
    @RequestParam(required = false, defaultValue = "0") Integer status) {
        try {
            if (!SecurityUtils.isAuthenticated()) {
                return ApiResponse.error("用户未登录");
            }
            Long userId = SecurityUtils.getCurrentUserId();
            UserStudyRecord record = new UserStudyRecord();
            record.setUserId(userId);
            record.setLessonId(lessonId);
            record.setTotalDuration(duration);
            record.setStatus(status);
            studyRecordService.addStudyRecord(record);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("更新学习进度失败：" + e.getMessage());
        }
    }
} 