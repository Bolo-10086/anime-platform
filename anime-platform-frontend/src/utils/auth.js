const TOKEN_KEY = 'anime_platform_token'
const USER_KEY = 'anime_platform_user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function getUser() {
  const value = localStorage.getItem(USER_KEY)
  if (!value) {
    return null
  }
  try {
    return JSON.parse(value)
  } catch (error) {
    return null
  }
}

export function hasToken() {
  return Boolean(getToken())
}

export function saveAuth(loginData) {
  localStorage.setItem(TOKEN_KEY, loginData.token)
  localStorage.setItem(USER_KEY, JSON.stringify(loginData.userInfo))
  emitAuthChange()
}

export function removeAuth(emitEvent = true) {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  if (emitEvent) {
    emitAuthChange()
  }
}

export function emitAuthChange() {
  window.dispatchEvent(new Event('auth-change'))
}
