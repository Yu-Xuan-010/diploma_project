package com.sen.web.controller.sen.service.impl;

import java.util.List;

import com.sen.web.controller.sen.domain.Category;
import com.sen.web.controller.sen.domain.Course;
import com.sen.web.controller.sen.mapper.CategoryMapper;
import com.sen.web.controller.sen.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.sen.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程种类Service业务层处理
 * 
 * @author sen
 * @date 2025-03-06
 */
@Service
public class CategoryServiceImpl implements ICategoryService
{
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询课程种类
     * 
     * @param id 课程种类主键
     * @return 课程种类
     */
    @Override
    public Category selectCategoryById(Long id)
    {
        return categoryMapper.selectCategoryById(id);
    }

    /**
     * 查询课程种类列表
     * 
     * @param category 课程种类
     * @return 课程种类
     */
    @Override
    public List<Category> selectCategoryList(Category category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    /**
     * 新增课程种类
     * 
     * @param category 课程种类
     * @return 结果
     */
    @Transactional
    @Override
    public int insertCategory(Category category)
    {
        int rows = categoryMapper.insertCategory(category);
        insertCourse(category);
        return rows;
    }

    /**
     * 修改课程种类
     * 
     * @param category 课程种类
     * @return 结果
     */
    @Transactional
    @Override
    public int updateCategory(Category category)
    {
        categoryMapper.deleteCourseByCategoryId(category.getId());
        insertCourse(category);
        return categoryMapper.updateCategory(category);
    }

    /**
     * 批量删除课程种类
     * 
     * @param ids 需要删除的课程种类主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteCategoryByIds(Long[] ids)
    {
        categoryMapper.deleteCourseByCategoryIds(ids);
        return categoryMapper.deleteCategoryByIds(ids);
    }

    /**
     * 删除课程种类信息
     * 
     * @param id 课程种类主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteCategoryById(Long id)
    {
        categoryMapper.deleteCourseByCategoryId(id);
        return categoryMapper.deleteCategoryById(id);
    }

    /**
     * 新增课程列表信息
     * 
     * @param category 课程种类对象
     */
    public void insertCourse(Category category)
    {
        List<Course> courseList = category.getCourseList();
        Long id = category.getId();
        if (StringUtils.isNotNull(courseList))
        {
            List<Course> list = new ArrayList<Course>();
            for (Course course : courseList)
            {
                course.setCategoryId(id);
                list.add(course);
            }
            if (list.size() > 0)
            {
                categoryMapper.batchCourse(list);
            }
        }
    }
}
