package com.ruoyi.web.controller.sen.mapper;

import java.util.List;
import com.ruoyi.web.controller.sen.domain.Feedback;

/**
 * 反馈管理Mapper接口
 */
public interface FeedbackMapper {
    /**
     * 查询反馈列表
     *
     * @param feedback 反馈信息
     * @return 反馈集合
     */
    public List<Feedback> selectSenFeedbackList(Feedback feedback);

    /**
     * 查询反馈信息
     *
     * @param id 反馈ID
     * @return 反馈信息
     */
    public Feedback selectSenFeedbackById(Long id);

    /**
     * 新增反馈
     *
     * @param feedback 反馈信息
     * @return 结果
     */
    public int insertSenFeedback(Feedback feedback);

    /**
     * 修改反馈
     *
     * @param feedback 反馈信息
     * @return 结果
     */
    public int updateSenFeedback(Feedback feedback);

    /**
     * 删除反馈
     *
     * @param id 反馈ID
     * @return 结果
     */
    public int deleteSenFeedbackById(Long id);

    /**
     * 批量删除反馈
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSenFeedbackByIds(Long[] ids);

    /**
     * 新增反馈回复
     *
     * @param feedbackId 反馈ID
     * @param replyText 回复内容
     * @return 结果
     */
    public int insertFeedbackReply(Long feedbackId, String replyText);
}