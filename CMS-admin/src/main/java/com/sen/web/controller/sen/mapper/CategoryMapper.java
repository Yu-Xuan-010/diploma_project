package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.Category;
import com.sen.web.controller.sen.domain.Course;

import java.util.List;


/**
 * 课程种类Mapper接口
 * 
 * @author sen
 * @date 2025-03-06
 */
public interface CategoryMapper 
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
     * 删除课程种类
     * 
     * @param id 课程种类主键
     * @return 结果
     */
    public int deleteCategoryById(Long id);

    /**
     * 批量删除课程种类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCategoryByIds(Long[] ids);

    /**
     * 批量删除课程列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseByCategoryIds(Long[] ids);
    
    /**
     * 批量新增课程列表
     * 
     * @param courseList 课程列表列表
     * @return 结果
     */
    public int batchCourse(List<Course> courseList);
    

    /**
     * 通过课程种类主键删除课程列表信息
     * 
     * @param id 课程种类ID
     * @return 结果
     */
    public int deleteCourseByCategoryId(Long id);
}
