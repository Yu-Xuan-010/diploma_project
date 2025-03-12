import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 用户注册
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/profile',
    method: 'get'
  })
}

// 修改用户信息
export function updateUserInfo(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

// 修改密码
export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data
  })
}

// 发送验证码
export function sendVerifyCode(email) {
  return request({
    url: '/user/verify-code',
    method: 'post',
    data: { email }
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/user/reset-password',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}