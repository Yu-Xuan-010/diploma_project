import { getToken, setToken, removeToken } from '@/utils/auth'
import { getUserProfile, updateUserProfile, updatePassword } from '@/api/user'
import login from '@/views/login/index.vue'

const state = {
  token: getToken(),
  userInfo: {},
  roles: [],
  permissions: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  }
}

const actions = {
  // 用户登录
  Login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        const { token } = response.data
        commit('SET_TOKEN', token)
        setToken(token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取用户信息
  GetUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserProfile().then(response => {
        const { data } = response
        commit('SET_USER_INFO', data)
        commit('SET_ROLES', data.roles)
        commit('SET_PERMISSIONS', data.permissions)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 更新用户信息
  UpdateUserInfo({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      updateUserProfile(userInfo).then(response => {
        const { data } = response
        commit('SET_USER_INFO', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 修改密码
  UpdatePassword({ commit }, passwordInfo) {
    return new Promise((resolve, reject) => {
      updatePassword(passwordInfo).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 退出登录
  LogOut({ commit }) {
    return new Promise((resolve, reject) => {
      try {
        commit('SET_TOKEN', '')
        commit('SET_USER_INFO', {})
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        removeToken()
        resolve()
      } catch (error) {
        reject(error)
      }
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
