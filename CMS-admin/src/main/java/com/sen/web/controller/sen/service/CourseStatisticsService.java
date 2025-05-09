package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.CourseStatisticsDTO;
import com.sen.web.controller.sen.mapper.CourseStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.sen.web.controller.sen.service
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-06  17:05
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Service
public class CourseStatisticsService {

    @Autowired
    private CourseStatisticsMapper courseStatisticsMapper;

    public List<CourseStatisticsDTO> getCourseStatistics() {
        System.out.println("执行了getCourseStatistics");
        return courseStatisticsMapper.getCourseStatistics();
    }
}
