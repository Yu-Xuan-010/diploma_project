package com.cms.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.reception.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    IPage<Course> selectCoursePage(Page<Course> page, @Param("categoryId") Long categoryId, 
                                 @Param("keyword") String keyword, @Param("status") String status);
                                 
    Course selectCourseDetail(@Param("id") Long id);
} 