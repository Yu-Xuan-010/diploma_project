package com.sen.web.controller.sen.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

/**
 * 反馈回复对象 feedback_reply
 */
public class FeedbackReply extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 回复ID */
    private Long id;

    /** 反馈ID */
    @Excel(name = "反馈ID")
    private Long feedbackId;

    /** 回复人 */
    @Excel(name = "回复人")
    private String replyBy;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String replyText;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("feedbackId", getFeedbackId())
                .append("replyBy", getReplyBy())
                .append("replyText", getReplyText())
                .append("createdAt", getCreatedAt())
                .toString();
    }
}