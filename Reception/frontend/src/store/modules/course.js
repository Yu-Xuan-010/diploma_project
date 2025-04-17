import axios from 'axios'

const state = {
  courseDetail: null,
  courseList: [],
  courseComments: []
}

const mutations = {
  SET_COURSE_DETAIL(state, course) {
    state.courseDetail = course
  },
  SET_COURSE_LIST(state, courses) {
    state.courseList = courses
  },
  SET_COURSE_COMMENTS(state, comments) {
    state.courseComments = comments
  }
}

const actions = {
  // 获取课程详情
  async getDetail({ commit }, courseId) {
    try {
      const response = await axios.get(`/api/courses/${courseId}`)
      if (response.data.success) {
        commit('SET_COURSE_DETAIL', response.data.data)
        return response.data.data
      } else {
        throw new Error(response.data.message || '获取课程详情失败')
      }
    } catch (error) {
      console.error('获取课程详情失败:', error)
      throw error
    }
  },

  // 获取课程列表
  async getList({ commit }, params) {
    try {
      const response = await axios.get('/api/courses', { params })
      if (response.data.success) {
        commit('SET_COURSE_LIST', response.data.data)
        return response.data.data
      }
      throw new Error(response.data.message || '获取课程列表失败')
    } catch (error) {
      console.error('获取课程列表失败:', error)
      throw error
    }
  },

  // 获取课程评论
  async getComments({ commit }, courseId) {
    try {
      const response = await axios.get(`/api/courses/${courseId}/comments`)
      if (response.data.success) {
        commit('SET_COURSE_COMMENTS', response.data.data)
        return response.data.data
      }
      throw new Error(response.data.message || '获取课程评论失败')
    } catch (error) {
      console.error('获取课程评论失败:', error)
      throw error
    }
  },

  // 提交课程评论
  async submitComment({ commit }, params) {
    try {
      const response = await axios.post(`/api/courses/${params.courseId}/comments`, {
        rating: params.rating,
        content: params.content
      })
      if (response.data.success) {
        return response.data.data
      }
      throw new Error(response.data.message || '提交评论失败')
    } catch (error) {
      console.error('提交评论失败:', error)
      throw error
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 