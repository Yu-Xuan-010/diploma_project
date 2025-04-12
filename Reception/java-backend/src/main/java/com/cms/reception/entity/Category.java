package com.cms.reception.entity;

import lombok.Data;

@Data
public class Category {
    
    /**
     * 分类ID
     */
    private Long id;
    
    /**
     * 种类名称
     */
    private String name;
    
    /**
     * 父类id
     */
    private Long parentId;
    
    /**
     * 分类描述
     */
    private String description;
    
    /**
     * 排序
     */
    private Long sortOrder;
}
