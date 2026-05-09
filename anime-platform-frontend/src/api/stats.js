import request from './request'

export function getAdminStats() {
  return request({
    url: '/admin/stats/overview',
    method: 'get'
  })
}
