import { createStore } from 'vuex';

const store = createStore({
    state: {
        user: null
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        }
    },
    actions: {
        login({ commit }, userData) {
            commit('setUser', userData);
        }
    }
});

export default store;
