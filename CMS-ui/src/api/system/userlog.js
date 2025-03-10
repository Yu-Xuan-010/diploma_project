import request from '@/utils/request'

// 查询用户日志列表
export function listUserlog(query) {
  return request({
    url: '/system/userlog/list',
    method: 'get',
    params: query
  })
}

// 查询用户日志详细
export function getUserlog(id) {
  return request({
    url: '/system/userlog/' + id,
    method: 'get'
  })
}

// 新增用户日志
export function addUserlog(data) {
  return request({
    url: '/system/userlog',
    method: 'post',
    data: data
  })
}

// 修改用户日志
export function updateUserlog(data) {
  return request({
    url: '/system/userlog',
    method: 'put',
    data: data
  })
}

// 删除用户日志
export function delUserlog(id) {
  return request({
    url: '/system/userlog/' + id,
    method: 'delete'
  })
}
