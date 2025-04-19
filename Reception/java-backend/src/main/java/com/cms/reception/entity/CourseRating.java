package com.cms.reception.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_rating")
@Data
public class CourseRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private Long userId;

    private Integer rating;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String userName;
    private String userAvatar;
}
