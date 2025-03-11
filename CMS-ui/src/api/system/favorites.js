import request from '@/utils/request'

// 查询课程收藏列表
export function listFavorites(query) {
  return request({
    url: '/system/favorites/list',
    method: 'get',
    params: query
  })
}

// 查询课程收藏详细
export function getFavorites(id) {
  return request({
    url: '/system/favorites/' + id,
    method: 'get'
  })
}

// 新增课程收藏
export function addFavorites(data) {
  return request({
    url: '/system/favorites',
    method: 'post',
    data: data
  })
}

// 修改课程收藏
export function updateFavorites(data) {
  return request({
    url: '/system/favorites',
    method: 'put',
    data: data
  })
}

// 删除课程收藏
export function delFavorites(id) {
  return request({
    url: '/system/favorites/' + id,
    method: 'delete'
  })
}
