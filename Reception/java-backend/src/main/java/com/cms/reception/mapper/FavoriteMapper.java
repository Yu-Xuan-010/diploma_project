package com.cms.reception.mapper;

import com.cms.reception.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    List<Favorite> selectByUserId(@Param("userId") Long userId);
    
    Favorite selectByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    int insert(Favorite favorite);
    
    int deleteById(@Param("id") Long id);
    
    int deleteByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    int countByCourseId(@Param("courseId") Long courseId);
} 