package com.sen.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.sen.web.controller.sen.domain.CourseRecommendation;
import com.sen.web.controller.sen.service.ICourseRecommendationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sen.common.annotation.Log;
import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.common.enums.BusinessType;
import com.sen.common.utils.poi.ExcelUtil;
import com.sen.common.core.page.TableDataInfo;

/**
 * 课程推荐Controller
 * 
 * @author sen
 * @date 2025-03-10
 */
@RestController
@RequestMapping("/system/recommendation")
public class CourseRecommendationController extends BaseController
{
    @Autowired
    private ICourseRecommendationService courseRecommendationService;

    /**
     * 查询课程推荐列表
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseRecommendation courseRecommendation)
    {
        startPage();
        List<CourseRecommendation> list = courseRecommendationService.selectCourseRecommendationList(courseRecommendation);
        return getDataTable(list);
    }

    /**
     * 导出课程推荐列表
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:export')")
    @Log(title = "课程推荐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseRecommendation courseRecommendation)
    {
        List<CourseRecommendation> list = courseRecommendationService.selectCourseRecommendationList(courseRecommendation);
        ExcelUtil<CourseRecommendation> util = new ExcelUtil<CourseRecommendation>(CourseRecommendation.class);
        util.exportExcel(response, list, "课程推荐数据");
    }

    /**
     * 获取课程推荐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(courseRecommendationService.selectCourseRecommendationById(id));
    }

    /**
     * 新增课程推荐
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:add')")
    @Log(title = "课程推荐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseRecommendation courseRecommendation)
    {
        return toAjax(courseRecommendationService.insertCourseRecommendation(courseRecommendation));
    }

    /**
     * 修改课程推荐
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:edit')")
    @Log(title = "课程推荐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseRecommendation courseRecommendation)
    {
        return toAjax(courseRecommendationService.updateCourseRecommendation(courseRecommendation));
    }

    /**
     * 删除课程推荐
     */
    @PreAuthorize("@ss.hasPermi('system:recommendation:remove')")
    @Log(title = "课程推荐", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseRecommendationService.deleteCourseRecommendationByIds(ids));
    }
}
