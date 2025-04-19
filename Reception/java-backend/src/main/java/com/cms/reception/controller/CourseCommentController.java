package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.CourseComment;
import com.cms.reception.service.CourseCommentService;
import com.cms.reception.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CourseCommentController {
    
    private static final Logger log = LoggerFactory.getLogger(CourseCommentController.class);
    
    @Autowired
    private CourseCommentService courseCommentService;

    
    @GetMapping("/course/{courseId}")
    public Result<List<CourseComment>> getCommentsByCourseId(@PathVariable Long courseId) {
        List<CourseComment> comments = courseCommentService.getCommentsByCourseId(courseId);
        return Result.success(comments);
    }
    
    @PostMapping
    public Result<CourseComment> addComment(@RequestBody CourseComment comment) {
        CourseComment savedComment = courseCommentService.addComment(comment);
        return Result.success(savedComment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestHeader("Authorization") String token) {
        try {
            // 验证用户身份
            if (!JwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "未授权", null));
            }

            // 获取评论信息
            CourseComment comment = courseCommentService.getCommentById(id);
            if (comment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "评论不存在", null));
            }

            // 检查是否是评论作者
            if (!comment.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "只能删除自己的评论", null));
            }

            // 删除评论
            courseCommentService.deleteComment(id,userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "删除成功", null));
        } catch (Exception e) {
            log.error("删除评论失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "删除评论失败: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/course/{courseId}/count")
    public Result<Integer> getCommentCount(@PathVariable Long courseId) {
        int count = courseCommentService.getCommentCount(courseId);
        return Result.success(count);
    }
    
    @GetMapping("/course/{courseId}/user/{userId}/has-commented")
    public Result<Boolean> hasUserCommented(@PathVariable Long courseId, @PathVariable Long userId) {
        boolean hasCommented = courseCommentService.hasUserCommented(courseId, userId);
        return Result.success(hasCommented);
    }
    
    @PostMapping("/reply")
    public ResponseEntity<ApiResponse<CourseComment>> replyComment(
            @RequestBody CourseComment comment,
            @RequestHeader("Authorization") String token) {
        try {
            // 验证用户身份
            if (!JwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "未授权", null));
            }

            // 获取父评论信息
            CourseComment parentComment = courseCommentService.getCommentById(comment.getParentId());
            if (parentComment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "父评论不存在", null));
            }

            // 检查是否是评论作者
            if (!parentComment.getUserId().equals(comment.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "只能回复自己的评论", null));
            }

            // 设置回复信息
            comment.setCourseId(parentComment.getCourseId());
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());

            // 保存回复
            CourseComment savedComment = courseCommentService.addComment(comment);
            return ResponseEntity.ok(new ApiResponse<>(true, "回复成功", savedComment));
        } catch (Exception e) {
            log.error("回复评论失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "回复评论失败: " + e.getMessage(), null));
        }
    }
} 