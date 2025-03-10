package com.sen.web.controller.sen.service.impl;

import com.sen.web.controller.sen.domain.CoursePopularityDTO;
import com.sen.web.controller.sen.mapper.CoursePopularityMapper;
import com.sen.web.controller.sen.service.CoursePopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePopularityServiceImpl implements CoursePopularityService {

    @Autowired
    private CoursePopularityMapper coursePopularityMapper;

    @Override
    public List<CoursePopularityDTO> getCoursePopularityList() {
        return coursePopularityMapper.getCoursePopularityList();
    }

    @Override
    public List<CoursePopularityDTO> getCoursePopularityTotal() {
        return coursePopularityMapper.getCoursePopularityTotal();
    }
} 