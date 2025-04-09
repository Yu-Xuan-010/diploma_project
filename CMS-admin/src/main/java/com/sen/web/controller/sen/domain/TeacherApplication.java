package com.sen.web.controller.sen.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 教师申请实体类
 */
@Data
public class TeacherApplication {
    /**
     * 申请ID
     */
    private Long id;

    /**
     * 申请用户ID
     */
    private Long userId;

    /**
     * 申请人姓名
     */
    private String userName;

    /**
     * 申请理由
     */
    private String reason;

    /**
     * 专业领域（多个以逗号分隔）
     */
    private String expertise;

    /**
     * 教学经验
     */
    private String experience;

    /**
     * 审核状态（PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝）
     */
    private String status;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /**
     * 审核管理员ID
     */
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewedAt;
} 