import {
  getCourseList,
  getCourseDetail,
  getCourseCategories,
  getFavoriteCourses,
  addToFavorites,
  removeFromFavorites
} from '@/api/course'

const state = {
  courseList: [],
  currentCourse: null,
  categories: [],
  favorites: [],
  total: 0,
  loading: false
}

const mutations = {
  SET_COURSE_LIST: (state, courses) => {
    state.courseList = courses
  },
  SET_CURRENT_COURSE: (state, course) => {
    state.currentCourse = course
  },
  SET_CATEGORIES: (state, categories) => {
    state.categories = categories
  },
  SET_FAVORITES: (state, favorites) => {
    state.favorites = favorites
  },
  SET_TOTAL: (state, total) => {
    state.total = total
  },
  SET_LOADING: (state, loading) => {
    state.loading = loading
  },
  ADD_TO_FAVORITES: (state, course) => {
    state.favorites.unshift(course)
  },
  REMOVE_FROM_FAVORITES: (state, courseId) => {
    const index = state.favorites.findIndex(item => item.id === courseId)
    if (index !== -1) {
      state.favorites.splice(index, 1)
    }
  }
}

const actions = {
  // 获取课程列表
  GetCourseList({ commit }, query) {
    commit('SET_LOADING', true)
    return new Promise((resolve, reject) => {
      getCourseList(query).then(response => {
        const { rows, total } = response.data
        commit('SET_COURSE_LIST', rows)
        commit('SET_TOTAL', total)
        commit('SET_LOADING', false)
        resolve()
      }).catch(error => {
        commit('SET_LOADING', false)
        reject(error)
      })
    })
  },

  // 获取课程详情
  GetCourseDetail({ commit }, courseId) {
    return new Promise((resolve, reject) => {
      getCourseDetail(courseId).then(response => {
        const { data } = response
        commit('SET_CURRENT_COURSE', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取课程分类
  GetCategories({ commit }) {
    return new Promise((resolve, reject) => {
      getCourseCategories().then(response => {
        const { data } = response
        commit('SET_CATEGORIES', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取收藏课程列表
  GetFavoriteCourses({ commit }, query) {
    return new Promise((resolve, reject) => {
      getFavoriteCourses(query).then(response => {
        const { data } = response
        commit('SET_FAVORITES', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 添加收藏
  AddToFavorites({ commit }, courseId) {
    return new Promise((resolve, reject) => {
      addToFavorites(courseId).then(response => {
        const { data } = response
        commit('ADD_TO_FAVORITES', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 取消收藏
  RemoveFromFavorites({ commit }, courseId) {
    return new Promise((resolve, reject) => {
      removeFromFavorites(courseId).then(() => {
        commit('REMOVE_FROM_FAVORITES', courseId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
