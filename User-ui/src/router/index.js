import Vue from 'vue'
import VueRouter from 'vue-router'

/* Layout */
import Layout from '@/layout'

Vue.use(VueRouter)

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },
  {
    path: '/forget-password',
    component: () => import('@/views/forget-password'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/home/index'),
        name: 'Home',
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  }
]

// 动态路由
export const dynamicRoutes = [
  // 课程学习
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/course/index'),
        name: 'Course',
        meta: { title: '课程中心', icon: 'education' }
      },
      {
        path: 'detail/:id',
        component: () => import('@/views/course/detail'),
        name: 'CourseDetail',
        meta: { title: '课程详情' },
        hidden: true
      }
    ]
  },
  // 个人学习
  {
    path: '/learning',
    component: Layout,
    children: [
      {
        path: 'progress',
        component: () => import('@/views/learning/progress'),
        name: 'Progress',
        meta: { title: '学习进度', icon: 'chart' }
      },
      {
        path: 'plan',
        component: () => import('@/views/learning/plan'),
        name: 'Plan',
        meta: { title: '学习计划', icon: 'calendar' }
      }
    ]
  },
  // 互动与反馈
  {
    path: '/discussion',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/interaction/discussion'),
        name: 'Discussion',
        meta: { title: '讨论区', icon: 'message' }
      }
    ]
  },
  // 账户管理
  {
    path: '/profile',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/account/profile'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 防止连续点击多次路由报错
const routerPush = VueRouter.prototype.push
const routerReplace = VueRouter.prototype.replace
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
VueRouter.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

const routes = constantRoutes.concat(dynamicRoutes)

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
