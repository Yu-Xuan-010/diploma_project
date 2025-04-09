package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.TeacherApplication;

import java.util.List;

public interface TeacherApplicationService {
    
    /**
     * 查询所有教师申请
     */
    List<TeacherApplication> getAllApplications();
    
    /**
     * 根据状态查询教师申请
     */
    List<TeacherApplication> getApplicationsByStatus(String status);
    
    /**
     * 根据ID查询教师申请
     */
    TeacherApplication getApplicationById(Long id);
    
    /**
     * 审核教师申请
     */
    boolean reviewApplication(Long id, String status, Long reviewerId, String reviewComment);
} 