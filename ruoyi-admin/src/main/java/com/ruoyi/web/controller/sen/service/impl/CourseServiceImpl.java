package com.ruoyi.web.controller.sen.service.impl;

import java.util.List;

import com.ruoyi.web.controller.sen.domain.Course;
import com.ruoyi.web.controller.sen.mapper.CourseMapper;
import com.ruoyi.web.controller.sen.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 课程列表Service业务层处理
 * 
 * @author sen
 * @date 2025-03-05
 */
@Service
public class CourseServiceImpl implements ICourseService
{
    @Autowired
    private CourseMapper courseMapper;

    /**
     * 查询课程列表
     * 
     * @param id 课程列表主键
     * @return 课程列表
     */
    @Override
    public Course selectCourseById(Long id)
    {
        return courseMapper.selectCourseById(id);
    }

    /**
     * 查询课程列表列表
     * 
     * @param course 课程列表
     * @return 课程列表
     */
    @Override
    public List<Course> selectCourseList(Course course)
    {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程列表
     * 
     * @param course 课程列表
     * @return 结果
     */
    @Override
    public int insertCourse(Course course)
    {
        return courseMapper.insertCourse(course);
    }

    /**
     * 修改课程列表
     * 
     * @param course 课程列表
     * @return 结果
     */
    @Override
    public int updateCourse(Course course)
    {
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程列表
     * 
     * @param ids 需要删除的课程列表主键
     * @return 结果
     */
    @Override
    public int deleteCourseByIds(Long[] ids)
    {
        return courseMapper.deleteCourseByIds(ids);
    }

    /**
     * 删除课程列表信息
     * 
     * @param id 课程列表主键
     * @return 结果
     */
    @Override
    public int deleteCourseById(Long id)
    {
        return courseMapper.deleteCourseById(id);
    }
}
