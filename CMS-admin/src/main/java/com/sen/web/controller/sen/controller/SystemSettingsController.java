package com.sen.web.controller.sen.controller;

import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.web.controller.sen.domain.SystemSettings;
import com.sen.web.controller.sen.domain.SystemAnnouncement;
import com.sen.web.controller.sen.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sen/system")
public class SystemSettingsController extends BaseController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @GetMapping("/settings")
    public AjaxResult getSystemSettings() {
        return AjaxResult.success(systemSettingsService.getSystemSettings());
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @PutMapping("/settings")
    public AjaxResult updateSystemSettings(@RequestBody SystemSettings settings) {
        return toAjax(systemSettingsService.updateSystemSettings(settings));
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @GetMapping("/announcements")
    public AjaxResult listAnnouncements() {
        List<SystemAnnouncement> list = systemSettingsService.listAnnouncements();
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @GetMapping("/announcements/{id}")
    public AjaxResult getAnnouncement(@PathVariable Long id) {
        return AjaxResult.success(systemSettingsService.getAnnouncementById(id));
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @PostMapping("/announcements")
    public AjaxResult addAnnouncement(@RequestBody SystemAnnouncement announcement) {
        return toAjax(systemSettingsService.insertAnnouncement(announcement));
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @PutMapping("/announcements")
    public AjaxResult updateAnnouncement(@RequestBody SystemAnnouncement announcement) {
        return toAjax(systemSettingsService.updateAnnouncement(announcement));
    }

    @PreAuthorize("@ss.hasPermi('system:settings:list')")
    @DeleteMapping("/announcements/{id}")
    public AjaxResult deleteAnnouncement(@PathVariable Long id) {
        return toAjax(systemSettingsService.deleteAnnouncement(id));
    }
} 