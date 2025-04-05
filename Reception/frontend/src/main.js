import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './store';
import axios from 'axios';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

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

// 获取存储的 token
const token = localStorage.getItem('token');
// 如果 token 存在，则在全局请求头中添加
if (token) {
    axios.defaults.headers['Authorization'] = `Bearer ${token}`;
}

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus).use(store).use(router).mount('#app');

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('全局错误:', err)
  console.error('错误信息:', info)
}

// 导出 app 实例（用于调试）
window.app = app
