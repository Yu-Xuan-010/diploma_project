package com.cms.reception.service;

import com.cms.reception.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> searchCourses(String keyword);
    List<Course> findTop5ByPopularity();
} 