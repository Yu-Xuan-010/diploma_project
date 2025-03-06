package com.ruoyi.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.web.controller.sen.domain.UserList;
import com.ruoyi.web.controller.sen.service.IUserListService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户Controller
 * 
 * @author sen
 * @date 2025-03-06
 */
@RestController
@RequestMapping("/system/userlist")
public class UserListController extends BaseController
{
    @Autowired
    private IUserListService userListService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserList userList)
    {
        startPage();
        List<UserList> list = userListService.selectUserListList(userList);
        return getDataTable(list);
    }

    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:export')")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserList userList)
    {
        List<UserList> list = userListService.selectUserListList(userList);
        ExcelUtil<UserList> util = new ExcelUtil<UserList>(UserList.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userListService.selectUserListById(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:add')")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserList userList)
    {
        return toAjax(userListService.insertUserList(userList));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:edit')")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserList userList)
    {
        return toAjax(userListService.updateUserList(userList));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:userlist:remove')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userListService.deleteUserListByIds(ids));
    }
}
