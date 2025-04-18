package com.cms.reception.mapper;

import com.cms.reception.entity.UserStudyRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserStudyRecordMapper {
    
    @Insert("INSERT INTO user_study_record (user_id, course_id, lesson_id, duration, study_date, create_time, update_time, status) " +
            "VALUES (#{userId}, #{courseId}, #{lessonId}, #{duration}, #{studyDate}, NOW(), NOW(), #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserStudyRecord record);

    @Select("SELECT * FROM user_study_record WHERE user_id = #{userId} AND study_date BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY study_date DESC")
    List<UserStudyRecord> findByDateRange(@Param("userId") Long userId, 
                                         @Param("startDate") Date startDate, 
                                         @Param("endDate") Date endDate);

    @Select("SELECT * FROM user_study_record WHERE user_id = #{userId} AND lesson_id = #{lessonId} " +
            "ORDER BY study_date DESC LIMIT 1")
    UserStudyRecord findLatestByLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Select("SELECT SUM(duration) FROM user_study_record WHERE user_id = #{userId} AND lesson_id = #{lessonId}")
    Integer getTotalDuration(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Update("UPDATE user_study_record SET status = 1 WHERE user_id = #{userId} AND lesson_id = #{lessonId} " +
            "AND duration >= 300")
    int updateStatus(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
} 