import Vue from 'vue'
import VueRouter from 'vue-router'
import { getToken } from '@/utils/auth'
import store from '@/store'

/* Layout */
import Layout from '@/layout'

Vue.use(VueRouter)

// 公共路由（不需要登录就可以访问）
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
    component: () => import('@/views/forget-password/index'),
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
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index'),
        meta: { title: '首页', icon: 'dashboard', requireAuth: true }
      }
    ]
  }
]

// 需要登录才能访问的路由
export const asyncRoutes = [
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/course/list'),
        meta: { title: '课程列表', icon: 'education', requireAuth: true }
      },
      {
        path: 'detail/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/detail'),
        meta: { title: '课程详情', requireAuth: true },
        hidden: true
      }
    ]
  },
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
  {
    path: '/profile',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index'),
        meta: { title: '个人中心', icon: 'user', requireAuth: true }
      }
    ]
  },
  {
    path: '*',
    component: () => import('@/views/error/404'),
    hidden: true
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

const createRouter = () => new VueRouter({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes.concat(asyncRoutes)
})

const router = createRouter()

// 重置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

// 全局路由守卫
router.beforeEach(async (to, from, next) => {
  // 获取token
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登录，重定向到首页
      next({ path: '/' })
    } else {
      // 确认用户是否已获取用户信息
      const hasUserInfo = store.getters.userInfo && Object.keys(store.getters.userInfo).length > 0
      if (hasUserInfo) {
        next()
      } else {
        try {
          // 获取用户信息
          await store.dispatch('user/getUserInfo')
          
          // 动态添加可访问路由
          const accessRoutes = await store.dispatch('permission/generateRoutes', asyncRoutes)
          router.addRoutes(accessRoutes)

          // 确保addRoutes完整的hack方法
          next({ ...to, replace: true })
        } catch (error) {
          // 移除token并跳转登录页
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    if (['/login', '/register', '/forget-password'].indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      // 其他没有访问权限的页面将被重定向到登录页面
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
