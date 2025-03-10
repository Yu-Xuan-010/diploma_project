package com.sen.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.sen.web.controller.sen.domain.Lesson;
import com.sen.web.controller.sen.service.ILessonService;
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
 * 课时列表Controller
 * 
 * @author sen
 * @date 2025-03-05
 */
@RestController
@RequestMapping("/system/lesson")
public class LessonController extends BaseController
{
    @Autowired
    private ILessonService lessonService;

    /**
     * 查询课时列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:list')")
    @GetMapping("/list")
    public TableDataInfo list(Lesson lesson)
    {
        startPage();
        List<Lesson> list = lessonService.selectLessonList(lesson);
        return getDataTable(list);
    }

    /**
     * 导出课时列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:export')")
    @Log(title = "课时列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Lesson lesson)
    {
        List<Lesson> list = lessonService.selectLessonList(lesson);
        ExcelUtil<Lesson> util = new ExcelUtil<Lesson>(Lesson.class);
        util.exportExcel(response, list, "课时列表数据");
    }

    /**
     * 获取课时列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lessonService.selectLessonById(id));
    }

    /**
     * 新增课时列表
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:add')")
    @Log(title = "课时列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Lesson lesson)
    {
        return toAjax(lessonService.insertLesson(lesson));
    }

    /**
     * 修改课时列表
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:edit')")
    @Log(title = "课时列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Lesson lesson)
    {
        return toAjax(lessonService.updateLesson(lesson));
    }

    /**
     * 删除课时列表
     */
    @PreAuthorize("@ss.hasPermi('system:lesson:remove')")
    @Log(title = "课时列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lessonService.deleteLessonByIds(ids));
    }
}
