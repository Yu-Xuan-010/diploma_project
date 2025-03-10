package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.SystemSettings;
import com.sen.web.controller.sen.domain.SystemAnnouncement;
import java.util.List;

public interface SystemSettingsService {
    SystemSettings getSystemSettings();
    int updateSystemSettings(SystemSettings settings);
    
    List<SystemAnnouncement> listAnnouncements();
    SystemAnnouncement getAnnouncementById(Long id);
    int insertAnnouncement(SystemAnnouncement announcement);
    int updateAnnouncement(SystemAnnouncement announcement);
    int deleteAnnouncement(Long id);
} 