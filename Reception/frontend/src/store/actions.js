
// store/actions.js
import axios from 'axios';  // 引入 axios 用于 HTTP 请求

export default {
    // 登录操作
    async login({ commit }, userData) {
        try {
            const response = await axios.post('/api/login', userData);
            if (response.data.code === 200) {
                commit('setUser', response.data.data); // 设置用户数据
                commit('setToken', response.data.data.token); // 设置 token
                return response.data;  // 返回成功结果
            } else {
                throw new Error(response.data.message || '登录失败');
            }
        } catch (error) {
            throw new Error(error.response?.data?.message || '登录请求失败');
        }
    },

    // 获取用户信息
    async fetchUserData({ commit }) {
        try {
            const response = await axios.get('/api/user');
            if (response.data.code === 200) {
                commit('setUser', response.data.data);
                return response.data;  // 返回用户数据
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
};
