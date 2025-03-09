import request from '@/utils/request'

// 获取系统设置
export function getSystemSettings() {
  return request({
    url: '/sen/system/settings',
    method: 'get'
  })
}

// 更新系统设置
export function updateSystemSettings(data) {
  return request({
    url: '/sen/system/settings',
    method: 'put',
    data: data
  })
}

// 获取公告列表
export function listAnnouncements() {
  return request({
    url: '/sen/system/announcements',
    method: 'get'
  })
}

// 获取公告详情
export function getAnnouncement(id) {
  return request({
    url: '/sen/system/announcements/' + id,
    method: 'get'
  })
}

// 新增公告
export function addAnnouncement(data) {
  return request({
    url: '/sen/system/announcements',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateAnnouncement(data) {
  return request({
    url: '/sen/system/announcements',
    method: 'put',
    data: data
  })
}

// 删除公告
export function deleteAnnouncement(id) {
  return request({
    url: '/sen/system/announcements/' + id,
    method: 'delete'
  })
} 