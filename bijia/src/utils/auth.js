// Token 存储和读取
const TOKEN_KEY = 'Authorization'

/**
 * 获取 Token
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置 Token
 */
export function setToken(token) {
  return localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 删除 Token
 */
export function removeToken() {
  return localStorage.removeItem(TOKEN_KEY)
}
