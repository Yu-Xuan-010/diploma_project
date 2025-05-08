package com.cms.reception.service;

import com.cms.reception.entity.UserStudyRecord;
import java.util.Date;
import java.util.List;

public interface UserStudyRecordService {
    
    /**
     * 添加学习记录
     */
    void addStudyRecord(UserStudyRecord record);

    /**
     * 获取指定日期范围内的学习记录
     */
    List<UserStudyRecord> getStudyRecordsByDateRange(Long userId, Date startDate, Date endDate);

    /**
     * 获取最近的学习记录
     */
    List<UserStudyRecord> getRecentStudyRecords(Long userId, int limit);

    /**
     * 获取总学习时长
     */
    Integer getTotalStudyTime(Long userId, Long lessonId);

    /**
     * 检查是否已学习
     */
    boolean hasStudied(Long userId, Long lessonId);

    /**
     * 更新学习状态
     */
    void updateStudyStatus(Long userId, Long lessonId);

    UserStudyRecord findRecordByLessonId(Long userId,Long lessonId);
}