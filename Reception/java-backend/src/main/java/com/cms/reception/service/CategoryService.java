package com.cms.reception.service;

import com.cms.reception.entity.Category;
import java.util.List;

public interface CategoryService {
    
    /**
     * 获取所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 根据父ID获取分类列表
     */
    List<Category> getCategoriesByParentId(Long parentId);
    
    /**
     * 根据ID获取分类
     */
    Category getCategoryById(Long id);
    
    /**
     * 创建分类
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类
     */
    Category updateCategory(Category category);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 获取子分类列表
     */
    List<Category> getSubCategories(Long parentId);
} 