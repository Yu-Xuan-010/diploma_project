package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.UserAnalyticsDTO;
import java.util.List;

public interface UserAnalyticsService {
    List<UserAnalyticsDTO> getUserActivityTrend();
    List<UserAnalyticsDTO> getCourseStatistics();
    List<UserAnalyticsDTO> getLearningPreferences();
    List<UserAnalyticsDTO> getUserLearningDetails();
}