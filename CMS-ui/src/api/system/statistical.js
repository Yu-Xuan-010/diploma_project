import request from '@/utils/request'

// 获取课程统计数据
export function getCourseStatistics() {
  return request({
    url: '/sen/statistics/course',
    method: 'get'
  })
}

