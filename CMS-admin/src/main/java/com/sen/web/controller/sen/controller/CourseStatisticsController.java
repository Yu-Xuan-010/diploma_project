package com.sen.web.controller.sen.controller;

import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.web.controller.sen.domain.CourseStatisticsDTO;
import com.sen.web.controller.sen.service.CourseStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.sen.web.controller.sen.controller
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-06  17:05
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@RestController
@RequestMapping("/sen/statistics")
public class CourseStatisticsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseStatisticsController.class);

    @Autowired
    private CourseStatisticsService courseStatisticsService;

    @PreAuthorize("@ss.hasPermi('system:statistics:view')")
    @GetMapping("/course")
    public AjaxResult getCourseStatistics() {
        try {
            logger.info("开始获取课程统计数据");
            List<CourseStatisticsDTO> statistics = courseStatisticsService.getCourseStatistics();
            logger.info("获取课程统计数据成功: {}", statistics);
            return AjaxResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取课程统计数据失败", e);
            return AjaxResult.error("获取课程统计数据失败: " + e.getMessage());
        }
    }

    @CacheEvict(value = "courseStatistics", allEntries = true)
    public void clearCache() {
        // 手动清理缓存
    }
}


