package com.sen.web.controller.sen.service.impl;

import com.sen.web.controller.sen.domain.UserAnalyticsDTO;
import com.sen.web.controller.sen.mapper.UserAnalyticsMapper;
import com.sen.web.controller.sen.service.UserAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnalyticsServiceImpl implements UserAnalyticsService {

    @Autowired
    private UserAnalyticsMapper userAnalyticsMapper;

    @Override
    public List<UserAnalyticsDTO> getUserActivityTrend() {
        return userAnalyticsMapper.getUserActivityTrend();
    }

    @Override
    public List<UserAnalyticsDTO> getCourseStatistics() {
        return userAnalyticsMapper.getCourseStatistics();
    }

    @Override
    public List<UserAnalyticsDTO> getLearningPreferences() {
        return userAnalyticsMapper.getLearningPreferences();
    }

    @Override
    public List<UserAnalyticsDTO> getUserLearningDetails() {
        return userAnalyticsMapper.getUserLearningDetails();
    }
}