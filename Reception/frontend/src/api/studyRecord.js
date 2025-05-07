import request from '@/utils/request'

// 创建学习记录
export function createStudyRecord(data) {
  return request({
    url: '/api/study-records',
    method: 'post',
    data
  })
}

// 获取用户的学习记录
export function getUserStudyRecords(userId) {
  return request({
    url: `/api/study-records/user/${userId}`,
    method: 'get'
  })
}

// 获取最近的N条学习记录
export function getRecentStudyRecords(userId, limit = 10) {
  return request({
    url: `/api/study-records/user/${userId}/recent`,
    method: 'get',
    params: { limit }
  })
}

// 获取特定课程的学习记录
export function getCourseStudyRecord(userId, lessonId) {
  return request({
    url: `/api/study-records/user/${userId}/lesson/${lessonId}`,
    method: 'get'
  })
} 