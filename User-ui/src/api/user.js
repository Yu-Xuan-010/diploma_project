import request from '@/utils/request'

// 获取用户个人资料
export function getUserProfile() {
  return request({
    url: '/user/profile',
    method: 'get'
  })
}

// 更新用户个人资料
export function updateUserProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data: data
  })
}

// 上传用户头像
export function uploadUserAvatar(data) {
  return request({
    url: '/user/avatar',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 修改用户密码
export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data: data
  })
}

// 获取用户学习成就
export function getUserAchievements() {
  return request({
    url: '/user/achievements',
    method: 'get'
  })
}

// 获取用户学习统计数据
export function getLearningStats() {
  return request({
    url: '/user/learning/stats',
    method: 'get'
  })
}

// 获取用户学习时间分布
export function getLearningTimeDistribution() {
  return request({
    url: '/user/learning/time-distribution',
    method: 'get'
  })
}

// 获取用户积分历史
export function getPointsHistory(query) {
  return request({
    url: '/user/points/history',
    method: 'get',
    params: query
  })
}

// 获取用户课程兴趣标签
export function getInterestTags() {
  return request({
    url: '/user/interests',
    method: 'get'
  })
}

// 更新用户课程兴趣标签
export function updateInterestTags(data) {
  return request({
    url: '/user/interests',
    method: 'put',
    data: data
  })
}
