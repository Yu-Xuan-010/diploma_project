package com.ruoyi.web.controller.sen.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.sen.service.UserAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userManage/userlist/analytics")
public class UserAnalyticsController extends BaseController {

    @Autowired
    private UserAnalyticsService userAnalyticsService;

    @PreAuthorize("@ss.hasPermi('system:user:analytics')")
    @GetMapping("/data")
    public AjaxResult getAnalyticsData() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("activityTrend", userAnalyticsService.getUserActivityTrend());
            data.put("courseStats", userAnalyticsService.getCourseStatistics());
            data.put("preferences", userAnalyticsService.getLearningPreferences());
            data.put("userDetails", userAnalyticsService.getUserLearningDetails());
            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error("获取用户数据分析失败: " + e.getMessage());
        }
    }
}