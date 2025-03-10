package com.sen.web.controller.sen.domain;

import lombok.Data;
import java.util.Date;

@Data
public class SystemAnnouncement {
    private Long id;
    private String title;
    private String content;
    private String status;  // 0-禁用 1-启用
    private Date createdAt;
    private Date updatedAt;
}