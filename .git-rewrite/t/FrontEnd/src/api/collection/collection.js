import request from '@/utils/request'

// 查询收藏列表
export function listInfo(query) {
    return request({
        url: '/collection/info/list',
        method: 'get',
        params: query
    })
}

// 查询收藏详细
export function getInfo(id) {
    return request({
        url: '/collection/info/' + id,
        method: 'get'
    })
}

// 新增收藏
export function addCollectionInfo(data) {
    return request({
        url: '/collection/info',
        method: 'post',
        data: data
    })
}

// 修改收藏
export function updateInfo(data) {
    return request({
        url: '/collection/info',
        method: 'put',
        data: data
    })
}

// 删除收藏
export function delInfo(id) {
    return request({
        url: '/collection/info/' + id,
        method: 'delete'
    })
}
