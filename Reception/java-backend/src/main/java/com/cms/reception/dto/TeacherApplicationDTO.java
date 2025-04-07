package com.cms.reception.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeacherApplicationDTO {
    private Long userId;
    private String reason;
    private List<String> expertise;
    private String experience;
} 