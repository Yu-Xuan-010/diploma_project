package com.ruoyi.web.controller.sen.domain;

import lombok.Data;
import java.util.Date;

@Data
public class SystemSettings {
    private Long id;
    private String siteName;
    private String siteLogo;
    private String siteFavicon;
    private String siteDescription;
    private String siteKeywords;
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;
    private Date createdAt;
    private Date updatedAt;
}