package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.TeacherApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教师申请数据访问层
 */
@Mapper
public interface TeacherApplicationMapper {
    
    /**
     * 查询所有教师申请
     */
    List<TeacherApplication> selectAll();
    
    /**
     * 根据状态查询教师申请
     */
    List<TeacherApplication> selectByStatus(@Param("status") String status);
    
    /**
     * 根据ID查询教师申请
     */
    TeacherApplication selectById(@Param("id") Long id);
    
    /**
     * 更新教师申请状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status,
                    @Param("reviewerId") Long reviewerId, @Param("reviewerName") String reviewerName,
                    @Param("reviewComment") String reviewComment);
} 