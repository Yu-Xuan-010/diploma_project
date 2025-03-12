package com.cms.reception.controller;

import com.cms.reception.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public Result<String> testDb() {
        try {
            // 测试数据库连接
            String result = jdbcTemplate.queryForObject("SELECT 'Database connection successful!' as test", String.class);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("数据库连接失败: " + e.getMessage());
        }
    }
} 