package com.ruoyi.web.controller.sen.mapper;

import com.ruoyi.web.controller.sen.domain.SystemAnnouncement;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SystemAnnouncementMapper {
    
    @Select("SELECT * FROM system_announcements ORDER BY created_at DESC")
    List<SystemAnnouncement> listAnnouncements();
    
    @Select("SELECT * FROM system_announcements WHERE id = #{id}")
    SystemAnnouncement getAnnouncementById(Long id);
    
    @Insert("INSERT INTO system_announcements(title, content, status, created_at, updated_at) " +
            "VALUES(#{title}, #{content}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAnnouncement(SystemAnnouncement announcement);
    
    @Update("UPDATE system_announcements SET " +
            "title = #{title}, " +
            "content = #{content}, " +
            "status = #{status}, " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int updateAnnouncement(SystemAnnouncement announcement);
    
    @Delete("DELETE FROM system_announcements WHERE id = #{id}")
    int deleteAnnouncement(Long id);
} 