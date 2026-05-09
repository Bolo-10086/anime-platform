import request from './request'

export function getAnimeList(params = {}) {
  return request({
    url: '/anime/list',
    method: 'get',
    params
  })
}

export function getAnimeDetail(id) {
  return request({
    url: `/anime/detail/${id}`,
    method: 'get'
  })
}

export function getAdminAnimeList(params = {}) {
  return request({
    url: '/admin/anime/list',
    method: 'get',
    params
  })
}

export function createAnime(data) {
  return request({
    url: '/admin/anime',
    method: 'post',
    data
  })
}

export function updateAnime(id, data) {
  return request({
    url: `/admin/anime/${id}`,
    method: 'put',
    data
  })
}

export function deleteAnime(id) {
  return request({
    url: `/admin/anime/${id}`,
    method: 'delete'
  })
}

export function getAnimeComments(animeId) {
  return request({
    url: `/anime/${animeId}/comments`,
    method: 'get'
  })
}

export function getUserAnimeState(animeId) {
  return request({
    url: `/user/anime/${animeId}/state`,
    method: 'get'
  })
}

export function addAnimeComment(animeId, content) {
  return request({
    url: `/user/anime/${animeId}/comments`,
    method: 'post',
    data: { content }
  })
}

export function favoriteAnime(animeId) {
  return request({
    url: `/user/anime/${animeId}/favorite`,
    method: 'post'
  })
}

export function cancelFavoriteAnime(animeId) {
  return request({
    url: `/user/anime/${animeId}/favorite`,
    method: 'delete'
  })
}

export function rateAnime(animeId, rating) {
  return request({
    url: `/user/anime/${animeId}/rating`,
    method: 'post',
    data: { rating }
  })
}

export function getUserFavorites() {
  return request({
    url: '/user/favorites',
    method: 'get'
  })
}

export function getUserRatings() {
  return request({
    url: '/user/ratings',
    method: 'get'
  })
}

export function getUserComments() {
  return request({
    url: '/user/comments',
    method: 'get'
  })
}
