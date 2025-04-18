package com.cms.reception.mapper;

import com.cms.reception.entity.UserStudyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface UserStudyRecordMapper {
    List<UserStudyRecord> getUserStudyRecords(@Param("userId") Long userId);
    
    List<UserStudyRecord> getRecentStudyRecords(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    Integer getTotalStudyTime(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    void insertStudyRecord(UserStudyRecord record);
    
    List<UserStudyRecord> getStudyRecordsByDateRange(
        @Param("userId") Long userId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );
} 