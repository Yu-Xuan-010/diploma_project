package com.cms.reception.service.impl;

import com.cms.reception.entity.Favorite;
import com.cms.reception.entity.Course;
import com.cms.reception.repository.FavoriteRepository;
import com.cms.reception.service.FavoriteService;
import com.cms.reception.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    private static final Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private CourseService courseService;
    
    @Override
    public List<Course> getUserFavoriteCourses(Long userId) {
        try {
            List<Favorite> favorites = favoriteRepository.findByUserId(userId);
            return favorites.stream()
                .map(favorite -> courseService.getCourseById(favorite.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting user favorite courses for userId: {}", userId, e);
            return List.of();
        }
    }
    
    @Override
    public List<Favorite> getUserFavorites(Long userId) {
        try {
            return favoriteRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error getting user favorites for userId: {}", userId, e);
            return List.of();
        }
    }
    
    @Override
    public boolean isFavorite(Long userId, Long courseId) {
        try {
            return favoriteRepository.findByUserIdAndCourseId(userId, courseId).isPresent();
        } catch (Exception e) {
            logger.error("Error checking favorite status for userId: {} and courseId: {}", userId, courseId, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public void addFavorite(Long userId, Long courseId) {
        try {
            if (!isFavorite(userId, courseId)) {
                Favorite favorite = new Favorite();
                favorite.setUserId(userId);
                favorite.setCourseId(courseId);
                favoriteRepository.save(favorite);
                logger.info("Added favorite for userId: {} and courseId: {}", userId, courseId);
            }
        } catch (Exception e) {
            logger.error("Error adding favorite for userId: {} and courseId: {}", userId, courseId, e);
            throw e;
        }
    }
    
    @Override
    @Transactional
    public void removeFavorite(Long userId, Long courseId) {
        try {
            favoriteRepository.deleteByUserIdAndCourseId(userId, courseId);
            logger.info("Removed favorite for userId: {} and courseId: {}", userId, courseId);
        } catch (Exception e) {
            logger.error("Error removing favorite for userId: {} and courseId: {}", userId, courseId, e);
            throw e;
        }
    }
    
    @Override
    public int getCourseFavoriteCount(Long courseId) {
        try {
            return favoriteRepository.countByCourseId(courseId);
        } catch (Exception e) {
            logger.error("Error getting favorite count for courseId: {}", courseId, e);
            return 0;
        }
    }
} 