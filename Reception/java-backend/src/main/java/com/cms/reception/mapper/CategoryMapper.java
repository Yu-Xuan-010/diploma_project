package com.cms.reception.mapper;

import com.cms.reception.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    /**
     * 查询所有分类
     */
    List<Category> selectAll();
    
    /**
     * 根据父ID查询分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据ID查询分类
     */
    Category selectById(@Param("id") Long id);
    
    /**
     * 新增分类
     */
    int insert(Category category);
    
    /**
     * 更新分类
     */
    int update(Category category);
    
    /**
     * 删除分类
     */
    int deleteById(@Param("id") Long id);
} 