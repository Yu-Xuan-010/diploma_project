package com.sen.web.controller.sen.controller;

import com.sen.common.annotation.Log;
import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.common.core.page.TableDataInfo;
import com.sen.common.enums.BusinessType;
import com.sen.web.controller.sen.domain.Feedback;
import com.sen.web.controller.sen.service.ISenFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 反馈管理Controller
 */
@RestController
@RequestMapping("/system/feedback")
public class FeedbackController extends BaseController {
    @Autowired
    private ISenFeedbackService senFeedbackService;

    /**
     * 查询反馈列表
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(Feedback feedback) {
        startPage();
        List<Feedback> list = senFeedbackService.selectSenFeedbackList(feedback);
        return getDataTable(list);
    }

    /**
     * 获取反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(senFeedbackService.selectSenFeedbackById(id));
    }

    /**
     * 新增反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:add')")
    @Log(title = "反馈管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Feedback feedback) {
        return toAjax(senFeedbackService.insertSenFeedback(feedback));
    }

    /**
     * 修改反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:edit')")
    @Log(title = "反馈管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Feedback feedback) {
        return toAjax(senFeedbackService.updateSenFeedback(feedback));
    }

    /**
     * 删除反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:remove')")
    @Log(title = "反馈管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(senFeedbackService.deleteSenFeedbackByIds(ids));
    }

    /**
     * 回复反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:reply')")
    @Log(title = "反馈管理", businessType = BusinessType.UPDATE)
    @PostMapping("/reply/{id}")
    public AjaxResult reply(@PathVariable("id") Long id, @RequestBody String replyText) {
        return toAjax(senFeedbackService.replySenFeedback(id, replyText));
    }
}