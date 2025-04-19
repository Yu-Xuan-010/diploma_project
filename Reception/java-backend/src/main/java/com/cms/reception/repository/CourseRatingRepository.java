package com.cms.reception.repository;

import com.cms.reception.entity.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.repository
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-04-19  09:30
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {
    CourseRating findByCourseIdAndUserId(Long courseId, Long userId);

    @Query("SELECT AVG(r.rating) FROM CourseRating r WHERE r.courseId = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(r) FROM CourseRating r WHERE r.courseId = :courseId")
    int countByCourseId(@Param("courseId") Long courseId);

    List<CourseRating> findByCourseId(Long courseId);

    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
