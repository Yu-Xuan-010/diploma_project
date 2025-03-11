const getters = {
  // 用户相关
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,

  // 课程相关
  currentCourse: state => state.course.currentCourse,
  courseList: state => state.course.courseList,
  courseCategories: state => state.course.categories,
  favoriteCourses: state => state.course.favorites,

  // 学习相关
  learningPlan: state => state.learning.plan,
  learningProgress: state => state.learning.progress,
  learningHistory: state => state.learning.history,
  notes: state => state.learning.notes,
  bookmarks: state => state.learning.bookmarks,

  // 互动相关
  discussions: state => state.interaction.discussions,
  notifications: state => state.interaction.notifications,
  unreadCount: state => state.interaction.unreadCount
}

export default getters
