package com.ruoyi.web.controller.sen.mapper;

import com.ruoyi.web.controller.sen.domain.SystemSettings;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SystemSettingsMapper {

    @Select("SELECT * FROM system_settings LIMIT 1")
    SystemSettings getSystemSettings();

    @Update("UPDATE system_settings SET " +
            "site_name = #{siteName}, " +
            "site_logo = #{siteLogo}, " +
            "site_favicon = #{siteFavicon}, " +
            "site_description = #{siteDescription}, " +
            "site_keywords = #{siteKeywords}, " +
            "contact_email = #{contactEmail}, " +
            "contact_phone = #{contactPhone}, " +
            "contact_address = #{contactAddress}, " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int updateSystemSettings(SystemSettings settings);
}