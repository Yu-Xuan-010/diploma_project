import { createStore } from 'vuex';

export default createStore({
    state: {
        userInfo: {} // 初始化 userInfo 为一个空对象，避免为 undefined
    },
    mutations: {
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo;
        }
    },
    actions: {
        fetchUserInfo({ commit }) {
            // 模拟从 API 获取用户信息
            const userInfo = { username: 'testUser', userName: 'Test User' };
            commit('setUserInfo', userInfo);
        }
    },
    getters: {
        userInfo: (state) => state.userInfo
    }
});
