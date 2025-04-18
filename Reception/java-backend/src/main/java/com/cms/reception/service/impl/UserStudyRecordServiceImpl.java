package com.cms.reception.service.impl;

import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.mapper.UserStudyRecordMapper;
import com.cms.reception.service.UserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserStudyRecordServiceImpl implements UserStudyRecordService {

    @Autowired
    private UserStudyRecordMapper studyRecordMapper;

    @Override
    @Transactional
    public void addStudyRecord(UserStudyRecord record) {
        // 设置初始状态为未完成
        record.setStatus(0);
        studyRecordMapper.insert(record);
        
        // 检查是否需要更新学习状态
        updateStudyStatus(record.getUserId(), record.getLessonId());
    }

    @Override
    public List<UserStudyRecord> getStudyRecordsByDateRange(Long userId, Date startDate, Date endDate) {
        return studyRecordMapper.findByDateRange(userId, startDate, endDate);
    }

    @Override
    public List<UserStudyRecord> getRecentStudyRecords(Long userId, int limit) {
        // 获取最近30天的记录
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - 30L * 24 * 60 * 60 * 1000);
        List<UserStudyRecord> records = studyRecordMapper.findByDateRange(userId, startDate, endDate);
        
        // 如果记录数超过限制，只返回最近的记录
        if (records.size() > limit) {
            return records.subList(0, limit);
        }
        return records;
    }

    @Override
    public Integer getTotalStudyTime(Long userId, Long lessonId) {
        Integer totalDuration = studyRecordMapper.getTotalDuration(userId, lessonId);
        return totalDuration != null ? totalDuration : 0;
    }

    @Override
    public boolean hasStudied(Long userId, Long lessonId) {
        UserStudyRecord latestRecord = studyRecordMapper.findLatestByLessonId(userId, lessonId);
        return latestRecord != null && latestRecord.getStatus() == 1;
    }

    @Override
    @Transactional
    public void updateStudyStatus(Long userId, Long lessonId) {
        studyRecordMapper.updateStatus(userId, lessonId);
    }
} 