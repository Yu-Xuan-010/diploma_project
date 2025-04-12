package com.sen.web.controller.sen.controller;

import com.sen.common.core.domain.AjaxResult;
import com.sen.web.controller.sen.domain.Major;
import com.sen.web.controller.sen.service.IMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/major")
public class MajorController {
    @Autowired
    private IMajorService majorService;

    @GetMapping("/list")
    public AjaxResult list(Major major) {
        List<Major> list = majorService.selectMajorList(major);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(majorService.selectMajorById(id));
    }
} 