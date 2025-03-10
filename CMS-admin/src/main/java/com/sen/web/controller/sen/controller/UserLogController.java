package com.sen.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.sen.web.controller.sen.domain.UserLog;
import com.sen.web.controller.sen.service.IUserLogService;
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
import com.sen.common.annotation.Log;
import com.sen.common.core.controller.BaseController;
import com.sen.common.core.domain.AjaxResult;
import com.sen.common.enums.BusinessType;
import com.sen.common.utils.poi.ExcelUtil;
import com.sen.common.core.page.TableDataInfo;

/**
 * 用户日志Controller
 * 
 * @author sen
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/system/userlog")
public class UserLogController extends BaseController
{
    @Autowired
    private IUserLogService userLogService;

    /**
     * 查询用户日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserLog userLog)
    {
        startPage();
        List<UserLog> list = userLogService.selectUserLogList(userLog);
        return getDataTable(list);
    }

    /**
     * 导出用户日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:export')")
    @Log(title = "用户日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserLog userLog)
    {
        List<UserLog> list = userLogService.selectUserLogList(userLog);
        ExcelUtil<UserLog> util = new ExcelUtil<UserLog>(UserLog.class);
        util.exportExcel(response, list, "用户日志数据");
    }

    /**
     * 获取用户日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userLogService.selectUserLogById(id));
    }

    /**
     * 新增用户日志
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:add')")
    @Log(title = "用户日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserLog userLog)
    {
        return toAjax(userLogService.insertUserLog(userLog));
    }

    /**
     * 修改用户日志
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:edit')")
    @Log(title = "用户日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserLog userLog)
    {
        return toAjax(userLogService.updateUserLog(userLog));
    }

    /**
     * 删除用户日志
     */
    @PreAuthorize("@ss.hasPermi('system:userlog:remove')")
    @Log(title = "用户日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userLogService.deleteUserLogByIds(ids));
    }
}
