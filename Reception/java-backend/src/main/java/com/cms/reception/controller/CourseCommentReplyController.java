package com.cms.reception.controller;

import com.cms.reception.entity.CourseCommentReply;
import com.cms.reception.service.CourseCommentReplyService;
import com.cms.reception.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course/comment/reply")
public class CourseCommentReplyController {

    @Autowired
    private CourseCommentReplyService courseCommentReplyService;

    @GetMapping("/{commentId}")
    public ApiResponse getRepliesByCommentId(@PathVariable Long commentId) {
        List<CourseCommentReply> replies = courseCommentReplyService.getRepliesByCommentId(commentId);
        return ApiResponse.success(replies);
    }

    @PostMapping
    public ApiResponse addReply(@RequestBody CourseCommentReply reply) {
        CourseCommentReply savedReply = courseCommentReplyService.addReply(reply);
        return ApiResponse.success(savedReply);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteReply(@PathVariable Long id) {
        courseCommentReplyService.deleteReply(id);
        return ApiResponse.success();
    }

    @GetMapping("/detail/{id}")
    public ApiResponse getReplyById(@PathVariable Long id) {
        CourseCommentReply reply = courseCommentReplyService.getReplyById(id);
        return ApiResponse.success(reply);
    }


} 