package com.cms.reception.service.impl;

import com.cms.reception.entity.UserStudyRecord;
import com.cms.reception.mapper.UserStudyRecordMapper;
import com.cms.reception.repository.UserStudyRecordRepository;
import com.cms.reception.service.UserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserStudyRecordServiceImpl implements UserStudyRecordService {

    @Autowired
    private UserStudyRecordMapper studyRecordMapper;

    @Autowired
    private UserStudyRecordRepository userStudyRecordRepository;

    @Override
    @Transactional
    public void addStudyRecord(UserStudyRecord record) {
        Long userId = record.getUserId();
        Long lessonId = record.getLessonId();
        Integer newDuration = record.getTotalDuration();
        Integer newStatus = record.getStatus() == null ? 0 : record.getStatus();

        // 查询是否已有学习记录
        Optional<UserStudyRecord> optional = userStudyRecordRepository.findByUserIdAndLessonId(userId, lessonId);

        if (optional.isPresent()) {
            UserStudyRecord existing = optional.get();

            // 累加学习时长
            int updatedDuration = existing.getTotalDuration() + newDuration;
            existing.setTotalDuration(updatedDuration);

            // 更新时间
            existing.setLastStudyTime(LocalDateTime.now());

            // 如果本次状态是“完成”，则覆盖旧状态
            if (newStatus == 1 && existing.getStatus() == 0) {
                existing.setStatus(1);
            }

            userStudyRecordRepository.save(existing);
        } else {
            // 新记录
            record.setLastStudyTime(LocalDateTime.now());
            record.setStatus(newStatus);
            userStudyRecordRepository.save(record);
        }
    }


    @Override
    public List<UserStudyRecord> getStudyRecordsByDateRange(Long userId, Date startDate, Date endDate) {
        return studyRecordMapper.findByDateRangeWithJoin(userId, startDate, endDate);
    }

    @Override
    public List<UserStudyRecord> getRecentStudyRecords(Long userId, int limit) {
        // 获取最近30天的记录
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - 30L * 24 * 60 * 60 * 1000);
        List<UserStudyRecord> records = studyRecordMapper.findByDateRangeWithJoin(userId, startDate, endDate);
        
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