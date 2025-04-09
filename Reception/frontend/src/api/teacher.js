import request from '../utils/request'

// 提交教师申请
export function submitTeacherApplication(data) {
  return request({
    url: '/api/teacher-applications',
    method: 'post',
    data: {
      userId: data.userId,
      reason: data.reason,
      expertise: data.expertise,
      experience: data.experience
    }
  })
}

// 获取用户的教师申请
export function getUserTeacherApplications(userId) {
  return request({
    url: `/api/teacher-applications/user/${userId}`,
    method: 'get'
  })
}

// 获取特定状态的教师申请
export function getTeacherApplicationsByStatus(status) {
  return request({
    url: `/api/teacher-applications/status/${status}`,
    method: 'get'
  })
}

// 审核教师申请
export function reviewTeacherApplication(id, reviewerId, status, reviewComment) {
  return request({
    url: `/api/teacher-applications/${id}/review`,
    method: 'put',
    params: {
      reviewerId,
      status,
      reviewComment
    }
  })
} 