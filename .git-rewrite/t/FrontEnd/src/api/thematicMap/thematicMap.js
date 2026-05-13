import request from '@/utils/request'

// 查询订单管理列表
export function thematicMapList(query) {
    return request({
        url: 'thematicMap/thematicMap/getThematic',
        method: 'get',
        params: query
    })
}

export function deleteThematicMap(id) {
    return request({
        url: 'thematicMap/thematicMap/'+ id,
        method: 'delete',
    })
}

// 新增专题图
export function addThematicMap(data) {
    return request({
        url: 'thematicMap/thematicMap/addThematicMap',
        method: 'post',
        data: data
    })
}

export function getThematic(id) {
    return request({
        url: '/thematicMap/thematicMap/' + id,
        method: 'get'
    })
}

// 修改参数配置
export function updateThematic(data) {
    return request({
        url: '/thematicMap/thematicMap',
        method: 'put',
        data: data
    })
}
