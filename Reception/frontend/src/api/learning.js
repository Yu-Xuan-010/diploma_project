import request from '@/utils/request'

// 学习计划相关接口
export function getStudyPlans(params) {
  return request({
    url: '/api/learning/plans',
    method: 'get',
    params
  })
}

export function createStudyPlan(data) {
  return request({
    url: '/api/learning/plans',
    method: 'post',
    data
  })
}

export function updateStudyPlan(id, data) {
  return request({
    url: `/api/learning/plans/${id}`,
    method: 'put',
    data
  })
}

export function deleteStudyPlan(id) {
  return request({
    url: `/api/learning/plans/${id}`,
    method: 'delete'
  })
}

// 学习进度相关接口
export function getLearningProgress(courseId) {
  return request({
    url: `/api/learning/progress/${courseId}`,
    method: 'get'
  })
}

export function getLearningOverview() {
  return request({
    url: '/api/learning/overview',
    method: 'get'
  })
}

export function getLearningStatistics() {
  return request({
    url: '/api/learning/statistics',
    method: 'get'
  })
}

export function getChapterProgress(courseId) {
  return request({
    url: `/api/learning/progress/course/${courseId}`,
    method: 'get'
  })
}

export function updateLearningRecord(data) {
  return request({
    url: '/api/learning/record',
    method: 'post',
    data
  })
}

export function getLearningHistory(params) {
  return request({
    url: '/api/learning/history',
    method: 'get',
    params
  })
}

// 笔记相关接口
export function getNotesList(params) {
  return request({
    url: '/api/learning/notes',
    method: 'get',
    params
  })
}

export function createNote(data) {
  return request({
    url: '/api/learning/notes',
    method: 'post',
    data
  })
}

export function updateNote(id, data) {
  return request({
    url: `/api/learning/notes/${id}`,
    method: 'put',
    data
  })
}

export function deleteNote(id) {
  return request({
    url: `/api/learning/notes/${id}`,
    method: 'delete'
  })
}

// 书签相关接口
export function getBookmarks(params) {
  return request({
    url: '/api/learning/bookmarks',
    method: 'get',
    params
  })
}

export function addBookmark(data) {
  return request({
    url: '/api/learning/bookmarks',
    method: 'post',
    data
  })
}

export function removeBookmark(id) {
  return request({
    url: `/api/learning/bookmarks/${id}`,
    method: 'delete'
  })
}

// 考试相关接口
export function getExamList(params) {
  return request({
    url: '/api/learning/exams',
    method: 'get',
    params
  })
}

export function getExamDetail(id) {
  return request({
    url: `/api/learning/exams/${id}`,
    method: 'get'
  })
}

export function submitExam(id, data) {
  return request({
    url: `/api/learning/exams/${id}/submit`,
    method: 'post',
    data
  })
}

// 作业相关接口
export function getHomeworkList(params) {
  return request({
    url: '/api/learning/homework',
    method: 'get',
    params
  })
}

export function submitHomework(id, data) {
  return request({
    url: `/api/learning/homework/${id}/submit`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 资源相关接口
export function getResourceList(courseId, params) {
  return request({
    url: `/api/learning/resources/${courseId}`,
    method: 'get',
    params
  })
}

export function downloadResource(resourceId) {
  return request({
    url: `/api/learning/resources/${resourceId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

// 提醒相关接口
export function getLearningReminders(params) {
  return request({
    url: '/api/learning/reminders',
    method: 'get',
    params
  })
}

export function createReminder(data) {
  return request({
    url: '/api/learning/reminders',
    method: 'post',
    data
  })
}

export function updateReminder(id, data) {
  return request({
    url: `/api/learning/reminders/${id}`,
    method: 'put',
    data
  })
}

export function deleteReminder(id) {
  return request({
    url: `/api/learning/reminders/${id}`,
    method: 'delete'
  })
}

// 学习报告相关接口
export function generateLearningReport(params) {
  return request({
    url: '/api/learning/reports/generate',
    method: 'get',
    params
  })
}

export function getLearningReportList(params) {
  return request({
    url: '/api/learning/reports',
    method: 'get',
    params
  })
}

export function getLearningReportDetail(id) {
  return request({
    url: `/api/learning/reports/${id}`,
    method: 'get'
  })
}

// 获取学习计划
export function getLearningPlan() {
  return request({
    url: '/api/learning/plan',
    method: 'get'
  })
}

// 更新学习计划
export function updateLearningPlan(data) {
  return request({
    url: '/api/learning/plan',
    method: 'put',
    data
  })
}
