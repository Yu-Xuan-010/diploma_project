import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router/index'

// 创建 axios 实例
const service = axios.create({
  baseURL: '', // 移除 /api 前缀，因为我们在具体的请求中已经包含了
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 直接返回响应数据
    return response.data
  },
  error => {
    console.error('Response error:', error)
    ElMessage({
      message: error.response?.data?.error || error.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    })
    
    // 401: 未登录或token过期
    if (error.response?.status === 401) {
      // 清除本地token
      localStorage.removeItem('token')
      // 跳转到登录页
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

// 封装 GET 请求
export function get(url, params) {
  return service({
    url,
    method: 'get',
    params
  })
}

// 封装 POST 请求
export function post(url, data) {
  return service({
    url,
    method: 'post',
    data
  })
}

// 封装 PUT 请求
export function put(url, data) {
  return service({
    url,
    method: 'put',
    data
  })
}

// 封装 DELETE 请求
export function del(url, params) {
  return service({
    url,
    method: 'delete',
    params
  })
}

export default service