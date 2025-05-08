import { createStore } from 'vuex';
import axios from 'axios';
import course from './modules/course';
import lesson from './modules/lesson';
import user from './modules/user';

export default createStore({
    state: {
        token: localStorage.getItem('token') || '',
        userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
    },
    getters: {
        isAuthenticated: state => !!state.token,
        userInfo: state => state.userInfo
    },
    mutations: {
        SET_TOKEN(state, token) {
            state.token = token;
            localStorage.setItem('token', token);
        },
        SET_USER(state, userInfo) {
            state.userInfo = userInfo;
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
        },
        CLEAR_USER(state) {
            state.token = '';
            state.userInfo = {};
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
        }
    },
    actions: {
        // 初始化用户状态
        initializeUserState({ commit }) {
            const token = localStorage.getItem('token');
            const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
            
            if (token) {
                commit('SET_TOKEN', token);
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            }
            
            if (userInfo && Object.keys(userInfo).length > 0) {
                commit('SET_USER', userInfo);
            }
        },
        // 登录
        async login({ commit }, { username, password }) {
            try {
                console.log('Vuex login action 开始执行:', { username, password });

                const response = await axios.post('/api/auth/login', {
                    username,
                    password
                });

                console.log('登录响应:', response.data);

                if (response.data.success && response.data.data) {
                    const { token, username, userId, email, nickname } = response.data.data;
                    localStorage.setItem('currentUserId', userId);
                    // 保存 token 到 localStorage
                    localStorage.setItem('token', token);

                    // 设置 axios 默认 headers
                    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

                    // 提交到 vuex
                    commit('SET_TOKEN', token);
                    commit('SET_USER', { username, userId, email, nickname });

                    return { success: true, message: '登录成功' };
                } else {
                    console.error('登录失败:', response.data.message);
                    return { success: false, message: response.data.message || '登录失败' };
                }
            } catch (error) {
                console.error('登录 action 错误:', error);
                console.error('错误响应:', error.response?.data);
                return {
                    success: false,
                    message: error.response?.data?.message || '登录失败，请重试'
                };
            }
        },
        async getUserProfile({ commit, state }) {
            try {
                if (!state.token) {
                    throw new Error('未登录');
                }
                
                const response = await axios.get('/api/user/profile');
                console.log('获取用户信息响应:', response.data);
                
                if (response.data.success) {
                    const userInfo = response.data.data;
                    console.log('用户信息:', userInfo);
                    commit('SET_USER', userInfo);
                    return userInfo;
                } else {
                    throw new Error(response.data.message || '获取用户信息失败');
                }
            } catch (error) {
                console.error('获取用户信息失败:', error);
                commit('CLEAR_USER');
                throw error;
            }
        },
        logout({ commit }) {
            commit('CLEAR_USER');
            delete axios.defaults.headers.common['Authorization'];
        }
    },
    modules: {
        course,
        lesson,
        user
    }
});
