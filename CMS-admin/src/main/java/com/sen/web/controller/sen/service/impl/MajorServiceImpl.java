package com.sen.web.controller.sen.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sen.web.controller.sen.mapper.MajorMapper;
import com.sen.web.controller.sen.domain.Major;
import com.sen.web.controller.sen.service.IMajorService;

/**
 * 专业管理Service业务层处理
 * 
 * @author sen
 */
@Service
public class MajorServiceImpl implements IMajorService 
{
    @Autowired
    private MajorMapper majorMapper;

    /**
     * 查询专业列表
     * 
     * @param major 专业信息
     * @return 专业
     */
    @Override
    public List<Major> selectMajorList(Major major)
    {
        return majorMapper.selectMajorList(major);
    }

    /**
     * 查询专业信息
     * 
     * @param id 专业ID
     * @return 专业信息
     */
    @Override
    public Major selectMajorById(Long id)
    {
        return majorMapper.selectMajorById(id);
    }

    /**
     * 新增专业
     * 
     * @param major 专业信息
     * @return 结果
     */
    @Override
    public int insertMajor(Major major)
    {
        return majorMapper.insertMajor(major);
    }

    /**
     * 修改专业
     * 
     * @param major 专业信息
     * @return 结果
     */
    @Override
    public int updateMajor(Major major)
    {
        return majorMapper.updateMajor(major);
    }

    /**
     * 批量删除专业
     * 
     * @param ids 需要删除的专业ID
     * @return 结果
     */
    @Override
    public int deleteMajorByIds(Long[] ids)
    {
        return majorMapper.deleteMajorByIds(ids);
    }

    /**
     * 删除专业信息
     * 
     * @param id 专业ID
     * @return 结果
     */
    @Override
    public int deleteMajorById(Long id)
    {
        return majorMapper.deleteMajorById(id);
    }
} 