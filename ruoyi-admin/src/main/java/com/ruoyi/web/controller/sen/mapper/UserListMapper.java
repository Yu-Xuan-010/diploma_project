package com.ruoyi.web.controller.sen.mapper;

import com.ruoyi.web.controller.sen.domain.UserList;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author sen
 * @date 2025-03-06
 */
public interface UserListMapper 
{
    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    public UserList selectUserListById(Long id);

    /**
     * 查询用户列表
     * 
     * @param userList 用户
     * @return 用户集合
     */
    public List<UserList> selectUserListList(UserList userList);

    /**
     * 新增用户
     * 
     * @param userList 用户
     * @return 结果
     */
    public int insertUserList(UserList userList);

    /**
     * 修改用户
     * 
     * @param userList 用户
     * @return 结果
     */
    public int updateUserList(UserList userList);

    /**
     * 删除用户
     * 
     * @param id 用户主键
     * @return 结果
     */
    public int deleteUserListById(Long id);

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserListByIds(Long[] ids);
}
