import request from './request'

export function getAdminUserList(params = {}) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/admin/user/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function getAdminCommentList(params = {}) {
  return request({
    url: '/admin/comment/list',
    method: 'get',
    params
  })
}

export function updateCommentStatus(id, status) {
  return request({
    url: `/admin/comment/${id}/status`,
    method: 'put',
    data: { status }
  })
}
