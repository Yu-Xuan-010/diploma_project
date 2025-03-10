package com.sen.web.controller.sen.service.impl;

import java.util.List;

import com.sen.web.controller.sen.domain.UserLog;
import com.sen.web.controller.sen.mapper.UserLogMapper;
import com.sen.web.controller.sen.service.IUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户日志Service业务层处理
 * 
 * @author sen
 * @date 2025-03-09
 */
@Service
public class UserLogServiceImpl implements IUserLogService
{
    @Autowired
    private UserLogMapper userLogMapper;

    /**
     * 查询用户日志
     * 
     * @param id 用户日志主键
     * @return 用户日志
     */
    @Override
    public UserLog selectUserLogById(Long id)
    {
        return userLogMapper.selectUserLogById(id);
    }

    /**
     * 查询用户日志列表
     * 
     * @param userLog 用户日志
     * @return 用户日志
     */
    @Override
    public List<UserLog> selectUserLogList(UserLog userLog)
    {
        return userLogMapper.selectUserLogList(userLog);
    }

    /**
     * 新增用户日志
     * 
     * @param userLog 用户日志
     * @return 结果
     */
    @Override
    public int insertUserLog(UserLog userLog)
    {
        return userLogMapper.insertUserLog(userLog);
    }

    /**
     * 修改用户日志
     * 
     * @param userLog 用户日志
     * @return 结果
     */
    @Override
    public int updateUserLog(UserLog userLog)
    {
        return userLogMapper.updateUserLog(userLog);
    }

    /**
     * 批量删除用户日志
     * 
     * @param ids 需要删除的用户日志主键
     * @return 结果
     */
    @Override
    public int deleteUserLogByIds(Long[] ids)
    {
        return userLogMapper.deleteUserLogByIds(ids);
    }

    /**
     * 删除用户日志信息
     * 
     * @param id 用户日志主键
     * @return 结果
     */
    @Override
    public int deleteUserLogById(Long id)
    {
        return userLogMapper.deleteUserLogById(id);
    }
}
