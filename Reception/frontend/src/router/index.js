import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'
import Login from '@/views/login'
import Register from '@/views/register'
import Profile from '@/views/account/profile'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: Layout,
      children: [
        { path: '/profile', component: Profile }
      ]
    },
    { path: '/login', component: Login },
    { path: '/register', component: Register }
  ]
})