package com.ruoyi.web.controller.sen.service;

import com.ruoyi.web.controller.sen.domain.Lesson;

import java.util.List;

/**
 * 课时列表Service接口
 * 
 * @author sen
 * @date 2025-03-05
 */
public interface ILessonService 
{
    /**
     * 查询课时列表
     * 
     * @param id 课时列表主键
     * @return 课时列表
     */
    public Lesson selectLessonById(Long id);

    /**
     * 查询课时列表列表
     * 
     * @param lesson 课时列表
     * @return 课时列表集合
     */
    public List<Lesson> selectLessonList(Lesson lesson);

    /**
     * 新增课时列表
     * 
     * @param lesson 课时列表
     * @return 结果
     */
    public int insertLesson(Lesson lesson);

    /**
     * 修改课时列表
     * 
     * @param lesson 课时列表
     * @return 结果
     */
    public int updateLesson(Lesson lesson);

    /**
     * 批量删除课时列表
     * 
     * @param ids 需要删除的课时列表主键集合
     * @return 结果
     */
    public int deleteLessonByIds(Long[] ids);

    /**
     * 删除课时列表信息
     * 
     * @param id 课时列表主键
     * @return 结果
     */
    public int deleteLessonById(Long id);
}
