package com.sen.web.controller.sen.controller;

import com.sen.common.core.domain.AjaxResult;
import com.sen.web.controller.sen.domain.TeacherApplication;
import com.sen.web.controller.sen.service.TeacherApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师申请管理
 */
@RestController
@RequestMapping("/system/teacherApplication")
public class TeacherApplicationController {
    
    @Autowired
    private TeacherApplicationService teacherApplicationService;
    
    /**
     * 获取所有教师申请
     */
    @PreAuthorize("@ss.hasPermi('system:teacherApplication:list')")
    @GetMapping("/list")
    public AjaxResult list() {
        List<TeacherApplication> list = teacherApplicationService.getAllApplications();
        return AjaxResult.success(list);
    }
    
    /**
     * 根据状态获取教师申请
     */
    @PreAuthorize("@ss.hasPermi('system:teacherApplication:list')")
    @GetMapping("/list/{status}")
    public AjaxResult listByStatus(@PathVariable("status") String status) {
        List<TeacherApplication> list = teacherApplicationService.getApplicationsByStatus(status);
        return AjaxResult.success(list);
    }
    
    /**
     * 获取教师申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:teacherApplication:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        TeacherApplication application = teacherApplicationService.getApplicationById(id);
        return AjaxResult.success(application);
    }
    
    /**
     * 审核教师申请
     */
    @PreAuthorize("@ss.hasPermi('system:teacherApplication:review')")
    @PutMapping("/review/{id}")
    public AjaxResult review(@PathVariable("id") Long id,
                             @RequestParam("status") String status,
                             @RequestParam("reviewerId") Long reviewerId,
                             @RequestParam("reviewComment") String reviewComment) {
        if (reviewerId == null) {
            return AjaxResult.error("审核人ID不能为空");
        }
        boolean result = teacherApplicationService.reviewApplication(id, status, reviewerId, reviewComment);
        return result ? AjaxResult.success() : AjaxResult.error("审核失败");
    }

} 