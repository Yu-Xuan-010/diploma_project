package com.cms.reception.service;

import com.cms.reception.entity.UserStudyRecord;
import java.util.List;
import java.util.Date;

public interface UserStudyRecordService {
    List<UserStudyRecord> getUserStudyRecords(Long userId);
    
    List<UserStudyRecord> getRecentStudyRecords(Long userId, Integer limit);
    
    Integer getTotalStudyTime(Long userId, Long courseId);
    
    void addStudyRecord(UserStudyRecord record);
    
    List<UserStudyRecord> getStudyRecordsByDateRange(Long userId, Date startDate, Date endDate);
} 