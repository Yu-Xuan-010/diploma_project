package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.entity.CourseComment;
import com.cms.reception.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseCommentController {
    
    @Autowired
    private CourseCommentService courseCommentService;
    
    @GetMapping("/{courseId}/comments")
    public Result<List<CourseComment>> getComments(@PathVariable Long courseId) {
        try {
            List<CourseComment> comments = courseCommentService.getCommentsByCourseId(courseId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error("获取评论失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/{courseId}/comments/check/{userId}")
    public Result<Boolean> checkUserComment(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            boolean hasCommented = courseCommentService.hasUserCommented(courseId, userId);
            return Result.success(hasCommented);
        } catch (Exception e) {
            return Result.error("检查评论状态失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/{courseId}/comments")
    public Result<CourseComment> addComment(@PathVariable Long courseId, @RequestBody CourseComment comment) {
        try {
            if (comment.getUserId() == null) {
                return Result.error("用户未登录");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                return Result.error("评论内容不能为空");
            }
            if (comment.getRating() == null || comment.getRating() < 1 || comment.getRating() > 5) {
                return Result.error("评分必须在1-5之间");
            }
            
            comment.setCourseId(courseId);
            CourseComment savedComment = courseCommentService.addComment(comment);
            return Result.success(savedComment);
        } catch (Exception e) {
            return Result.error("提交评论失败：" + e.getMessage());
        }
    }
    
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        try {
            courseCommentService.deleteComment(id);
            return Result.success("删除成功",null);
        } catch (Exception e) {
            return Result.error("删除评论失败：" + e.getMessage());
        }
    }
} 