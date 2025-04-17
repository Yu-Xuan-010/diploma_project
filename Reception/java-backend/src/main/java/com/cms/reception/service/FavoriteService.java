package com.cms.reception.service;

import com.cms.reception.entity.Favorite;
import java.util.List;

public interface FavoriteService {
    List<Favorite> getUserFavorites(Long userId);
    
    boolean isFavorite(Long userId, Long courseId);
    
    void addFavorite(Long userId, Long courseId);
    
    void removeFavorite(Long userId, Long courseId);
    
    int getCourseFavoriteCount(Long courseId);
} 