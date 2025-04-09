import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './store';
import axios from 'axios';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn';

const app = createApp(App);

// 路由守卫
router.beforeEach((to, from, next) => {
    // 判断目标路由是否需要认证
    const token = localStorage.getItem('token');  // 获取token
    if (to.meta.requiresAuth && !token) {  // 如果需要认证且没有token
        next({ name: 'login' });  // 跳转到登录页面
    } else {
        next();  // 否则继续执行
    }
});

// 配置 axios 默认值
axios.defaults.baseURL = 'http://localhost:8001'
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json'

// 添加请求拦截器，用于调试
axios.interceptors.request.use(
  config => {
    console.log('发送请求:', {
      method: config.method.toUpperCase(),
      url: config.url,
      baseURL: config.baseURL,
      headers: config.headers,
      data: config.data
    });
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 添加响应拦截器，用于调试
axios.interceptors.response.use(
  response => {
    console.log('收到响应:', {
      status: response.status,
      statusText: response.statusText,
      headers: response.headers,
      data: response.data
    });
    return response;
  },
  error => {
    console.error('响应错误:', {
      message: error.message,
      response: error.response,
      config: error.config
    });
    return Promise.reject(error);
  }
);

// 从本地存储获取 token
const token = localStorage.getItem('token')
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 初始化用户状态
store.dispatch('initializeUserState')

app.use(ElementPlus).use(store).use(router).mount('#app').use(ElementPlus, { locale: zhCn });

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('全局错误:', err)
  console.error('错误信息:', info)
}

// 导出 app 实例（用于调试）
window.app = app
