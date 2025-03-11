package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.Favorites;

import java.util.List;

/**
 * 课程收藏Service接口
 * 
 * @author sen
 * @date 2025-03-11
 */
public interface IFavoritesService 
{
    /**
     * 查询课程收藏
     * 
     * @param id 课程收藏主键
     * @return 课程收藏
     */
    public Favorites selectFavoritesById(Long id);

    /**
     * 查询课程收藏列表
     * 
     * @param favorites 课程收藏
     * @return 课程收藏集合
     */
    public List<Favorites> selectFavoritesList(Favorites favorites);

    /**
     * 新增课程收藏
     * 
     * @param favorites 课程收藏
     * @return 结果
     */
    public int insertFavorites(Favorites favorites);

    /**
     * 修改课程收藏
     * 
     * @param favorites 课程收藏
     * @return 结果
     */
    public int updateFavorites(Favorites favorites);

    /**
     * 批量删除课程收藏
     * 
     * @param ids 需要删除的课程收藏主键集合
     * @return 结果
     */
    public int deleteFavoritesByIds(Long[] ids);

    /**
     * 删除课程收藏信息
     * 
     * @param id 课程收藏主键
     * @return 结果
     */
    public int deleteFavoritesById(Long id);
}
