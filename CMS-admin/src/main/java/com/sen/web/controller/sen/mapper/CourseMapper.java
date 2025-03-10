package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.Course;

import java.util.List;

/**
 * 课程列表Mapper接口
 * 
 * @author sen
 * @date 2025-03-05
 */
public interface CourseMapper 
{
    /**
     * 查询课程列表
     * 
     * @param id 课程列表主键
     * @return 课程列表
     */
    public Course selectCourseById(Long id);

    /**
     * 查询课程列表列表
     * 
     * @param course 课程列表
     * @return 课程列表集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 新增课程列表
     * 
     * @param course 课程列表
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 修改课程列表
     * 
     * @param course 课程列表
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 删除课程列表
     * 
     * @param id 课程列表主键
     * @return 结果
     */
    public int deleteCourseById(Long id);

    /**
     * 批量删除课程列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseByIds(Long[] ids);
}
