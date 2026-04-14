import request from '@/utils/request'

/**
 * 获取验证码
 */
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get',
    headers: {
      isToken: false
    }
  })
}

/**
 * 登录
 */
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    headers: {
      isToken: false
    },
    data: data
  })
}

/**
 * 注册
 */
export function register(data) {
  return request({
    url: '/register',
    method: 'post',
    headers: {
      isToken: false
    },
    data: data
  })
}

/**
 * 获取用户信息
 */
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

/**
 * 获取路由
 */
export function getRouters() {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}

/**
 * 获取字典数据
 */
export function getDicts(dictType) {
  return request({
    url: '/system/dict/data/type/' + dictType,
    method: 'get'
  })
}
