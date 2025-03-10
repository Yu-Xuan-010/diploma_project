import request from '@/utils/request'

// 查询课程列表列表
export function listCourse(query) {
  return request({
    url: '/system/course/list',
    method: 'get',
    params: query
  })
}

// 查询课程列表详细
export function getCourse(id) {
  return request({
    url: '/system/course/' + id,
    method: 'get'
  })
}

// 新增课程列表
export function addCourse(data) {
  return request({
    url: '/system/course',
    method: 'post',
    data: data
  })
}

// 修改课程列表
export function updateCourse(data) {
  return request({
    url: '/system/course',
    method: 'put',
    data: data
  })
}

// 删除课程列表
export function delCourse(id) {
  return request({
    url: '/system/course/' + id,
    method: 'delete'
  })
}
