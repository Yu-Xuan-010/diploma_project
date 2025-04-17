package com.cms.reception.service.impl;

import com.cms.reception.entity.Favorite;
import com.cms.reception.mapper.FavoriteMapper;
import com.cms.reception.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteMapper.selectByUserId(userId);
    }
    
    @Override
    public boolean isFavorite(Long userId, Long courseId) {
        return favoriteMapper.selectByUserIdAndCourseId(userId, courseId) != null;
    }
    
    @Override
    @Transactional
    public void addFavorite(Long userId, Long courseId) {
        if (!isFavorite(userId, courseId)) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setCourseId(courseId);
            favoriteMapper.insert(favorite);
        }
    }
    
    @Override
    @Transactional
    public void removeFavorite(Long userId, Long courseId) {
        favoriteMapper.deleteByUserIdAndCourseId(userId, courseId);
    }
    
    @Override
    public int getCourseFavoriteCount(Long courseId) {
        return favoriteMapper.countByCourseId(courseId);
    }
} 