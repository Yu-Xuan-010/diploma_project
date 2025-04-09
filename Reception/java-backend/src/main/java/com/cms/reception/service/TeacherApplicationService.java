package com.cms.reception.service;

import com.cms.reception.entity.TeacherApplication;
import java.util.List;

public interface TeacherApplicationService {
    TeacherApplication submitApplication(TeacherApplication application);
    List<TeacherApplication> getApplicationsByUserId(Long userId);
    List<TeacherApplication> getApplicationsByStatus(TeacherApplication.ApplicationStatus status);
    TeacherApplication reviewApplication(Long applicationId, Long reviewerId, 
                                      TeacherApplication.ApplicationStatus status, 
                                      String reviewComment);
    List<TeacherApplication> getAllApplications();
    TeacherApplication getApplicationById(Long id);
    List<TeacherApplication> getPendingApplications();
    TeacherApplication getApplication(Long userId);
    TeacherApplication reviewApplication(Long applicationId, boolean approved, String comment, Long reviewerId);
} 