import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './store';
import axios from 'axios';

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

app.use(ElementPlus).use(store).use(router).mount('#app');
