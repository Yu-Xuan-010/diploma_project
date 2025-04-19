package com.cms.reception.mapper;

import com.cms.reception.entity.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRatingMapper extends JpaRepository<CourseRating, Long> {
    
    CourseRating findByCourseIdAndUserId(Long courseId, Long userId);

    @Query("SELECT AVG(r.rating) FROM CourseRating r WHERE r.courseId = :courseId")
    Double getAverageRating(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(r) FROM CourseRating r WHERE r.courseId = :courseId")
    int countByCourseId(@Param("courseId") Long courseId);
    
    List<CourseRating> findByCourseId(Long courseId);
    
    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
