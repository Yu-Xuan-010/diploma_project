package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.CourseRecommendation;
import java.util.List;


/**
 * 课程推荐Mapper接口
 * 
 * @author sen
 * @date 2025-03-10
 */
public interface CourseRecommendationMapper 
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
     * 删除课程推荐
     * 
     * @param id 课程推荐主键
     * @return 结果
     */
    public int deleteCourseRecommendationById(Long id);

    /**
     * 批量删除课程推荐
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseRecommendationByIds(Long[] ids);
}
