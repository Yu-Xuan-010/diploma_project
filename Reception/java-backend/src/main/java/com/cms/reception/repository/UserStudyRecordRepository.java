package com.cms.reception.repository;

import com.cms.reception.dto.StudyRecordDTO;
import com.cms.reception.entity.StudyRecordVO;
import com.cms.reception.entity.UserStudyRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.repository
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-04-29  10:29
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
public interface UserStudyRecordRepository  extends JpaRepository<UserStudyRecord, Integer> {
    Optional<UserStudyRecord> findByUserIdAndLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Query(value = "SELECT c.name as courseName, l.title as lessonTitle, r.last_study_time as lastStudyTime, " +
            "r.total_duration as totalDuration, l.id as lessonId, r.status as status " +
            "FROM user_study_record r " +
            "JOIN lesson l ON r.lesson_id = l.id " +
            "JOIN course c ON l.course_id = c.id " +
            "WHERE r.user_id = :userId " +
            "ORDER BY r.last_study_time DESC", nativeQuery = true)
    List<Map<String, Object>> findStudyRecordsWithCourseName(@Param("userId") Long userId);
    @Query(value = """
        SELECT 
            sr.id,
            sr.user_id,
            sr.lesson_id,
            l.title AS lesson_title,
            l.course_id,
            c.name AS course_name,
            sr.total_duration,
            sr.last_study_time,
            sr.status
        FROM 
            user_study_record sr
        JOIN 
            lesson l ON sr.lesson_id = l.id
        JOIN 
            course c ON l.course_id = c.id
        WHERE 
            sr.user_id = :userId
        ORDER BY 
            sr.last_study_time DESC
        """, nativeQuery = true)
    List<Object[]> findStudyRecordRaw(@Param("userId") Long userId);

}
