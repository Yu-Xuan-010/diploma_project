import request from '@/utils/request'

// 获取讨论列表
export function getDiscussionList(params) {
  return request({
    url: '/api/discussions',
    method: 'get',
    params
  })
}

// 获取讨论详情
export function getDiscussionDetail(id) {
  return request({
    url: `/api/discussions/${id}`,
    method: 'get'
  })
}

// 创建讨论
export function createDiscussion(data) {
  return request({
    url: '/api/discussions',
    method: 'post',
    data
  })
}

// 回复讨论
export function replyDiscussion(id, data) {
  return request({
    url: `/api/discussions/${id}/replies`,
    method: 'post',
    data
  })
}

// 删除讨论
export function deleteDiscussion(id) {
  return request({
    url: `/api/discussions/${id}`,
    method: 'delete'
  })
}

// 获取通知列表
export function getNotifications(params) {
  return request({
    url: '/api/notifications',
    method: 'get',
    params
  })
}

// 标记通知为已读
export function readNotification(id) {
  return request({
    url: `/api/notifications/${id}/read`,
    method: 'put'
  })
}

// 获取课程评论
export function getCourseComments(courseId, params) {
  return request({
    url: `/api/courses/${courseId}/comments`,
    method: 'get',
    params
  })
}

// 创建课程评论
export function createCourseComment(courseId, data) {
  return request({
    url: `/api/courses/${courseId}/comments`,
    method: 'post',
    data
  })
}

// 更新课程评论
export function updateCourseComment(courseId, commentId, data) {
  return request({
    url: `/api/courses/${courseId}/comments/${commentId}`,
    method: 'put',
    data
  })
}

// 删除课程评论
export function deleteCourseComment(courseId, commentId) {
  return request({
    url: `/api/courses/${courseId}/comments/${commentId}`,
    method: 'delete'
  })
}

// 点赞课程评论
export function likeCourseComment(courseId, commentId) {
  return request({
    url: `/api/courses/${courseId}/comments/${commentId}/like`,
    method: 'post'
  })
}

// 获取热门讨论
export function getHotDiscussions() {
  return request({
    url: '/api/discussions/hot',
    method: 'get'
  })
}

// 点赞讨论
export function likeDiscussion(id) {
  return request({
    url: `/api/discussions/${id}/like`,
    method: 'post'
  })
}

// 获取我的参与
export function getMyParticipations(params) {
  return request({
    url: '/api/discussions/participations',
    method: 'get',
    params
  })
}

// 获取互动统计
export function getInteractionStats() {
  return request({
    url: '/api/interactions/stats',
    method: 'get'
  })
}

// 创建反馈
export function createFeedback(data) {
  return request({
    url: '/api/feedback',
    method: 'post',
    data
  })
}

// 获取反馈列表
export function getFeedbackList(params) {
  return request({
    url: '/api/feedback',
    method: 'get',
    params
  })
}

// 获取反馈详情
export function getFeedbackDetail(id) {
  return request({
    url: `/api/feedback/${id}`,
    method: 'get'
  })
}

// 回复反馈
export function replyFeedback(id, data) {
  return request({
    url: `/api/feedback/${id}/reply`,
    method: 'post',
    data
  })
}

// 更新反馈状态
export function updateFeedbackStatus(id, status) {
  return request({
    url: `/api/feedback/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 举报内容
export function reportContent(data) {
  return request({
    url: '/api/reports',
    method: 'post',
    data
  })
}

// 创建回复
export function createReply(data) {
  return request({
    url: '/api/discussions/replies',
    method: 'post',
    data
  })
}

// 标记所有通知为已读
export function readAllNotifications() {
  return request({
    url: '/api/notifications/read-all',
    method: 'put'
  })
}
