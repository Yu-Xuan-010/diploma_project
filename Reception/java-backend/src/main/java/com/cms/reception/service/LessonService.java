package com.cms.reception.service;

import com.cms.reception.entity.Lesson;
import java.util.List;

public interface LessonService {
    /**
     * 获取课程的课时列表
     */
    List<Lesson> getLessonsByCourseId(Long courseId);

    /**
     * 获取课时详情
     */
    Lesson getLessonById(Long id);

    /**
     * 创建课时
     */
    Lesson createLesson(Lesson lesson);

    /**
     * 更新课时
     */
    Lesson updateLesson(Lesson lesson);

    /**
     * 删除课时
     */
    void deleteLesson(Long id);
} 