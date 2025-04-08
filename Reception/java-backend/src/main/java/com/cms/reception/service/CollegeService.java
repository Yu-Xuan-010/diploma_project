package com.cms.reception.service;

import com.cms.reception.entity.College;
import com.cms.reception.repository.CollegeRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CollegeService {
    private static final Logger logger = LoggerFactory.getLogger(CollegeService.class);
    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public College findById(Long id) {
        return collegeRepository.findById(id).orElse(null);
    }

    public List<College> findAll() {
        try {
            logger.info("开始获取所有学院列表");
            List<College> colleges = collegeRepository.findAll();
            logger.info("成功获取学院列表，共 {} 个学院", colleges.size());
            return colleges;
        } catch (Exception e) {
            logger.error("获取学院列表失败", e);
            throw e;
        }
    }
} 