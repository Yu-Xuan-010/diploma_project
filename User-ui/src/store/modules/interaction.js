import {
  getDiscussionList,
  getDiscussionDetail,
  createDiscussion,
  replyDiscussion,
  likeDiscussion,
  getNotifications,
  readNotification,
  readAllNotifications
} from '@/api/interaction'

const state = {
  discussions: [],
  currentDiscussion: null,
  notifications: [],
  unreadCount: 0,
  total: 0,
  loading: false
}

const mutations = {
  SET_DISCUSSIONS: (state, discussions) => {
    state.discussions = discussions
  },
  SET_CURRENT_DISCUSSION: (state, discussion) => {
    state.currentDiscussion = discussion
  },
  SET_NOTIFICATIONS: (state, notifications) => {
    state.notifications = notifications
  },
  SET_UNREAD_COUNT: (state, count) => {
    state.unreadCount = count
  },
  SET_TOTAL: (state, total) => {
    state.total = total
  },
  SET_LOADING: (state, loading) => {
    state.loading = loading
  },
  ADD_DISCUSSION: (state, discussion) => {
    state.discussions.unshift(discussion)
  },
  UPDATE_DISCUSSION: (state, discussion) => {
    const index = state.discussions.findIndex(item => item.id === discussion.id)
    if (index !== -1) {
      state.discussions.splice(index, 1, discussion)
    }
  },
  ADD_REPLY: (state, { discussionId, reply }) => {
    if (state.currentDiscussion && state.currentDiscussion.id === discussionId) {
      state.currentDiscussion.replies.push(reply)
    }
  },
  UPDATE_LIKE_STATUS: (state, { discussionId, liked, likeCount }) => {
    const discussion = state.discussions.find(item => item.id === discussionId)
    if (discussion) {
      discussion.liked = liked
      discussion.likeCount = likeCount
    }
    if (state.currentDiscussion && state.currentDiscussion.id === discussionId) {
      state.currentDiscussion.liked = liked
      state.currentDiscussion.likeCount = likeCount
    }
  },
  MARK_NOTIFICATION_READ: (state, notificationId) => {
    const notification = state.notifications.find(item => item.id === notificationId)
    if (notification && !notification.read) {
      notification.read = true
      state.unreadCount--
    }
  },
  MARK_ALL_READ: (state) => {
    state.notifications.forEach(notification => {
      notification.read = true
    })
    state.unreadCount = 0
  }
}

const actions = {
  // 获取讨论列表
  GetDiscussions({ commit }, query) {
    commit('SET_LOADING', true)
    return new Promise((resolve, reject) => {
      getDiscussionList(query).then(response => {
        const { rows, total } = response.data
        commit('SET_DISCUSSIONS', rows)
        commit('SET_TOTAL', total)
        commit('SET_LOADING', false)
        resolve()
      }).catch(error => {
        commit('SET_LOADING', false)
        reject(error)
      })
    })
  },

  // 获取讨论详情
  GetDiscussionDetail({ commit }, discussionId) {
    return new Promise((resolve, reject) => {
      getDiscussionDetail(discussionId).then(response => {
        const { data } = response
        commit('SET_CURRENT_DISCUSSION', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 创建讨论
  CreateDiscussion({ commit }, discussionData) {
    return new Promise((resolve, reject) => {
      createDiscussion(discussionData).then(response => {
        const { data } = response
        commit('ADD_DISCUSSION', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 回复讨论
  ReplyDiscussion({ commit }, { discussionId, content }) {
    return new Promise((resolve, reject) => {
      replyDiscussion(discussionId, content).then(response => {
        const { data } = response
        commit('ADD_REPLY', { discussionId, reply: data })
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 点赞/取消点赞讨论
  LikeDiscussion({ commit }, discussionId) {
    return new Promise((resolve, reject) => {
      likeDiscussion(discussionId).then(response => {
        const { liked, likeCount } = response.data
        commit('UPDATE_LIKE_STATUS', { discussionId, liked, likeCount })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取通知列表
  GetNotifications({ commit }, query) {
    return new Promise((resolve, reject) => {
      getNotifications(query).then(response => {
        const { rows, unreadCount } = response.data
        commit('SET_NOTIFICATIONS', rows)
        commit('SET_UNREAD_COUNT', unreadCount)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 标记通知为已读
  ReadNotification({ commit }, notificationId) {
    return new Promise((resolve, reject) => {
      readNotification(notificationId).then(() => {
        commit('MARK_NOTIFICATION_READ', notificationId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 标记所有通知为已读
  ReadAllNotifications({ commit }) {
    return new Promise((resolve, reject) => {
      readAllNotifications().then(() => {
        commit('MARK_ALL_READ')
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
