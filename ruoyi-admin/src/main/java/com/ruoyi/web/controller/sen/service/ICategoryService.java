package com.ruoyi.web.controller.sen.service;

import com.ruoyi.web.controller.sen.domain.Category;

import java.util.List;


/**
 * 课程种类Service接口
 * 
 * @author sen
 * @date 2025-03-06
 */
public interface ICategoryService 
{
    /**
     * 查询课程种类
     * 
     * @param id 课程种类主键
     * @return 课程种类
     */
    public Category selectCategoryById(Long id);

    /**
     * 查询课程种类列表
     * 
     * @param category 课程种类
     * @return 课程种类集合
     */
    public List<Category> selectCategoryList(Category category);

    /**
     * 新增课程种类
     * 
     * @param category 课程种类
     * @return 结果
     */
    public int insertCategory(Category category);

    /**
     * 修改课程种类
     * 
     * @param category 课程种类
     * @return 结果
     */
    public int updateCategory(Category category);

    /**
     * 批量删除课程种类
     * 
     * @param ids 需要删除的课程种类主键集合
     * @return 结果
     */
    public int deleteCategoryByIds(Long[] ids);

    /**
     * 删除课程种类信息
     * 
     * @param id 课程种类主键
     * @return 结果
     */
    public int deleteCategoryById(Long id);
}
