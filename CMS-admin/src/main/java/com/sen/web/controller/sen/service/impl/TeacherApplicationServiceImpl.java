package com.sen.web.controller.sen.service.impl;

import com.sen.web.controller.sen.domain.TeacherApplication;
import com.sen.web.controller.sen.mapper.TeacherApplicationMapper;
import com.sen.web.controller.sen.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherApplicationServiceImpl implements TeacherApplicationService {
    
    @Autowired
    private TeacherApplicationMapper teacherApplicationMapper;
    
    @Override
    public List<TeacherApplication> getAllApplications() {
        return teacherApplicationMapper.selectAll();
    }
    
    @Override
    public List<TeacherApplication> getApplicationsByStatus(String status) {
        return teacherApplicationMapper.selectByStatus(status);
    }
    
    @Override
    public TeacherApplication getApplicationById(Long id) {
        return teacherApplicationMapper.selectById(id);
    }
    
    @Override
    public boolean reviewApplication(Long id, String status, Long reviewerId, String reviewComment) {
        return teacherApplicationMapper.updateStatus(id, status, reviewerId, reviewComment) > 0;
    }
} 