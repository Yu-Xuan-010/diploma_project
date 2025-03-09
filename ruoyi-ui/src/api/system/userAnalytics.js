import request from '@/utils/request'

// 获取用户数据分析
export function getAnalyticsData() {
  return request({
    url: '/userManage/userlist/analytics/data',
    method: 'get'
  })
} 