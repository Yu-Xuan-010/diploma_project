package com.cms.reception.service.impl;

import com.cms.reception.entity.Lesson;
import com.cms.reception.mapper.LessonMapper;
import com.cms.reception.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonMapper.selectByCourseId(courseId);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonMapper.selectById(id);
    }

    @Override
    @Transactional
    public Lesson createLesson(Lesson lesson) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreateTime(now);
        lesson.setUpdateTime(now);

        // 如果没有指定排序号，则设置为当前最大排序号+1
        if (lesson.getSortOrder() == null) {
            Integer maxSortOrder = lessonMapper.getMaxSortOrder(lesson.getCourseId());
            lesson.setSortOrder(maxSortOrder + 1);
        }

        lessonMapper.insert(lesson);
        return lesson;
    }

    @Override
    @Transactional
    public Lesson updateLesson(Lesson lesson) {
        // 设置更新时间
        lesson.setUpdateTime(LocalDateTime.now());
        lessonMapper.update(lesson);
        return lesson;
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
        lessonMapper.deleteById(id);
    }
} 