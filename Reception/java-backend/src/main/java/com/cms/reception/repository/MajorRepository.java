package com.cms.reception.repository;

import com.cms.reception.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Logger logger = LoggerFactory.getLogger(MajorRepository.class);
} 