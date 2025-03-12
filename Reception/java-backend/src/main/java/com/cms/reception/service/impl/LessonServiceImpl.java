package com.cms.reception.service.impl;

import com.cms.reception.dto.LessonDTO;
import com.cms.reception.entity.Lesson;
import com.cms.reception.mapper.LessonMapper;
import com.cms.reception.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {
    
    @Resource
    private LessonMapper lessonMapper;
    
    @Override
    public List<LessonDTO> getLessonsByCourseId(Long courseId) {
        List<Lesson> lessons = lessonMapper.selectLessonsByCourseId(courseId);
        return lessons.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public LessonDTO getLessonDetail(Long id) {
        Lesson lesson = lessonMapper.selectLessonDetail(id);
        return lesson != null ? convertToDTO(lesson) : null;
    }
    
    @Override
    @Transactional
    public Lesson createLesson(Lesson lesson) {
        lessonMapper.insert(lesson);
        return lesson;
    }
    
    @Override
    @Transactional
    public Lesson updateLesson(Lesson lesson) {
        lessonMapper.updateById(lesson);
        return lesson;
    }
    
    @Override
    @Transactional
    public void deleteLesson(Long id) {
        lessonMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void updateLessonStatus(Long id, String status) {
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setStatus(status);
        lessonMapper.updateById(lesson);
    }
    
    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        BeanUtils.copyProperties(lesson, dto);
        return dto;
    }
} 