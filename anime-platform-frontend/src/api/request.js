import axios from 'axios'
import { Message } from 'element-ui'
import { getToken, removeAuth } from '../utils/auth'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      removeAuth()
      Message.warning('登录状态已失效，请重新登录')
    }
    return Promise.reject(error)
  }
)

export default request
