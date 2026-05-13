import request from '@/utils/request'

// 查询订单管理列表
export function listOrderManagement(query) {
    return request({
        url: '/orderManagement/orderManagement/list',
        method: 'get',
        params: query
    })
}// 查询订单管理列表
export function listOrderManagement2(query) {
    return request({
        url: '/orderManagement/orderManagement/list2',
        method: 'get',
        params: query
    })
}

// 查询订单管理详细
export function getOrderManagement(id) {
    return request({
        url: '/orderManagement/orderManagement/' + id,
        method: 'get'
    })
}

// 新增订单管理
export function addOrderManagement(data) {
    return request({
        url: '/orderManagement/orderManagement',
        method: 'post',
        data: data
    })
}

// 修改订单管理
export function updateOrderManagement(data) {
    return request({
        url: '/orderManagement/orderManagement',
        method: 'put',
        data: data
    })
}

// 删除订单管理
export function delOrderManagement(id) {
    return request({
        url: '/orderManagement/orderManagement/' + id,
        method: 'delete'
    })
}

export function generateGTMOrder(data) {
    return request({
        url: '/aiprocess/generateGTM',
        method: 'post',
        data: data
    })
}

export function setProductShow(data) {
    return request({
        url: '/order/orderInfo/showSet',
        method: 'put',
        data: data
    })
}
