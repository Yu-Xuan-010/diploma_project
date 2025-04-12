package com.sen.web.controller.sen.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师申请实体类
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "teacher_application")
public class TeacherApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Transient
    private String userName;

    @Column(name = "expertise", nullable = false)
    private String expertise;

    @Transient
    private String expertiseNames;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "experience")
    private String experience;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Transient
    private String reviewerName;

    @Column(name = "review_comment")
    private String reviewComment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status != null && !status.equals("PENDING") && reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }
} 