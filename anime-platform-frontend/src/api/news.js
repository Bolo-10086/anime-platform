import request from './request'

export function getNewsList(params = {}) {
  return request({
    url: '/news/list',
    method: 'get',
    params
  })
}

export function getNewsDetail(id) {
  return request({
    url: `/news/detail/${id}`,
    method: 'get'
  })
}

export function getAdminNewsList(params = {}) {
  return request({
    url: '/admin/news/list',
    method: 'get',
    params
  })
}

export function createNews(data) {
  return request({
    url: '/admin/news',
    method: 'post',
    data
  })
}

export function updateNews(id, data) {
  return request({
    url: `/admin/news/${id}`,
    method: 'put',
    data
  })
}

export function deleteNews(id) {
  return request({
    url: `/admin/news/${id}`,
    method: 'delete'
  })
}
