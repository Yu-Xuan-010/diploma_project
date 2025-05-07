package com.cms.reception.mapper;

import com.cms.reception.entity.UserStudyRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserStudyRecordMapper {

    @Insert("INSERT INTO user_study_record (user_id, lesson_id, total_duration,last_study_time , create_time, status) " +
            "VALUES (#{userId}, #{courseId}, #{lessonId}, #{duration}, #{studyDate}, NOW(), NOW(), #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserStudyRecord record);

    @Select("""
    SELECT 
        r.*, 
        l.title AS lessonTitle,
        c.name AS courseName
    FROM user_study_record r
    LEFT JOIN lesson l ON r.lesson_id = l.id
    LEFT JOIN course c ON l.course_id = c.id
    WHERE r.user_id = #{userId}
      AND r.last_study_time BETWEEN #{startDate} AND #{endDate}
    ORDER BY r.last_study_time DESC
""")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "lesson_id", property = "lessonId"),
            @Result(column = "last_study_time", property = "lastStudyTime"),
            @Result(column = "total_duration", property = "totalDuration"),
            @Result(column = "lessonTitle", property = "lessonTitle"),
            @Result(column = "courseName", property = "courseName")
    })
    List<UserStudyRecord> findByDateRangeWithJoin(@Param("userId") Long userId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);


    @Select("SELECT * FROM user_study_record WHERE user_id = #{userId} AND lesson_id = #{lessonId} " +
            "ORDER BY last_study_time DESC LIMIT 1")
    UserStudyRecord findLatestByLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Select("SELECT SUM(total_duration) FROM user_study_record WHERE user_id = #{userId} AND lesson_id = #{lessonId}")
    Integer getTotalDuration(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Update("UPDATE user_study_record SET status = 1 WHERE user_id = #{userId} AND lesson_id = #{lessonId} " +
            "AND user_study_record.total_duration >= 300")
    int updateStatus(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
} 