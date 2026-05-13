import request from '@/utils/request'

// 查询我的数据列表
export function listInfo(query) {
    return request({
        url: '/record/info/list',
        method: 'get',
        params: query
    })
}

// 查询我的数据详细
export function getInfo(id) {
    return request({
        url: '/record/info/' + id,
        method: 'get'
    })
}

// 新增我的数据
export function addInfo(data) {
    return request({
        url: '/record/info',
        method: 'post',
        data: data
    })
}
export function addInfo1(data) {
    return request({
        url: '/record/info/dealZip',
        method: 'post',
        data: data,
        timeout: 1000 * 600,
    })
}
// 修改我的数据
export function updateInfo(data) {
    return request({
        url: '/record/info',
        method: 'put',
        data: data
    })
}

// 删除我的数据
export function delInfo(id) {
    return request({
        url: '/record/info/' + id,
        method: 'delete'
    })
}

// 新增我的数据
export function addDateInfo(data) {
    return request({
        url: '/record/info/add',
        method: 'post',
        data: data
    })
}
