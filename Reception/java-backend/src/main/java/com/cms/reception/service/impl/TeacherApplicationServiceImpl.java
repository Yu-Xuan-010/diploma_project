package com.cms.reception.service.impl;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.ApplicationStatus;
import com.cms.reception.entity.User;
import com.cms.reception.repository.TeacherApplicationRepository;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherApplicationServiceImpl implements TeacherApplicationService {

    @Autowired
    private TeacherApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public TeacherApplication submitApplication(TeacherApplicationDTO applicationDTO) {
        // 检查用户是否已经提交过申请
        if (applicationRepository.existsByUserIdAndStatus(applicationDTO.getUserId(), ApplicationStatus.PENDING)) {
            throw new IllegalStateException("您已经有一个待审核的申请");
        }

        // 检查用户是否已经是教师
        User user = userRepository.findById(applicationDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        if ("teacher".equals(user.getRole())) {
            throw new IllegalStateException("您已经是教师身份");
        }

        TeacherApplication application = new TeacherApplication();
        application.setUserId(applicationDTO.getUserId());
        application.setReason(applicationDTO.getReason());
        application.setExpertise(String.join(",", applicationDTO.getExpertise()));
        application.setExperience(applicationDTO.getExperience());
        application.setStatus(ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }

    @Override
    public TeacherApplication getApplication(Long userId) {
        return applicationRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("未找到申请记录"));
    }

    @Override
    @Transactional
    public TeacherApplication reviewApplication(Long applicationId, boolean approved, String comment, Long reviewerId) {
        TeacherApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("申请不存在"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("该申请已经被处理");
        }

        application.setStatus(approved ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
        application.setReviewComment(comment);
        application.setReviewedBy(reviewerId);

        if (approved) {
            User user = userRepository.findById(application.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
            user.setRole("teacher");
            userRepository.save(user);
        }

        return applicationRepository.save(application);
    }

    @Override
    public List<TeacherApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public List<TeacherApplication> getPendingApplications() {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("申请不存在"));
    }
} 