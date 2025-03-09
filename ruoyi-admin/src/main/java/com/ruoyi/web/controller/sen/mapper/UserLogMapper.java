package com.ruoyi.web.controller.sen.mapper;

import com.ruoyi.web.controller.sen.domain.UserLog;
import java.util.List;


/**
 * 用户日志Mapper接口
 * 
 * @author sen
 * @date 2025-03-09
 */
public interface UserLogMapper 
{
    /**
     * 查询用户日志
     * 
     * @param id 用户日志主键
     * @return 用户日志
     */
    public UserLog selectUserLogById(Long id);

    /**
     * 查询用户日志列表
     * 
     * @param userLog 用户日志
     * @return 用户日志集合
     */
    public List<UserLog> selectUserLogList(UserLog userLog);

    /**
     * 新增用户日志
     * 
     * @param userLog 用户日志
     * @return 结果
     */
    public int insertUserLog(UserLog userLog);

    /**
     * 修改用户日志
     * 
     * @param userLog 用户日志
     * @return 结果
     */
    public int updateUserLog(UserLog userLog);

    /**
     * 删除用户日志
     * 
     * @param id 用户日志主键
     * @return 结果
     */
    public int deleteUserLogById(Long id);

    /**
     * 批量删除用户日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLogByIds(Long[] ids);
}
