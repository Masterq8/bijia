/**
 * 参数处理
 * @param {*} params 参数
 */
export function tansParams(params) {
  let result = ''
  Object.keys(params).forEach((key) => {
    if (!Object.prototype.hasOwnProperty.call(params, key) || params[key] === null || params[key] === '') {
      return
    }
    result += encodeURIComponent(key) + '=' + encodeURIComponent(params[key]) + '&'
  })
  return result
}

/**
 * 验证是否为 blob 数据
 * @param {*} data
 */
export function blobValidate(data) {
  return data.type !== 'application/json'
}
