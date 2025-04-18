package com.cms.reception.service;

import com.cms.reception.entity.Favorite;
import com.cms.reception.entity.Course;
import java.util.List;

public interface FavoriteService {
    List<Favorite> getUserFavorites(Long userId);
    
    List<Course> getUserFavoriteCourses(Long userId);
    
    boolean isFavorite(Long userId, Long courseId);
    
    void addFavorite(Long userId, Long courseId);
    
    void removeFavorite(Long userId, Long courseId);
    
    int getCourseFavoriteCount(Long courseId);
} 