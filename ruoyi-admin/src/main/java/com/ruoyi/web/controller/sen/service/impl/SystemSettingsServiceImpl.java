package com.ruoyi.web.controller.sen.service.impl;

import com.ruoyi.web.controller.sen.domain.SystemSettings;
import com.ruoyi.web.controller.sen.domain.SystemAnnouncement;
import com.ruoyi.web.controller.sen.mapper.SystemSettingsMapper;
import com.ruoyi.web.controller.sen.mapper.SystemAnnouncementMapper;
import com.ruoyi.web.controller.sen.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    
    @Autowired
    private SystemAnnouncementMapper systemAnnouncementMapper;

    @Override
    public SystemSettings getSystemSettings() {
        return systemSettingsMapper.getSystemSettings();
    }

    @Override
    public int updateSystemSettings(SystemSettings settings) {
        return systemSettingsMapper.updateSystemSettings(settings);
    }

    @Override
    public List<SystemAnnouncement> listAnnouncements() {
        return systemAnnouncementMapper.listAnnouncements();
    }

    @Override
    public SystemAnnouncement getAnnouncementById(Long id) {
        return systemAnnouncementMapper.getAnnouncementById(id);
    }

    @Override
    public int insertAnnouncement(SystemAnnouncement announcement) {
        return systemAnnouncementMapper.insertAnnouncement(announcement);
    }

    @Override
    public int updateAnnouncement(SystemAnnouncement announcement) {
        return systemAnnouncementMapper.updateAnnouncement(announcement);
    }

    @Override
    public int deleteAnnouncement(Long id) {
        return systemAnnouncementMapper.deleteAnnouncement(id);
    }
} 