package com.cms.reception.service.impl;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.repository.TeacherApplicationRepository;
import com.cms.reception.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherApplicationServiceImpl implements TeacherApplicationService {

    @Autowired
    private TeacherApplicationRepository teacherApplicationRepository;

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
    public TeacherApplication reviewApplication(Long applicationId, Long reviewerId, 
                                              TeacherApplication.ApplicationStatus status, 
                                              String reviewComment) {
        TeacherApplication application = teacherApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        
        application.setStatus(status);
        application.setReviewerId(reviewerId);
        application.setReviewComment(reviewComment);
        application.setReviewedAt(LocalDateTime.now());
        
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
    public TeacherApplication reviewApplication(Long applicationId, boolean approved, String comment, Long reviewerId) {
        TeacherApplication application = teacherApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        
        application.setStatus(approved ? TeacherApplication.ApplicationStatus.APPROVED : TeacherApplication.ApplicationStatus.REJECTED);
        application.setReviewerId(reviewerId);
        application.setReviewComment(comment);
        application.setReviewedAt(LocalDateTime.now());
        
        return teacherApplicationRepository.save(application);
    }
} 