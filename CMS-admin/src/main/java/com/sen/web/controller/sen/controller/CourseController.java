package com.sen.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.sen.web.controller.sen.domain.Course;
import com.sen.web.controller.sen.service.ICourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sen.common.annotation.Log;
import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.common.enums.BusinessType;
import com.sen.common.utils.poi.ExcelUtil;
import com.sen.common.core.page.TableDataInfo;

/**
 * 课程列表Controller
 * 
 * @author sen
 * @date 2025-03-05
 */
@CrossOrigin
@RestController
@RequestMapping("/system/course")
public class CourseController extends BaseController
{
    @Autowired
    private ICourseService courseService;

    /**
     * 查询课程列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(Course course)
    {
        startPage();
        List<Course> list = courseService.selectCourseList(course);
        return getDataTable(list);
    }

    /**
     * 导出课程列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:export')")
    @Log(title = "课程列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Course course)
    {
        List<Course> list = courseService.selectCourseList(course);
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.exportExcel(response, list, "课程列表数据");
    }

    /**
     * 获取课程列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:course:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(courseService.selectCourseById(id));
    }

    /**
     * 新增课程列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:add')")
    @Log(title = "课程列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Course course)
    {
        return toAjax(courseService.insertCourse(course));
    }

    /**
     * 修改课程列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:edit')")
    @Log(title = "课程列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Course course)
    {
        return toAjax(courseService.updateCourse(course));
    }

    /**
     * 删除课程列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:remove')")
    @Log(title = "课程列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseService.deleteCourseByIds(ids));
    }

    /**
     * 更新课程状态
     */
    @PreAuthorize("@ss.hasPermi('system:course:edit')")
    @Log(title = "课程列表", businessType = BusinessType.UPDATE)
    @PutMapping("/status/{id}")
    public AjaxResult updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> request)
    {
        String status = request.get("status");
        String rejectReason = request.get("rejectReason");
        
        if (status == null || status.isEmpty()) {
            return AjaxResult.error("状态不能为空");
        }
        
        Course course = courseService.selectCourseById(id);
        if (course == null) {
            return AjaxResult.error("课程不存在");
        }
        
        course.setStatus(status);
        if ("rejected".equals(status) && rejectReason != null) {
            course.setRejectReason(rejectReason);
        }
        
        return toAjax(courseService.updateCourse(course));
    }

}
