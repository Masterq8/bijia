import request from '@/utils/request'

// 查询订单管理列表
export function imageTypeGet(query) {
    return request({
        url: 'thematicMap/thematicMap/imageTypeGet',
        method: 'get',
        params: query
    })
}

// 新增专题图
export function addMake(data) {
    return request({
        url: '/orderManagement/orderManagement/addMake',
        method: 'post',
        data: data
    })
}

// 查询订单管理列表
export function imageGetByCode(code) {
    return request({
        url: 'thematicMap/thematicMap/getByCode/'+code,
        method: 'get'
    })
}
