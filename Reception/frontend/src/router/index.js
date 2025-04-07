import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/index.vue';
import Login from '../views/login/index.vue';
import Register from '../views/register/index.vue';
import Layout from '../layout/components/AppHeader.vue';

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: { 
      requiresAuth: false,
      title: '登录'
    }
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
    meta: { 
      requiresAuth: true,
      title: '首页'
    }
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    meta: { 
      requiresAuth: false,
      title: '注册'
    }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/profile/index.vue')
  },
  {
    path: '/admin',
    component: Layout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'teacher-applications',
        name: 'TeacherApplications',
        component: () => import('../views/admin/teacher-applications/index.vue'),
        meta: { title: '教师申请管理', icon: 'user' }
      }
    ]
  },
  // 添加通配符路由
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  scrollBehavior: () => ({ top: 0 })
});

// 路由守卫，检查是否需要认证
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 在线学习平台` : '在线学习平台';
  
  const isAuthenticated = localStorage.getItem('token');
  
  // 如果需要认证但未登录，跳转到登录页面
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ 
      name: 'login',
      query: { redirect: to.fullPath }
    });
    return;
  }
  
  // 如果已登录且访问登录页，重定向到首页
  if (isAuthenticated && to.name === 'login') {
    next({ name: 'home' });
    return;
  }
  
  next();
});

// 添加导航失败处理
router.onError((error) => {
  console.error('路由错误:', error);
});

export default router;
