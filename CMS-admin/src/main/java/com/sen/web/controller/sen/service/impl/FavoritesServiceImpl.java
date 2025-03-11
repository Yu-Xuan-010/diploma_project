package com.sen.web.controller.sen.service.impl;

import java.util.List;

import com.sen.web.controller.sen.domain.Favorites;
import com.sen.web.controller.sen.mapper.FavoritesMapper;
import com.sen.web.controller.sen.service.IFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 课程收藏Service业务层处理
 * 
 * @author sen
 * @date 2025-03-11
 */
@Service
public class FavoritesServiceImpl implements IFavoritesService
{
    @Autowired
    private FavoritesMapper favoritesMapper;

    /**
     * 查询课程收藏
     * 
     * @param id 课程收藏主键
     * @return 课程收藏
     */
    @Override
    public Favorites selectFavoritesById(Long id)
    {
        return favoritesMapper.selectFavoritesById(id);
    }

    /**
     * 查询课程收藏列表
     * 
     * @param favorites 课程收藏
     * @return 课程收藏
     */
    @Override
    public List<Favorites> selectFavoritesList(Favorites favorites)
    {
        return favoritesMapper.selectFavoritesList(favorites);
    }

    /**
     * 新增课程收藏
     * 
     * @param favorites 课程收藏
     * @return 结果
     */
    @Override
    public int insertFavorites(Favorites favorites)
    {
        return favoritesMapper.insertFavorites(favorites);
    }

    /**
     * 修改课程收藏
     * 
     * @param favorites 课程收藏
     * @return 结果
     */
    @Override
    public int updateFavorites(Favorites favorites)
    {
        return favoritesMapper.updateFavorites(favorites);
    }

    /**
     * 批量删除课程收藏
     * 
     * @param ids 需要删除的课程收藏主键
     * @return 结果
     */
    @Override
    public int deleteFavoritesByIds(Long[] ids)
    {
        return favoritesMapper.deleteFavoritesByIds(ids);
    }

    /**
     * 删除课程收藏信息
     * 
     * @param id 课程收藏主键
     * @return 结果
     */
    @Override
    public int deleteFavoritesById(Long id)
    {
        return favoritesMapper.deleteFavoritesById(id);
    }
}
