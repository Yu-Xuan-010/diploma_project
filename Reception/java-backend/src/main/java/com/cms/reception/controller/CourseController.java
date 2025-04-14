package com.cms.reception.controller;

import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.Course;
import com.cms.reception.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.cms.reception.entity.User;
import com.cms.reception.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/courses")  // 添加 /api 前缀
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        try {
            List<Course> courses = courseService.searchCourses(keyword);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("搜索课程时发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedCourses() {
        try {
            log.info("获取推荐课程");
            List<Course> courses = courseService.findTop5ByPopularity();
            log.info("查询到推荐课程数量：{}, 课程状态：{}", 
                courses.size(), 
                courses.stream().map(c -> c.getStatus()).collect(Collectors.toList()));
            
            // 再次确认所有课程都是审核通过的
            List<Course> approvedCourses = courses.stream()
                .filter(course -> "approved".equals(course.getStatus()))
                .collect(Collectors.toList());
            
            if (approvedCourses.size() != courses.size()) {
                log.warn("发现未审核通过的课程，已过滤");
            }
            
            return ResponseEntity.ok(new ApiResponse(true, "获取推荐课程成功", approvedCourses));
        } catch (Exception e) {
            log.error("获取推荐课程失败", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "获取推荐课程失败: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            // 设置默认值
            course.setCreatedAt(LocalDateTime.now());
            course.setUpdatedAt(LocalDateTime.now());
            course.setAverageRating(new java.math.BigDecimal("0.00"));
            course.setStudentCount(0L);
            course.setStatus("pending");

            // 验证必要字段
            if (course.getName() == null || course.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("课程名称不能为空");
            }
            if (course.getCategoryId() == null) {
                return ResponseEntity.badRequest().body("课程分类不能为空");
            }
            if (course.getTeacherId() == null) {
                return ResponseEntity.badRequest().body("教师ID不能为空");
            }

            // 保存课程
            Course savedCourse = courseService.createCourse(course);

            // 构建返回数据
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程创建成功");
            response.put("data", savedCourse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("课程创建失败：" + e.getMessage());
        }
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<?> incrementViewCount(@PathVariable Long id) {
        try {
            courseService.incrementViewCount(id);
            return ResponseEntity.ok(new ApiResponse(true, "浏览量更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "更新浏览量失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listCourses(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            log.info("获取课程列表，参数：categoryId={}, status={}, page={}, pageSize={}", 
                categoryId, status, page, pageSize);
            
            List<Course> courses = courseService.listCourses(categoryId, status, page, pageSize);
            int total = courseService.countCourses(categoryId, status);
            
            log.info("查询到课程数量：{}, 总数：{}", courses.size(), total);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", courses);
            data.put("total", total);
            
            return ResponseEntity.ok(new ApiResponse(true, "获取课程列表成功", data));
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "获取课程列表失败: " + e.getMessage()));
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<Course>> getMyCourses() {
        try {
            // 从 SecurityContext 获取当前用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            log.info("当前用户: {}", username);
            
            // 获取当前用户ID
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.error("用户不存在: {}", username);
                return ApiResponse.error("用户不存在");
            }
            log.info("用户ID: {}, 角色: {}", user.getId(), user.getRole());
            
            // 获取该教师的所有课程
            List<Course> courses = courseService.getCoursesByTeacherId(user.getId());
            log.info("获取到课程数量: {}", courses.size());
            
            // 记录每个课程的基本信息
            for (Course course : courses) {
                log.info("课程信息 - ID: {}, 名称: {}, 状态: {}", 
                    course.getId(), course.getName(), course.getStatus());
            }
            
            return ApiResponse.success(courses);
        } catch (Exception e) {
            log.error("获取教师课程列表失败", e);
            return ApiResponse.error("获取课程列表失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<?> deleteCourse(@PathVariable Long id) {
        try {
            // 从 SecurityContext 获取当前用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            // 获取当前用户ID
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }
            
            // 获取课程信息
            Course course = courseService.getCourseById(id);
            if (course == null) {
                return ApiResponse.error("课程不存在");
            }
            
            // 验证课程是否属于当前教师
            if (!course.getTeacherId().equals(user.getId())) {
                return ApiResponse.error("无权删除此课程");
            }
            
            // 删除课程
            courseService.deleteCourse(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("删除课程失败", e);
            return ApiResponse.error("删除课程失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCourseStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            String rejectReason = request.get("rejectReason");  // 获取驳回原因
            
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "状态不能为空"));
            }
            
            Course course = courseService.getCourseById(id);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            
            course.setStatus(status);
            if ("rejected".equals(status) && rejectReason != null) {
                course.setRejectReason(rejectReason);  // 设置驳回原因
            }
            
            courseService.updateCourse(course);
            return ResponseEntity.ok(new ApiResponse(true, "课程状态更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "更新课程状态失败：" + e.getMessage()));
        }
    }
}
