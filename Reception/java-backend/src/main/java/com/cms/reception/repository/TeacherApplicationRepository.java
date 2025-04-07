package com.cms.reception.repository;

import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TeacherApplicationRepository extends JpaRepository<TeacherApplication, Long> {
    Optional<TeacherApplication> findByUserId(Long userId);
    boolean existsByUserIdAndStatus(Long userId, ApplicationStatus status);
    List<TeacherApplication> findByStatus(ApplicationStatus status);
    List<TeacherApplication> findByReviewedBy(Long reviewerId);
} 