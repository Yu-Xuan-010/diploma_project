package com.ruoyi.web.controller.sen.service.impl;

import java.util.List;

import com.ruoyi.web.controller.sen.domain.Lesson;
import com.ruoyi.web.controller.sen.mapper.LessonMapper;
import com.ruoyi.web.controller.sen.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 课时列表Service业务层处理
 * 
 * @author sen
 * @date 2025-03-05
 */
@Service
public class LessonServiceImpl implements ILessonService
{
    @Autowired
    private LessonMapper lessonMapper;

    /**
     * 查询课时列表
     * 
     * @param id 课时列表主键
     * @return 课时列表
     */
    @Override
    public Lesson selectLessonById(Long id)
    {
        return lessonMapper.selectLessonById(id);
    }

    /**
     * 查询课时列表列表
     * 
     * @param lesson 课时列表
     * @return 课时列表
     */
    @Override
    public List<Lesson> selectLessonList(Lesson lesson)
    {
        return lessonMapper.selectLessonList(lesson);
    }

    /**
     * 新增课时列表
     * 
     * @param lesson 课时列表
     * @return 结果
     */
    @Override
    public int insertLesson(Lesson lesson)
    {
        return lessonMapper.insertLesson(lesson);
    }

    /**
     * 修改课时列表
     * 
     * @param lesson 课时列表
     * @return 结果
     */
    @Override
    public int updateLesson(Lesson lesson)
    {
        return lessonMapper.updateLesson(lesson);
    }

    /**
     * 批量删除课时列表
     * 
     * @param ids 需要删除的课时列表主键
     * @return 结果
     */
    @Override
    public int deleteLessonByIds(Long[] ids)
    {
        return lessonMapper.deleteLessonByIds(ids);
    }

    /**
     * 删除课时列表信息
     * 
     * @param id 课时列表主键
     * @return 结果
     */
    @Override
    public int deleteLessonById(Long id)
    {
        return lessonMapper.deleteLessonById(id);
    }
}
