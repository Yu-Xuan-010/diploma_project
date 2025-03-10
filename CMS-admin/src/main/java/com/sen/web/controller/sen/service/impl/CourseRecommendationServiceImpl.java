package com.sen.web.controller.sen.service.impl;

import java.util.List;

import com.sen.web.controller.sen.domain.CourseRecommendation;
import com.sen.web.controller.sen.mapper.CourseRecommendationMapper;
import com.sen.web.controller.sen.service.ICourseRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 课程推荐Service业务层处理
 * 
 * @author sen
 * @date 2025-03-10
 */
@Service
public class CourseRecommendationServiceImpl implements ICourseRecommendationService
{
    @Autowired
    private CourseRecommendationMapper courseRecommendationMapper;

    /**
     * 查询课程推荐
     * 
     * @param id 课程推荐主键
     * @return 课程推荐
     */
    @Override
    public CourseRecommendation selectCourseRecommendationById(Long id)
    {
        return courseRecommendationMapper.selectCourseRecommendationById(id);
    }

    /**
     * 查询课程推荐列表
     * 
     * @param courseRecommendation 课程推荐
     * @return 课程推荐
     */
    @Override
    public List<CourseRecommendation> selectCourseRecommendationList(CourseRecommendation courseRecommendation)
    {
        return courseRecommendationMapper.selectCourseRecommendationList(courseRecommendation);
    }

    /**
     * 新增课程推荐
     * 
     * @param courseRecommendation 课程推荐
     * @return 结果
     */
    @Override
    public int insertCourseRecommendation(CourseRecommendation courseRecommendation)
    {
        return courseRecommendationMapper.insertCourseRecommendation(courseRecommendation);
    }

    /**
     * 修改课程推荐
     * 
     * @param courseRecommendation 课程推荐
     * @return 结果
     */
    @Override
    public int updateCourseRecommendation(CourseRecommendation courseRecommendation)
    {
        return courseRecommendationMapper.updateCourseRecommendation(courseRecommendation);
    }

    /**
     * 批量删除课程推荐
     * 
     * @param ids 需要删除的课程推荐主键
     * @return 结果
     */
    @Override
    public int deleteCourseRecommendationByIds(Long[] ids)
    {
        return courseRecommendationMapper.deleteCourseRecommendationByIds(ids);
    }

    /**
     * 删除课程推荐信息
     * 
     * @param id 课程推荐主键
     * @return 结果
     */
    @Override
    public int deleteCourseRecommendationById(Long id)
    {
        return courseRecommendationMapper.deleteCourseRecommendationById(id);
    }
}
