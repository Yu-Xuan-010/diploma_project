import axios from 'axios'

const state = {
  lessons: []
}

const mutations = {
  SET_LESSONS(state, lessons) {
    state.lessons = lessons
  }
}

const actions = {
  // 获取课程的所有课时
  async getLessonsByCourseId({ commit }, courseId) {
    try {
      const response = await axios.get(`/api/lessons/course/${courseId}`)
      if (response.data.success) {
        commit('SET_LESSONS', response.data.data)
        return response.data.data
      }
      throw new Error(response.data.message || '获取课时列表失败')
    } catch (error) {
      console.error('获取课时列表失败:', error)
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