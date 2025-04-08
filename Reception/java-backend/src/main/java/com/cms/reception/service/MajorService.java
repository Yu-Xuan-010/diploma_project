package com.cms.reception.service;

import com.cms.reception.entity.Major;
import com.cms.reception.repository.MajorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MajorService {
    private static final Logger logger = LoggerFactory.getLogger(MajorService.class);
    private final MajorRepository majorRepository;

    @Autowired
    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public Major findById(Long id) {
        return majorRepository.findById(id).orElse(null);
    }

    public List<Major> findAll() {
        try {
            logger.info("开始获取所有专业列表");
            List<Major> majors = majorRepository.findAll();
            logger.info("成功获取专业列表，共 {} 个专业", majors.size());
            return majors;
        } catch (Exception e) {
            logger.error("获取专业列表失败", e);
            throw e;
        }
    }
} 