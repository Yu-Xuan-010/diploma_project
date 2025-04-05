import { createStore } from 'vuex';

export default createStore({
    state: {
        userInfo: JSON.parse(localStorage.getItem('userInfo')) || {},
        token: localStorage.getItem('token') || ''
    },
    getters: {
        userInfo: state => state.userInfo,
        token: state => state.token,
        isAuthenticated: state => !!state.token
    },
    mutations: {
        SET_USER_INFO(state, userInfo) {
            state.userInfo = userInfo;
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
        },
        SET_TOKEN(state, token) {
            state.token = token;
            localStorage.setItem('token', token);
        },
        CLEAR_USER_STATE(state) {
            state.userInfo = {};
            state.token = '';
            localStorage.removeItem('userInfo');
            localStorage.removeItem('token');
        }
    },
    actions: {
        // 登录成功后调用
        loginSuccess({ commit }, { token, userInfo }) {
            commit('SET_TOKEN', token);
            commit('SET_USER_INFO', userInfo);
        },
        // 登出
        logout({ commit }) {
            commit('CLEAR_USER_STATE');
        },
        // 更新用户信息
        updateUserInfo({ commit }, userInfo) {
            commit('SET_USER_INFO', userInfo);
        }
    }
});
