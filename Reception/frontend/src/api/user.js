import request from '../utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

// 用户注册
export function register(data) {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getProfile() {
  return request({
    url: '/api/user/profile',
    method: 'get'
  })
}

// 更新用户信息
export function updateProfile(data) {
  return request({
    url: '/api/user/profile',
    method: 'put',
    data
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/api/user/reset-password',
    method: 'post',
    data
  })
}

// 发送验证码
export function sendCode(username) {
  return request({
    url: '/api/user/send-code',
    method: 'post',
    params: { username }
  })
} 