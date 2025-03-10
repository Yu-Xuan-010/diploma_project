package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.CourseRecommendation;

import java.util.List;


/**
 * 课程推荐Service接口
 * 
 * @author sen
 * @date 2025-03-10
 */
public interface ICourseRecommendationService 
{
    /**
     * 查询课程推荐
     * 
     * @param id 课程推荐主键
     * @return 课程推荐
     */
    public CourseRecommendation selectCourseRecommendationById(Long id);

    /**
     * 查询课程推荐列表
     * 
     * @param courseRecommendation 课程推荐
     * @return 课程推荐集合
     */
    public List<CourseRecommendation> selectCourseRecommendationList(CourseRecommendation courseRecommendation);

    /**
     * 新增课程推荐
     * 
     * @param courseRecommendation 课程推荐
     * @return 结果
     */
    public int insertCourseRecommendation(CourseRecommendation courseRecommendation);

    /**
     * 修改课程推荐
     * 
     * @param courseRecommendation 课程推荐
     * @return 结果
     */
    public int updateCourseRecommendation(CourseRecommendation courseRecommendation);

    /**
     * 批量删除课程推荐
     * 
     * @param ids 需要删除的课程推荐主键集合
     * @return 结果
     */
    public int deleteCourseRecommendationByIds(Long[] ids);

    /**
     * 删除课程推荐信息
     * 
     * @param id 课程推荐主键
     * @return 结果
     */
    public int deleteCourseRecommendationById(Long id);
}
