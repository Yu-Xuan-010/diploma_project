// store/actions.js
import axios from 'axios';  // 引入 axios 用于 HTTP 请求
import { login, getProfile } from '@/api/user'

export default {
    // 登录操作
    async login({ commit }, userData) {
        try {
            const response = await axios.post('/api/auth/login', userData);
            if (response.data.success) {
                const { token, user } = response.data.data;
                commit('setUser', user);
                commit('setToken', token);
                return response.data;  // 返回成功结果
            } else {
                throw new Error(response.data.message || '登录失败');
            }
        } catch (error) {
            throw new Error(error.response?.data?.message || '登录请求失败');
        }
    },

    // 获取用户信息
    async fetchUserData({ commit, state }) {
        try {
            const response = await axios.get('/api/user/profile', {
                headers: {
                    'Authorization': `Bearer ${state.token}`
                }
            });
            if (response.data.success) {
                commit('setUser', response.data.data);
                return response.data.data;  // 返回用户数据
            } else {
                throw new Error(response.data.message || '获取用户信息失败');
            }
        } catch (error) {
            throw new Error(error.response?.data?.message || '请求失败');
        }
    },

    // 退出登录
    async logout({ commit }) {
        try {
            await axios.post('/api/logout');  // 假设有登出 API
            commit('clearUser');  // 清空用户数据
            commit('clearToken');  // 清空 token
        } catch (error) {
            console.error('登出失败:', error);
        }
    },

    // 其他异步操作...

    async getUserProfile({ commit }) {
        try {
            console.log('开始获取用户信息')
            const token = localStorage.getItem('token')
            if (!token) {
                console.log('未找到token')
                return null
            }
            
            const response = await getProfile()
            console.log('获取用户信息响应:', response)
            
            if (response.data.success) {
                const userData = response.data.data
                console.log('用户信息:', userData)
                commit('SET_USER', userData)
                return userData
            } else {
                console.error('获取用户信息失败:', response.data.message)
                return null
            }
        } catch (error) {
            console.error('获取用户信息出错:', error)
            return null
        }
    }
};
