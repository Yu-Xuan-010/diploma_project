package com.cms.reception.service.impl;

import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.mapper.UserStudyRecordMapper;
import com.cms.reception.service.UserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

@Service
public class UserStudyRecordServiceImpl implements UserStudyRecordService {
    
    @Autowired
    private UserStudyRecordMapper studyRecordMapper;
    
    @Override
    public List<UserStudyRecord> getUserStudyRecords(Long userId) {
        return studyRecordMapper.getUserStudyRecords(userId);
    }
    
    @Override
    public List<UserStudyRecord> getRecentStudyRecords(Long userId, Integer limit) {
        return studyRecordMapper.getRecentStudyRecords(userId, limit);
    }
    
    @Override
    public Integer getTotalStudyTime(Long userId, Long courseId) {
        return studyRecordMapper.getTotalStudyTime(userId, courseId);
    }
    
    @Override
    @Transactional
    public void addStudyRecord(UserStudyRecord record) {
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        studyRecordMapper.insertStudyRecord(record);
    }
    
    @Override
    public List<UserStudyRecord> getStudyRecordsByDateRange(Long userId, Date startDate, Date endDate) {
        return studyRecordMapper.getStudyRecordsByDateRange(userId, startDate, endDate);
    }
} 