import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/index.vue';
import Login from '../views/login/index.vue';

// 路由配置
const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    meta: { requiresAuth: true },  // 需要认证
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: { requiresAuth: false }
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
