import request from '@/utils/request'

// 查询卫星影像管理列表
export function listSatellite(query) {
    return request({
        url: '/map/satellite/list',
        method: 'get',
        params: query
    })
}
export function listSatellite1(query) {
    return request({
        url: '/map/satellite/list1',
        method: 'get',
        params: query
    })
}

// 查询卫星影像管理详细
export function getSatellite(id) {
    return request({
        url: '/map/satellite/' + id,
        method: 'get'
    })
}

// 新增卫星影像管理
export function addSatellite(data) {
    return request({
        url: '/map/satellite',
        method: 'post',
        data: data
    })
}

// 修改卫星影像管理
export function updateSatellite(data) {
    return request({
        url: '/map/satellite',
        method: 'put',
        data: data
    })
}

// 删除卫星影像管理
export function delSatellite(id) {
    return request({
        url: '/map/satellite/' + id,
        method: 'delete'
    })
}

// 智能ai机器人调用接口
export function getByContent(query) {
    return request({
        url: '/answer/getByContent',
        method: 'get',
        params: query
    })
}
