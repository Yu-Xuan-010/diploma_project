package com.cms.reception.repository;

import com.cms.reception.entity.TeacherApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherApplicationRepository extends JpaRepository<TeacherApplication, Long> {
    List<TeacherApplication> findByUserId(Long userId);
    List<TeacherApplication> findByStatus(TeacherApplication.ApplicationStatus status);
    boolean existsByUserIdAndStatus(Long userId, TeacherApplication.ApplicationStatus status);
    Optional<TeacherApplication> findFirstByUserId(Long userId);
} 