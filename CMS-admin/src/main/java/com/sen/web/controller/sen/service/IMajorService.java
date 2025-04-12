package com.sen.web.controller.sen.service;

import java.util.List;
import com.sen.web.controller.sen.domain.Major;

/**
 * 专业管理Service接口
 * 
 * @author sen
 */
public interface IMajorService 
{
    /**
     * 查询专业列表
     * 
     * @param major 专业信息
     * @return 专业集合
     */
    public List<Major> selectMajorList(Major major);

    /**
     * 查询专业信息
     * 
     * @param id 专业ID
     * @return 专业信息
     */
    public Major selectMajorById(Long id);

    /**
     * 新增专业
     * 
     * @param major 专业信息
     * @return 结果
     */
    public int insertMajor(Major major);

    /**
     * 修改专业
     * 
     * @param major 专业信息
     * @return 结果
     */
    public int updateMajor(Major major);

    /**
     * 批量删除专业
     * 
     * @param ids 需要删除的专业ID
     * @return 结果
     */
    public int deleteMajorByIds(Long[] ids);

    /**
     * 删除专业信息
     * 
     * @param id 专业ID
     * @return 结果
     */
    public int deleteMajorById(Long id);
} 