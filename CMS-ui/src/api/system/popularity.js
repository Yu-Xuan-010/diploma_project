import request from '@/utils/request'

// 获取课程热度数据
export function getCoursePopularity() {
  return request({
    url: '/sen/popularity/list',
    method: 'get'
  })
} 