import request from '@/utils/request'

//查看订单信息列表
export function getOrderInfoList(type) {
  return request({
    url: '/order/orderInfo/list',
    method: 'get',
    params: type,   //将category参数传递给后端
  })
}

export function listMain() {
  return request({
      url: '/order/orderInfo/main/list',
      method: 'get'
  })
}







