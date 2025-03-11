import {
  getLearningPlan,
  updateLearningPlan,
  getLearningProgress,
  getLearningHistory,
  getNotesList,
  getBookmarks
} from '@/api/learning'

const state = {
  plan: null,
  progress: {},
  history: [],
  notes: [],
  bookmarks: [],
  loading: false
}

const mutations = {
  SET_LEARNING_PLAN: (state, plan) => {
    state.plan = plan
  },
  SET_LEARNING_PROGRESS: (state, progress) => {
    state.progress = progress
  },
  SET_LEARNING_HISTORY: (state, history) => {
    state.history = history
  },
  SET_NOTES: (state, notes) => {
    state.notes = notes
  },
  SET_BOOKMARKS: (state, bookmarks) => {
    state.bookmarks = bookmarks
  },
  SET_LOADING: (state, loading) => {
    state.loading = loading
  },
  ADD_NOTE: (state, note) => {
    state.notes.unshift(note)
  },
  UPDATE_NOTE: (state, updatedNote) => {
    const index = state.notes.findIndex(note => note.id === updatedNote.id)
    if (index !== -1) {
      state.notes.splice(index, 1, updatedNote)
    }
  },
  DELETE_NOTE: (state, noteId) => {
    const index = state.notes.findIndex(note => note.id === noteId)
    if (index !== -1) {
      state.notes.splice(index, 1)
    }
  },
  ADD_BOOKMARK: (state, bookmark) => {
    state.bookmarks.unshift(bookmark)
  },
  REMOVE_BOOKMARK: (state, bookmarkId) => {
    const index = state.bookmarks.findIndex(bookmark => bookmark.id === bookmarkId)
    if (index !== -1) {
      state.bookmarks.splice(index, 1)
    }
  },
  UPDATE_LEARNING_PROGRESS: (state, { courseId, progress }) => {
    if (state.progress[courseId]) {
      state.progress[courseId] = { ...state.progress[courseId], ...progress }
    } else {
      state.progress[courseId] = progress
    }
  }
}

const actions = {
  // 获取学习计划
  GetLearningPlan({ commit }) {
    return new Promise((resolve, reject) => {
      getLearningPlan().then(response => {
        const { data } = response
        commit('SET_LEARNING_PLAN', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 更新学习计划
  UpdateLearningPlan({ commit }, plan) {
    return new Promise((resolve, reject) => {
      updateLearningPlan(plan).then(response => {
        const { data } = response
        commit('SET_LEARNING_PLAN', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取学习进度
  GetLearningProgress({ commit }, query) {
    commit('SET_LOADING', true)
    return new Promise((resolve, reject) => {
      getLearningProgress(query).then(response => {
        const { data } = response
        commit('SET_LEARNING_PROGRESS', data)
        commit('SET_LOADING', false)
        resolve(data)
      }).catch(error => {
        commit('SET_LOADING', false)
        reject(error)
      })
    })
  },

  // 获取学习历史
  GetLearningHistory({ commit }, query) {
    return new Promise((resolve, reject) => {
      getLearningHistory(query).then(response => {
        const { data } = response
        commit('SET_LEARNING_HISTORY', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取笔记列表
  GetNotes({ commit }, query) {
    return new Promise((resolve, reject) => {
      getNotesList(query).then(response => {
        const { data } = response
        commit('SET_NOTES', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取书签列表
  GetBookmarks({ commit }, query) {
    return new Promise((resolve, reject) => {
      getBookmarks(query).then(response => {
        const { data } = response
        commit('SET_BOOKMARKS', data)
        resolve(data)
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
