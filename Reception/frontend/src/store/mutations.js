// store/mutations.js
export default {
    setUser(state, userData) {
        state.user = userData;
    },
    setToken(state, token) {
        state.token = token;
    },
    clearUser(state) {
        state.user = null;
    },
    clearToken(state) {
        state.token = null;
    },
};
