package com.cms.reception.service;

import com.cms.reception.dto.LessonDTO;
import com.cms.reception.entity.Lesson;
import java.util.List;

public interface LessonService {
    List<LessonDTO> getLessonsByCourseId(Long courseId);
    
    LessonDTO getLessonDetail(Long id);
    
    Lesson createLesson(Lesson lesson);
    
    Lesson updateLesson(Lesson lesson);
    
    void deleteLesson(Long id);
    
    void updateLessonStatus(Long id, String status);
} 