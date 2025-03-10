import request from '@/utils/request'

// 查询课程推荐列表
export function listRecommendation(query) {
  return request({
    url: '/system/recommendation/list',
    method: 'get',
    params: query
  })
}

// 查询课程推荐详细
export function getRecommendation(id) {
  return request({
    url: '/system/recommendation/' + id,
    method: 'get'
  })
}

// 新增课程推荐
export function addRecommendation(data) {
  return request({
    url: '/system/recommendation',
    method: 'post',
    data: data
  })
}

// 修改课程推荐
export function updateRecommendation(data) {
  return request({
    url: '/system/recommendation',
    method: 'put',
    data: data
  })
}

// 删除课程推荐
export function delRecommendation(id) {
  return request({
    url: '/system/recommendation/' + id,
    method: 'delete'
  })
}
