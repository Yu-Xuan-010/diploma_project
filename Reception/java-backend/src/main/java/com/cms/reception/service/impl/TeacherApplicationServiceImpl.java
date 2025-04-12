package com.cms.reception.service.impl;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.User;
import com.cms.reception.repository.TeacherApplicationRepository;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherApplicationServiceImpl implements TeacherApplicationService {

    @Autowired
    private TeacherApplicationRepository teacherApplicationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public TeacherApplication submitApplication(TeacherApplication application) {
        // 设置初始状态为待审核
        application.setStatus(TeacherApplication.ApplicationStatus.PENDING);
        return teacherApplicationRepository.save(application);
    }

    @Override
    public List<TeacherApplication> getApplicationsByUserId(Long userId) {
        return teacherApplicationRepository.findByUserId(userId);
    }

    @Override
    public List<TeacherApplication> getApplicationsByStatus(TeacherApplication.ApplicationStatus status) {
        return teacherApplicationRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public TeacherApplication reviewApplication(Long applicationId, Long reviewerId, 
                                              TeacherApplication.ApplicationStatus status, 
                                              String reviewComment) {
        TeacherApplication application = teacherApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        
        application.setStatus(status);
        application.setReviewerId(reviewerId);
        application.setReviewComment(reviewComment);
        application.setReviewedAt(LocalDateTime.now());
        
        // 如果审核通过，更新用户角色为教师
        if (status == TeacherApplication.ApplicationStatus.APPROVED) {
            User user = userRepository.findById(application.getUserId())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setRole("teacher");
            userRepository.save(user);
        }
        
        return teacherApplicationRepository.save(application);
    }

    @Override
    public List<TeacherApplication> getAllApplications() {
        return teacherApplicationRepository.findAll();
    }

    @Override
    public TeacherApplication getApplicationById(Long id) {
        return teacherApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
    }

    @Override
    public List<TeacherApplication> getPendingApplications() {
        return teacherApplicationRepository.findByStatus(TeacherApplication.ApplicationStatus.PENDING);
    }

    @Override
    public TeacherApplication getApplication(Long userId) {
        return teacherApplicationRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new RuntimeException("未找到申请记录"));
    }

    @Override
    @Transactional
    public TeacherApplication reviewApplication(Long applicationId, boolean approved, String comment, Long reviewerId) {
        TeacherApplication application = teacherApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        
        TeacherApplication.ApplicationStatus status = approved ? 
            TeacherApplication.ApplicationStatus.APPROVED : 
            TeacherApplication.ApplicationStatus.REJECTED;
            
        application.setStatus(status);
        application.setReviewerId(reviewerId);
        application.setReviewComment(comment);
        application.setReviewedAt(LocalDateTime.now());
        
        // 如果审核通过，更新用户角色为教师
        if (approved) {
            User user = userRepository.findById(application.getUserId())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setRole("teacher");
            userRepository.save(user);
        }
        
        return teacherApplicationRepository.save(application);
    }
} 