package com.ruoyi.web.controller.sen.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.sen.domain.CourseComment;
import com.ruoyi.web.controller.sen.domain.CourseCommentReply;
import com.ruoyi.web.controller.sen.service.ICourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程评论管理Controller
 */
@RestController
@RequestMapping("/feedback/CCM")
public class CourseCommentController extends BaseController {
    @Autowired
    private ICourseCommentService courseCommentService;

    /**
     * 查询课程评论列表
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseComment courseComment) {
        startPage();
        List<CourseComment> list = courseCommentService.selectCourseCommentList(courseComment);
        return getDataTable(list);
    }

    /**
     * 获取课程评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(courseCommentService.selectCourseCommentById(id));
    }

    /**
     * 修改课程评论状态
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:edit')")
    @Log(title = "课程评论管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CourseComment courseComment) {
        return toAjax(courseCommentService.updateCourseComment(courseComment));
    }

    /**
     * 删除课程评论
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:remove')")
    @Log(title = "课程评论管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(courseCommentService.deleteCourseCommentByIds(ids));
    }

    /**
     * 查询评论回复列表
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:list')")
    @GetMapping("/reply/list/{commentId}")
    public TableDataInfo replyList(@PathVariable("commentId") Long commentId) {
        startPage();
        List<CourseCommentReply> list = courseCommentService.selectCourseCommentReplyByCommentId(commentId);
        return getDataTable(list);
    }

    /**
     * 新增评论回复
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:reply')")
    @Log(title = "课程评论回复", businessType = BusinessType.INSERT)
    @PostMapping("/reply")
    public AjaxResult addReply(@RequestBody CourseCommentReply courseCommentReply) {
        return toAjax(courseCommentService.insertCourseCommentReply(courseCommentReply));
    }

    /**
     * 删除评论回复
     */
    @PreAuthorize("@ss.hasPermi('system:CCM:reply')")
    @Log(title = "课程评论回复", businessType = BusinessType.DELETE)
    @DeleteMapping("/reply/{ids}")
    public AjaxResult removeReply(@PathVariable Long[] ids) {
        return toAjax(courseCommentService.deleteCourseCommentReplyByIds(ids));
    }
}