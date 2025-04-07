package com.cms.reception.service.impl;

import com.cms.reception.entity.Course;
import com.cms.reception.mapper.CourseMapper;
import com.cms.reception.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseMapper.searchCourses(keyword);
    }

    @Override
    public List<Course> findTop5ByPopularity() {
        return courseMapper.findTop5ByPopularity();
    }
} 