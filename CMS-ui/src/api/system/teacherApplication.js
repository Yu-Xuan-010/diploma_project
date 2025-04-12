import request from '@/utils/request'

// 查询教师申请列表
export function listTeacherApplications(query) {
  return request({
    url: '/system/teacherApplication/list',
    method: 'get',
    params: query
  })
}

// 查询教师申请详细
export function getTeacherApplication(id) {
  return request({
    url: '/system/teacherApplication/' + id,
    method: 'get'
  })
}

// 审核教师申请
export function reviewTeacherApplication(data) {
  return request({
    url: `/system/teacherApplication/review/${data.id}`,
    method: 'put',
    params: {
      status: data.status,
      reviewerId: data.reviewerId,
      reviewComment: data.reviewComment || ''
    }
  })
} 