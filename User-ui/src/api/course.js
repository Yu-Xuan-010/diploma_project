import request from '@/utils/request'

// 课程列表相关接口
export function getCourseList(query) {
  return request({
    url: '/course/list',
    method: 'get',
    params: query
  })
}

export function getRecommendCourses() {
  return request({
    url: '/course/recommend',
    method: 'get'
  })
}

export function getPopularCourses() {
  return request({
    url: '/course/popular',
    method: 'get'
  })
}

export function getNewCourses() {
  return request({
    url: '/course/new',
    method: 'get'
  })
}

// 课程详情相关接口
export function getCourseDetail(id) {
  return request({
    url: `/course/${id}`,
    method: 'get'
  })
}

export function getCourseOutline(id) {
  return request({
    url: `/course/${id}/outline`,
    method: 'get'
  })
}

export function getChapterDetail(courseId, chapterId) {
  return request({
    url: `/course/${courseId}/chapter/${chapterId}`,
    method: 'get'
  })
}

export function getSectionContent(courseId, chapterId, sectionId) {
  return request({
    url: `/course/${courseId}/chapter/${chapterId}/section/${sectionId}`,
    method: 'get'
  })
}

// 课程分类相关接口
export function getCourseCategories() {
  return request({
    url: '/course/categories',
    method: 'get'
  })
}

export function getCategoryDetail(id) {
  return request({
    url: `/course/category/${id}`,
    method: 'get'
  })
}

// 课程评价相关接口
export function getCourseRatings(courseId, query) {
  return request({
    url: `/course/${courseId}/ratings`,
    method: 'get',
    params: query
  })
}

export function submitCourseRating(courseId, data) {
  return request({
    url: `/course/${courseId}/rating`,
    method: 'post',
    data: data
  })
}

export function updateCourseRating(courseId, ratingId, data) {
  return request({
    url: `/course/${courseId}/rating/${ratingId}`,
    method: 'put',
    data: data
  })
}

// 课程报名与购买相关接口
export function enrollCourse(courseId) {
  return request({
    url: `/course/${courseId}/enroll`,
    method: 'post'
  })
}

export function createCourseOrder(courseId, data) {
  return request({
    url: `/course/${courseId}/order`,
    method: 'post',
    data: data
  })
}

export function getCourseOrderDetail(orderId) {
  return request({
    url: `/course/order/${orderId}`,
    method: 'get'
  })
}

// 学习记录相关接口
export function updateLearningProgress(courseId, data) {
  return request({
    url: `/course/${courseId}/progress`,
    method: 'post',
    data: data
  })
}

export function getLastLearningPosition(courseId) {
  return request({
    url: `/course/${courseId}/last-position`,
    method: 'get'
  })
}

// 课程资源相关接口
export function getCourseResources(courseId, query) {
  return request({
    url: `/course/${courseId}/resources`,
    method: 'get',
    params: query
  })
}

export function downloadCourseResource(courseId, resourceId) {
  return request({
    url: `/course/${courseId}/resource/${resourceId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

// 课程互动相关接口
export function getCourseQuestions(courseId, query) {
  return request({
    url: `/course/${courseId}/questions`,
    method: 'get',
    params: query
  })
}

export function askQuestion(courseId, data) {
  return request({
    url: `/course/${courseId}/question`,
    method: 'post',
    data: data
  })
}

export function answerQuestion(courseId, questionId, data) {
  return request({
    url: `/course/${courseId}/question/${questionId}/answer`,
    method: 'post',
    data: data
  })
}

// 课程收藏相关接口
export function getFavoriteCourses(query) {
  return request({
    url: '/course/favorites',
    method: 'get',
    params: query
  })
}

export function addToFavorites(courseId) {
  return request({
    url: `/course/${courseId}/favorite`,
    method: 'post'
  })
}

export function removeFromFavorites(courseId) {
  return request({
    url: `/course/${courseId}/favorite`,
    method: 'delete'
  })
}

// 课程通知相关接口
export function getCourseNotifications(courseId, query) {
  return request({
    url: `/course/${courseId}/notifications`,
    method: 'get',
    params: query
  })
}

export function markNotificationRead(courseId, notificationId) {
  return request({
    url: `/course/${courseId}/notification/${notificationId}/read`,
    method: 'put'
  })
}

// 课程证书相关接口
export function getCourseCertificate(courseId) {
  return request({
    url: `/course/${courseId}/certificate`,
    method: 'get',
    responseType: 'blob'
  })
}

export function verifyCertificate(certificateNo) {
  return request({
    url: '/course/certificate/verify',
    method: 'post',
    data: { certificateNo }
  })
}

// 课程搜索相关接口
export function searchCourses(query) {
  return request({
    url: '/course/search',
    method: 'get',
    params: query
  })
}

export function getSearchHistory() {
  return request({
    url: '/course/search/history',
    method: 'get'
  })
}

export function clearSearchHistory() {
  return request({
    url: '/course/search/history',
    method: 'delete'
  })
}
