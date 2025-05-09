import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/index.vue';
import Login from '../views/login/index.vue';
import Register from '../views/register/index.vue';
import Layout from '../layout/components/AppHeader.vue';
import store from '../store';

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
    component: () => import('../views/profile/index.vue'),
    meta: {
      requiresAuth: true,
      title: '个人中心'
    }
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
  {
    path: '/course',
    name: 'CourseList',
    component: () => import('../views/course/index.vue'),
    meta: { title: '课程列表' }
  },
  {
    path: '/course/:courseId',
    name: 'CourseDetail',
    component: () => import('../views/course/detail.vue'),
    meta: { title: '课程详情' }
  },
  {
    path: '/course/:courseId/lesson/:lessonId',
    name: 'LessonPlayer',
    component: () => import('../views/course/lesson.vue'),
    meta: { 
      title: '课时学习',
      keepAlive: false
    },
    props: true
  },
    {
        path: '/recommend',
        name: 'CourseRecommend',
        component: () => import('../views/CourseRecommend.vue'),  // 动态加载组件
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

// 路由守卫
router.beforeEach((to, from, next) => {
    console.log('路由守卫 - 目标路由:', to.path);
    console.log('路由守卫 - 当前状态:', {
        token: !!localStorage.getItem('token'),
        isAuthenticated: store.getters.isAuthenticated
    });
    
    // 判断目标路由是否需要认证
    const token = localStorage.getItem('token');
    const isAuthenticated = store.getters.isAuthenticated;
    
    if (to.meta.requiresAuth) {
        if (!token || !isAuthenticated) {
            console.log('需要认证但未登录，重定向到登录页');
            // 保存重定向地址
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            });
        } else {
            console.log('已认证，允许访问');
            next();
        }
    } else if (to.path === '/login' && token && isAuthenticated) {
        console.log('已登录用户访问登录页，重定向到首页');
        // 如果已登录且访问登录页，重定向到首页
        next('/home');
    } else {
        console.log('不需要认证或未登录访问登录页，允许访问');
        next();
    }
});

// 添加导航失败处理
router.onError((error) => {
  console.error('路由错误:', error);
});

export default router;
