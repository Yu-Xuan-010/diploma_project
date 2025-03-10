package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.CoursePopularityDTO;
import java.util.List;

public interface CoursePopularityService {
    List<CoursePopularityDTO> getCoursePopularityList();
    List<CoursePopularityDTO> getCoursePopularityTotal();
} 