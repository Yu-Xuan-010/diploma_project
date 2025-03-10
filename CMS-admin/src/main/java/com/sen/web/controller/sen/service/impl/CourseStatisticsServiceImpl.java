package com.sen.web.controller.sen.service.impl;

import com.sen.web.controller.sen.domain.CourseStatisticsDTO;
import com.sen.web.controller.sen.mapper.CourseStatisticsMapper;
import com.sen.web.controller.sen.service.CourseStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.sen.web.controller.sen.service.impl
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-07  09:24
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Service
public class CourseStatisticsServiceImpl extends CourseStatisticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseStatisticsServiceImpl.class);
    
    @Autowired
    private CourseStatisticsMapper courseStatisticsMapper;

    @Override
    public List<CourseStatisticsDTO> getCourseStatistics() {
        try {
            logger.info("开始获取课程统计数据");
            List<CourseStatisticsDTO> result = courseStatisticsMapper.getCourseStatistics();
            
            if (result != null && !result.isEmpty()) {
                logger.info("成功获取到 {} 条课程统计数据", result.size());
                for (CourseStatisticsDTO dto : result) {
                    logger.debug("课程统计数据: 课程={}, 总人数={}, 完成人数={}, 活跃人数={}, 平均进度={}%, 完成率={}%",
                            dto.getCourseName(),
                            dto.getTotalStudents(),
                            dto.getCompletedStudents(),
                            dto.getActiveStudents(),
                            dto.getAverageProgress(),
                            dto.getCompletionRate());
                }
            } else {
                logger.warn("未查询到课程统计数据");
            }
            
            return result;
        } catch (Exception e) {
            logger.error("获取课程统计数据时发生错误", e);
            throw e;
        }
    }
}
