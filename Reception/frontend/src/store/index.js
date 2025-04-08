import { createStore } from 'vuex';
import axios from 'axios';

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
        // 登录
        async login({ commit }, { username, password }) {
            try {
                console.log('Vuex login action 开始执行:', { username, password });
                
                const response = await axios.post('/api/auth/login', {
                    username,
                    password
                });
                
                console.log('登录响应:', response.data);
                
                if (response.data && response.data.token) {
                    // 保存token到localStorage
                    localStorage.setItem('token', response.data.token);
                    
                    // 设置axios默认headers
                    axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
                    
                    // 提交到vuex
                    commit('SET_TOKEN', response.data.token);
                    
                    // 如果有用户信息，也保存
                    if (response.data.user) {
                        commit('SET_USER', response.data.user);
                    }
                    
                    return { success: true, message: '登录成功' };
                } else {
                    console.error('登录失败:', response.data.message);
                    return { 
                        success: false, 
                        message: response.data.message || '登录失败'
                    };
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
                if (response.data.success) {
                    commit('SET_USER', response.data.data);
                    return response.data.data;
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
        }
    }
});
