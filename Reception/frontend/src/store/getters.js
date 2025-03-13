// store/getters.js
export default {
    isAuthenticated: state => !!state.token,  // 判断用户是否已登录
    getUser: state => state.user,  // 获取用户信息
};
