import request from '@/utils/request'

// 学习计划相关接口
export function getLearningPlan() {
  return request({
    url: '/learning/plan',
    method: 'get'
  })
}

export function updateLearningPlan(data) {
  return request({
    url: '/learning/plan',
    method: 'put',
    data: data
  })
}

export function createLearningTask(data) {
  return request({
    url: '/learning/task',
    method: 'post',
    data: data
  })
}

export function updateLearningTask(id, data) {
  return request({
    url: `/learning/task/${id}`,
    method: 'put',
    data: data
  })
}

export function deleteLearningTask(id) {
  return request({
    url: `/learning/task/${id}`,
    method: 'delete'
  })
}

export function completeLearningTask(id) {
  return request({
    url: `/learning/task/${id}/complete`,
    method: 'post'
  })
}

// 学习进度相关接口
export function getLearningProgress(query) {
  return request({
    url: '/learning/progress',
    method: 'get',
    params: query
  })
}

export function getLearningStatistics(timeRange) {
  return request({
    url: '/learning/statistics',
    method: 'get',
    params: timeRange
  })
}

export function getChapterProgress(courseId) {
  return request({
    url: `/learning/progress/course/${courseId}`,
    method: 'get'
  })
}

export function updateLearningRecord(data) {
  return request({
    url: '/learning/record',
    method: 'post',
    data: data
  })
}

export function getLearningHistory(query) {
  return request({
    url: '/learning/history',
    method: 'get',
    params: query
  })
}

// 笔记和标记相关接口
export function getNotesList(query) {
  return request({
    url: '/learning/notes',
    method: 'get',
    params: query
  })
}

export function createNote(data) {
  return request({
    url: '/learning/note',
    method: 'post',
    data: data
  })
}

export function updateNote(id, data) {
  return request({
    url: `/learning/note/${id}`,
    method: 'put',
    data: data
  })
}

export function deleteNote(id) {
  return request({
    url: `/learning/note/${id}`,
    method: 'delete'
  })
}

export function getBookmarks(query) {
  return request({
    url: '/learning/bookmarks',
    method: 'get',
    params: query
  })
}

export function addBookmark(data) {
  return request({
    url: '/learning/bookmark',
    method: 'post',
    data: data
  })
}

export function removeBookmark(id) {
  return request({
    url: `/learning/bookmark/${id}`,
    method: 'delete'
  })
}

// 考试和作业相关接口
export function getExamList(query) {
  return request({
    url: '/learning/exams',
    method: 'get',
    params: query
  })
}

export function getExamDetail(id) {
  return request({
    url: `/learning/exam/${id}`,
    method: 'get'
  })
}

export function submitExam(id, data) {
  return request({
    url: `/learning/exam/${id}/submit`,
    method: 'post',
    data: data
  })
}

export function getHomeworkList(query) {
  return request({
    url: '/learning/homework',
    method: 'get',
    params: query
  })
}

export function submitHomework(id, data) {
  return request({
    url: `/learning/homework/${id}/submit`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 学习资源相关接口
export function getResourceList(query) {
  return request({
    url: '/learning/resources',
    method: 'get',
    params: query
  })
}

export function downloadResource(id) {
  return request({
    url: `/learning/resource/${id}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

// 学习提醒相关接口
export function getLearningReminders() {
  return request({
    url: '/learning/reminders',
    method: 'get'
  })
}

export function createReminder(data) {
  return request({
    url: '/learning/reminder',
    method: 'post',
    data: data
  })
}

export function updateReminder(id, data) {
  return request({
    url: `/learning/reminder/${id}`,
    method: 'put',
    data: data
  })
}

export function deleteReminder(id) {
  return request({
    url: `/learning/reminder/${id}`,
    method: 'delete'
  })
}

// 学习报告相关接口
export function generateLearningReport(params) {
  return request({
    url: '/learning/report/generate',
    method: 'get',
    params: params
  })
}

export function getLearningReportList(query) {
  return request({
    url: '/learning/reports',
    method: 'get',
    params: query
  })
}

export function getLearningReportDetail(id) {
  return request({
    url: `/learning/report/${id}`,
    method: 'get'
  })
}
