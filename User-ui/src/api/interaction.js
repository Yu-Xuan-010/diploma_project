import request from '@/utils/request'

// 讨论相关接口
export function getDiscussionList(query) {
  return request({
    url: '/discussion/list',
    method: 'get',
    params: query
  })
}

export function getDiscussionDetail(id) {
  return request({
    url: `/discussion/${id}`,
    method: 'get'
  })
}

export function createDiscussion(data) {
  return request({
    url: '/discussion',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function deleteDiscussion(id) {
  return request({
    url: `/discussion/${id}`,
    method: 'delete'
  })
}

export function likeDiscussion(id) {
  return request({
    url: `/discussion/${id}/like`,
    method: 'post'
  })
}

export function createReply(data) {
  return request({
    url: '/discussion/reply',
    method: 'post',
    data: data
  })
}

export function deleteReply(id) {
  return request({
    url: `/discussion/reply/${id}`,
    method: 'delete'
  })
}

export function getHotDiscussions() {
  return request({
    url: '/discussion/hot',
    method: 'get'
  })
}

export function getMyParticipations() {
  return request({
    url: '/discussion/my-participations',
    method: 'get'
  })
}

// 反馈相关接口
export function createFeedback(data) {
  return request({
    url: '/feedback',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function getFeedbackList(query) {
  return request({
    url: '/feedback/list',
    method: 'get',
    params: query
  })
}

export function getFeedbackDetail(id) {
  return request({
    url: `/feedback/${id}`,
    method: 'get'
  })
}

export function updateFeedbackStatus(id, data) {
  return request({
    url: `/feedback/${id}/status`,
    method: 'put',
    data: data
  })
}

export function replyFeedback(id, data) {
  return request({
    url: `/feedback/${id}/reply`,
    method: 'post',
    data: data
  })
}

// 课程评价相关接口
export function getCourseComments(query) {
  return request({
    url: '/course/comments',
    method: 'get',
    params: query
  })
}

export function createCourseComment(data) {
  return request({
    url: '/course/comment',
    method: 'post',
    data: data
  })
}

export function updateCourseComment(id, data) {
  return request({
    url: `/course/comment/${id}`,
    method: 'put',
    data: data
  })
}

export function deleteCourseComment(id) {
  return request({
    url: `/course/comment/${id}`,
    method: 'delete'
  })
}

export function likeCourseComment(id) {
  return request({
    url: `/course/comment/${id}/like`,
    method: 'post'
  })
}

// 统计相关接口
export function getInteractionStats() {
  return request({
    url: '/interaction/stats',
    method: 'get'
  })
}

// 举报相关接口
export function reportContent(data) {
  return request({
    url: '/interaction/report',
    method: 'post',
    data: data
  })
}
