package com.ruoyi.web.controller.sen.service.impl;

import java.util.List;

import com.ruoyi.web.controller.sen.domain.UserList;
import com.ruoyi.web.controller.sen.mapper.UserListMapper;
import com.ruoyi.web.controller.sen.service.IUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 用户Service业务层处理
 * 
 * @author sen
 * @date 2025-03-06
 */
@Service
public class UserListServiceImpl implements IUserListService
{
    @Autowired
    private UserListMapper userListMapper;

    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public UserList selectUserListById(Long id)
    {
        return userListMapper.selectUserListById(id);
    }

    /**
     * 查询用户列表
     * 
     * @param userList 用户
     * @return 用户
     */
    @Override
    public List<UserList> selectUserListList(UserList userList)
    {
        return userListMapper.selectUserListList(userList);
    }

    /**
     * 新增用户
     * 
     * @param userList 用户
     * @return 结果
     */
    @Override
    public int insertUserList(UserList userList)
    {
        return userListMapper.insertUserList(userList);
    }

    /**
     * 修改用户
     * 
     * @param userList 用户
     * @return 结果
     */
    @Override
    public int updateUserList(UserList userList)
    {
        return userListMapper.updateUserList(userList);
    }

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteUserListByIds(Long[] ids)
    {
        return userListMapper.deleteUserListByIds(ids);
    }

    /**
     * 删除用户信息
     * 
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteUserListById(Long id)
    {
        return userListMapper.deleteUserListById(id);
    }
}
