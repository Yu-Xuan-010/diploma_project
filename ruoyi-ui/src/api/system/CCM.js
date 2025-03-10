import request from '@/utils/request'

// 查询课程评论列表
export function list(query) {
  return request({
    url: '/feedback/CCM/list',
    method: 'get',
    params: query
  })
}

// 获取课程评论详细信息
export function getComment(id) {
  return request({
    url: `/feedback/CCM/${id}`,
    method: 'get'
  })
}

// 修改评论状态
export function changeStatus(data) {
  return request({
    url: '/feedback/CCM/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除课程评论
export function remove(id) {
  return request({
    url: `/feedback/CCM/${id}`,
    method: 'delete'
  })
}

// 获取评论回复列表
export function replyList(commentId) {
  return request({
    url: `/feedback/CCM/reply/list/${commentId}`,
    method: 'get'
  })
}

// 新增评论回复
export function addReply(data) {
  return request({
    url: '/feedback/CCM/reply',
    method: 'post',
    data: data
  })
}

// 删除评论回复
export function removeReply(id) {
  return request({
    url: `/feedback/CCM/reply/${id}`,
    method: 'delete'
  })
}
