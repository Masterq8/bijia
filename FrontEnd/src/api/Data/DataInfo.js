import request from '@/utils/request'

export function getMenuInfoList(query) {
  return request({
    url: '/data/info/list',
    method: 'get',
    params: query,   //将category参数传递给后端
  })
}
