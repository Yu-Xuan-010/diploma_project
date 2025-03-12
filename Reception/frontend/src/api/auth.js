import axios from 'axios'

const API_BASE_URL = 'http://192.168.151.141:5000/api/auth' // 后端地址

// 登录
export const login = async(userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/login`, userData)
    return response.data // 返回登录结果（如 token）
  } catch (error) {
    throw new Error(error.response?.data?.message || '登录失败')
  }
}

// 注册
export const register = async(userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/register`, userData)
    return response.data // 返回注册结果
  } catch (error) {
    throw new Error(error.response?.data?.message || '注册失败')
  }
}

// 发送重置密码验证码
export const sendResetCode = async(email) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/send-reset-code`, { email })
    return response.data // 返回发送验证码结果
  } catch (error) {
    throw new Error(error.response?.data?.message || '验证码发送失败')
  }
}

// 重置密码
export const resetPassword = async(resetData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/reset-password`, resetData)
    return response.data // 返回重置密码结果
  } catch (error) {
    throw new Error(error.response?.data?.message || '密码重置失败')
  }
}
