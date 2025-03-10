import request from '@/utils/request'

// 查询课时列表列表
export function listLesson(query) {
  return request({
    url: '/system/lesson/list',
    method: 'get',
    params: query
  })
}

// 查询课时列表详细
export function getLesson(id) {
  return request({
    url: '/system/lesson/' + id,
    method: 'get'
  })
}

// 新增课时列表
export function addLesson(data) {
  return request({
    url: '/system/lesson',
    method: 'post',
    data: data
  })
}

// 修改课时列表
export function updateLesson(data) {
  return request({
    url: '/system/lesson',
    method: 'put',
    data: data
  })
}

// 删除课时列表
export function delLesson(id) {
  return request({
    url: '/system/lesson/' + id,
    method: 'delete'
  })
}
