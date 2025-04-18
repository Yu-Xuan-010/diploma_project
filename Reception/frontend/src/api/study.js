import request from '@/utils/request'

// 获取用户学习记录
export function getStudyRecords() {
  return request({
    url: '/api/study/records',
    method: 'get'
  })
}

// 获取最近的学习记录
export function getRecentStudyRecords(limit = 5) {
  return request({
    url: '/api/study/records/recent',
    method: 'get',
    params: { limit }
  })
}

// 更新学习进度
export function updateStudyProgress(lessonId, duration) {
  return request({
    url: '/api/study/progress',
    method: 'post',
    data: {
      lessonId,
      duration
    }
  })
}

// 检查是否已学习
export function checkStudyStatus(lessonId) {
  return request({
    url: '/api/study/status',
    method: 'get',
    params: { lessonId }
  })
}

// 获取总学习时长
export function getTotalStudyTime(lessonId) {
  return request({
    url: '/api/study/total-time',
    method: 'get',
    params: { lessonId }
  })
} 