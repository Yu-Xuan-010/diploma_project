package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.UserAnalyticsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserAnalyticsMapper {

    // 获取用户活跃数据
    @Select("SELECT date, dau, mau, new_users FROM user_activity ORDER BY date DESC LIMIT 30")
    List<UserAnalyticsDTO> getUserActivityTrend();

    // 获取课程学习数据（关联category表获取分类名称）
    @Select("SELECT " +
            "c.id as courseId, " +
            "c.name as courseName, " +
            "cat.name as category, " +
            "cps.views, " +
            "cps.students as studentCount, " +
            "cps.completion_rate as completionRate " +
            "FROM course c " +
            "LEFT JOIN category cat ON c.category_id = cat.id " +
            "LEFT JOIN course_play_statistics cps ON c.id = cps.course_id " +
            "ORDER BY cps.students DESC")
    List<UserAnalyticsDTO> getCourseStatistics();

    // 获取用户学习偏好分布（关联category表获取分类名称）
    @Select("SELECT " +
            "cat.name as preferenceCategory, " +
            "COUNT(DISTINCT ulp.user_id) as userCount, " +
            "COUNT(DISTINCT ulp.user_id) * 100.0 / (SELECT COUNT(DISTINCT user_id) FROM user_learning_preferences) as percentage " +
            "FROM user_learning_preferences ulp " +
            "LEFT JOIN category cat ON ulp.category = cat.id " +
            "GROUP BY cat.id, cat.name")
    List<UserAnalyticsDTO> getLearningPreferences();

    // 获取用户学习详情
    @Select("SELECT " +
            "u.id as userId, " +
            "u.username, " +
            "u.email, " +
            "SUM(usl.study_time) as totalStudyTime, " +
            "COUNT(DISTINCT CASE WHEN up.completed = 1 THEN up.course_id END) as completedCourses, " +
            "MAX(usl.created_at) as lastStudyTime " +
            "FROM user u " +
            "LEFT JOIN user_study_log usl ON u.id = usl.user_id " +
            "LEFT JOIN user_progress up ON u.id = up.user_id " +
            "GROUP BY u.id, u.username, u.email " +
            "ORDER BY totalStudyTime DESC")
    List<UserAnalyticsDTO> getUserLearningDetails();
}