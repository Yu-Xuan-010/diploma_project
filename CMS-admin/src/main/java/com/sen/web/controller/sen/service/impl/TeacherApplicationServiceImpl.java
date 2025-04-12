package com.sen.web.controller.sen.service.impl;

import com.sen.web.controller.sen.domain.TeacherApplication;
import com.sen.web.controller.sen.mapper.TeacherApplicationMapper;
import com.sen.web.controller.sen.mapper.UserListMapper;
import com.sen.web.controller.sen.service.TeacherApplicationService;
import com.sen.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherApplicationServiceImpl implements TeacherApplicationService {
    
    @Autowired
    private TeacherApplicationMapper teacherApplicationMapper;
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private UserListMapper userListMapper;
    
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
    @Transactional
    public boolean reviewApplication(Long id, String status, Long reviewerId, String reviewComment) {
        TeacherApplication application = teacherApplicationMapper.selectById(id);
        if (application == null) {
            return false;
        }
        
        // 查询审核人的姓名
        String reviewerName = userMapper.getUserNameById(reviewerId);
        if (reviewerName == null) {
            return false;  // 避免 reviewerId 无效导致错误
        }
        
        // 更新申请状态，并存入审核人姓名
        int result = teacherApplicationMapper.updateStatus(id, status, reviewerId, reviewerName, reviewComment);
        
        // 如果审核通过，更新用户角色为教师
        if (result > 0 && "APPROVED".equals(status)) {
            userListMapper.updateUserRole(application.getUserId(), "teacher");
            // 返回更新后的用户信息
            return true;
        }
        
        return result > 0;
    }
} 