package com.cms.reception.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

import javax.persistence.*;

@Data
@Entity
@Table(
        name = "user_study_record",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_lesson", columnNames = {"user_id", "lesson_id"})
        }
)
public class UserStudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "last_study_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastStudyTime;

    @Column(name = "total_duration", nullable = false)
    private Integer totalDuration = 0;

    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private Integer status = 0;

    private String lessonTitle;
    private String courseName;

}
