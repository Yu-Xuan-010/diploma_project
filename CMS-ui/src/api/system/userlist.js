import request from '@/utils/request'

// 查询用户列表
export function listUserlist(query) {
  return request({
    url: '/system/userlist/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUserlist(id) {
  return request({
    url: '/system/userlist/' + id,
    method: 'get'
  })
}

// 新增用户
export function addUserlist(data) {
  return request({
    url: '/system/userlist',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUserlist(data) {
  return request({
    url: '/system/userlist',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUserlist(id) {
  return request({
    url: '/system/userlist/' + id,
    method: 'delete'
  })
}
