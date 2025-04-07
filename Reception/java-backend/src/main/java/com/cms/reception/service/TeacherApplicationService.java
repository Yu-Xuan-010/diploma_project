package com.cms.reception.service;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import java.util.List;

public interface TeacherApplicationService {
    TeacherApplication submitApplication(TeacherApplicationDTO applicationDTO);
    TeacherApplication getApplication(Long userId);
    TeacherApplication reviewApplication(Long applicationId, boolean approved, String comment, Long reviewerId);
    List<TeacherApplication> getAllApplications();
    List<TeacherApplication> getPendingApplications();
    TeacherApplication getApplicationById(Long id);
} 