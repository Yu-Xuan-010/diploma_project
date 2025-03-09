package com.ruoyi.web.controller.sen.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.sen.domain.CoursePopularityDTO;
import com.ruoyi.web.controller.sen.service.CoursePopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sen/popularity")
public class CoursePopularityController extends BaseController {

    @Autowired
    private CoursePopularityService coursePopularityService;

    @PreAuthorize("@ss.hasPermi('system:statistics:view')")
    @GetMapping("/list")
    public AjaxResult getCoursePopularity() {
        try {
            List<CoursePopularityDTO> trendList = coursePopularityService.getCoursePopularityList();
            List<CoursePopularityDTO> totalList = coursePopularityService.getCoursePopularityTotal();
            
            Map<String, Object> result = new HashMap<>();
            result.put("trendList", trendList);
            result.put("totalList", totalList);
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取课程热度数据失败: " + e.getMessage());
        }
    }
} 