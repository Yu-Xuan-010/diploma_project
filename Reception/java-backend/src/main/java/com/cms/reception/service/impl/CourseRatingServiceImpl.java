package com.cms.reception.service.impl;

import com.cms.reception.entity.CourseRating;
import com.cms.reception.repository.CourseRatingRepository;
import com.cms.reception.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingServiceImpl implements CourseRatingService {

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Override
    public CourseRating getRating(Long courseId, Long userId) {
        return courseRatingRepository.findByCourseIdAndUserId(courseId, userId);
    }

    @Override
    public CourseRating updateRating(Long courseId, Long userId, Integer rating) {
        CourseRating cr = courseRatingRepository.findByCourseIdAndUserId(courseId, userId);
        if (cr == null) {
            cr = new CourseRating();
            cr.setCourseId(courseId);
            cr.setUserId(userId);
        }
        cr.setRating(rating);
        return courseRatingRepository.save(cr);
    }

    @Override
    public Double getAverageRating(Long courseId) {
        return courseRatingRepository.findAverageRatingByCourseId(courseId);
    }

    @Override
    public int getRatingCount(Long courseId) {
        return courseRatingRepository.countByCourseId(courseId);
    }
}
