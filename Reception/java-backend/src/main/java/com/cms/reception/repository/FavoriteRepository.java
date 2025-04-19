package com.cms.reception.repository;

import com.cms.reception.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    
    Optional<Favorite> findByUserIdAndCourseId(Long userId, Long courseId);
    
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.userId = :userId AND f.courseId = :courseId")
    void deleteByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.courseId = :courseId")
    int countByCourseId(@Param("courseId") Long courseId);
} 