package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.entity.Favorite;
import com.cms.reception.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @GetMapping("/{courseId}/favorite/check")
    public Result<Boolean> checkFavorite(@PathVariable Long courseId, @RequestAttribute Long userId) {
        boolean isFavorite = favoriteService.isFavorite(userId, courseId);
        return Result.success(isFavorite);
    }
    
    @PostMapping("/{courseId}/favorite")
    public Result<Void> addFavorite(@PathVariable Long courseId, @RequestAttribute Long userId) {
        favoriteService.addFavorite(userId, courseId);
        return Result.success("收藏成功",null);
    }
    
    @DeleteMapping("/{courseId}/favorite/cancel")
    public Result<Void> removeFavorite(@PathVariable Long courseId, @RequestAttribute Long userId) {
        favoriteService.removeFavorite(userId, courseId);
        return Result.success("取消收藏成功",null);
    }
    
    @GetMapping("/favorites")
    public Result<List<Favorite>> getUserFavorites(@RequestAttribute Long userId) {
        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        return Result.success(favorites);
    }
    
    @GetMapping("/{courseId}/favorite/count")
    public Result<Integer> getCourseFavoriteCount(@PathVariable Long courseId) {
        int count = favoriteService.getCourseFavoriteCount(courseId);
        return Result.success(count);
    }
} 