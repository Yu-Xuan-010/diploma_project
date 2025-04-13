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

@RestController
@RequestMapping("/api/courses")  // 添加 /api 前缀
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

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
}
