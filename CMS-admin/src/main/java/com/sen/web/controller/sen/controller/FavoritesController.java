package com.sen.web.controller.sen.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.sen.web.controller.sen.domain.Favorites;
import com.sen.web.controller.sen.service.IFavoritesService;
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
 * 课程收藏Controller
 * 
 * @author sen
 * @date 2025-03-11
 */
@RestController
@RequestMapping("/system/favorites")
public class FavoritesController extends BaseController
{
    @Autowired
    private IFavoritesService favoritesService;

    /**
     * 查询课程收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:list')")
    @GetMapping("/list")
    public TableDataInfo list(Favorites favorites)
    {
        startPage();
        List<Favorites> list = favoritesService.selectFavoritesList(favorites);
        return getDataTable(list);
    }

    /**
     * 导出课程收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:export')")
    @Log(title = "课程收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Favorites favorites)
    {
        List<Favorites> list = favoritesService.selectFavoritesList(favorites);
        ExcelUtil<Favorites> util = new ExcelUtil<Favorites>(Favorites.class);
        util.exportExcel(response, list, "课程收藏数据");
    }

    /**
     * 获取课程收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(favoritesService.selectFavoritesById(id));
    }

    /**
     * 新增课程收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:add')")
    @Log(title = "课程收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Favorites favorites)
    {
        return toAjax(favoritesService.insertFavorites(favorites));
    }

    /**
     * 修改课程收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:edit')")
    @Log(title = "课程收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Favorites favorites)
    {
        return toAjax(favoritesService.updateFavorites(favorites));
    }

    /**
     * 删除课程收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:remove')")
    @Log(title = "课程收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(favoritesService.deleteFavoritesByIds(ids));
    }
}
