import request from '../utils/request'

// 获取用户学习记录
export function getStudyRecords() {
  return request({
    url: '/api/study/records',
    method: 'get'
  }).then(response => {
    if (!response || !response.data) {
      console.error('No data received from API')
      return { data: [] }  // 返回一个空数组，防止出现未定义错误
    }
    console.log('Received data:', response.data)
    return response.data  // 这里确保返回的数据是正确的
  }).catch(error => {
    console.error('API Request failed:', error)
    throw error  // 抛出错误，以便外部处理
  })
}





// 获取最近的学习记录
export function getRecentStudyRecords(limit = 5) {
  return request({
    url: '/api/study/records/recent',
    method: 'get',
    params: { limit }
  }).then(response => {
    if (!response || !response.data) {
      return { data: { code: 200, data: [] } }
    }
    return response
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