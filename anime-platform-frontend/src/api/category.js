import request from './request'

export function getCategoryList() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}

export function getAdminCategoryList() {
  return request({
    url: '/admin/category/list',
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/admin/category',
    method: 'post',
    data
  })
}

export function updateCategory(id, data) {
  return request({
    url: `/admin/category/${id}`,
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/admin/category/${id}`,
    method: 'delete'
  })
}

export function getTagList() {
  return request({
    url: '/tag/list',
    method: 'get'
  })
}

export function getAdminTagList() {
  return request({
    url: '/admin/tag/list',
    method: 'get'
  })
}

export function createTag(data) {
  return request({
    url: '/admin/tag',
    method: 'post',
    data
  })
}

export function updateTag(id, data) {
  return request({
    url: `/admin/tag/${id}`,
    method: 'put',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: `/admin/tag/${id}`,
    method: 'delete'
  })
}
