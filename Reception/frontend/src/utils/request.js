import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router/index'

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8001', // 设置后端服务器地址
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // 允许跨域请求携带凭证
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    console.log('发送请求:', {
      method: config.method.toUpperCase(),
      url: config.url,
      baseURL: config.baseURL,
      headers: config.headers,
      data: config.data
    });
    
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
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
    console.log('收到响应:', {
      status: response.status,
      statusText: response.statusText,
      headers: response.headers,
      data: response.data
    });
    return response.data
  },
  error => {
    console.error('Response error:', {
      message: error.message,
      response: error.response,
      config: error.config
    });
    
    if (error.response?.status === 401) {
      // 清除本地token
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 跳转到登录页
      router.push('/login')
    } else {
      ElMessage({
        message: error.response?.data?.error || error.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
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