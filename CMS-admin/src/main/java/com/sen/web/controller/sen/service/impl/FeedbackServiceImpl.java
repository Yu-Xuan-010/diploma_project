package com.sen.web.controller.sen.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sen.web.controller.sen.mapper.FeedbackMapper;
import com.sen.web.controller.sen.domain.Feedback;
import com.sen.web.controller.sen.service.ISenFeedbackService;

/**
 * 反馈管理Service业务层处理
 */
@Service
public class FeedbackServiceImpl implements ISenFeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 查询反馈列表
     * 
     * @param feedback 反馈信息
     * @return 反馈
     */
    @Override
    public List<Feedback> selectSenFeedbackList(Feedback feedback) {
        return feedbackMapper.selectSenFeedbackList(feedback);
    }

    /**
     * 查询反馈信息
     * 
     * @param id 反馈ID
     * @return 反馈信息
     */
    @Override
    public Feedback selectSenFeedbackById(Long id) {
        return feedbackMapper.selectSenFeedbackById(id);
    }

    /**
     * 新增反馈
     * 
     * @param feedback 反馈信息
     * @return 结果
     */
    @Override
    public int insertSenFeedback(Feedback feedback) {
        return feedbackMapper.insertSenFeedback(feedback);
    }

    /**
     * 修改反馈
     * 
     * @param feedback 反馈信息
     * @return 结果
     */
    @Override
    public int updateSenFeedback(Feedback feedback) {
        return feedbackMapper.updateSenFeedback(feedback);
    }

    /**
     * 批量删除反馈
     * 
     * @param ids 需要删除的反馈ID
     * @return 结果
     */
    @Override
    public int deleteSenFeedbackByIds(Long[] ids) {
        return feedbackMapper.deleteSenFeedbackByIds(ids);
    }

    /**
     * 删除反馈信息
     * 
     * @param id 反馈ID
     * @return 结果
     */
    @Override
    public int deleteSenFeedbackById(Long id) {
        return feedbackMapper.deleteSenFeedbackById(id);
    }

    /**
     * 回复反馈
     * 
     * @param id 反馈ID
     * @param replyText 回复内容
     * @return 结果
     */
    @Override
    public int replySenFeedback(Long id, String replyText) {
        return feedbackMapper.insertFeedbackReply(id, replyText);
    }
}
