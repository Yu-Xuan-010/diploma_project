import request from '@/utils/request'

// 获取教师申请列表
export function listTeacherApplications(query) {
  return request({
    url: '/sen/teacherApplication/list',
    method: 'get',
    params: query
  })
}

// 获取教师申请详细信息
export function getTeacherApplication(id) {
  return request({
    url: '/sen/teacherApplication/' + id,
    method: 'get'
  })
}

// 审核教师申请
export function reviewTeacherApplication(data) {
  return request({
    url: '/sen/teacherApplication/review/' + data.id,
    method: 'put',
    params: {
      status: data.status,
      reviewerId: data.reviewerId,
      reviewComment: data.reviewComment
    }
  })
} 