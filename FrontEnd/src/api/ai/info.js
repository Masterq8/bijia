import request from '@/utils/request'
//处理分析
export function aiInfo(query) {
    return request({
        url: '/ai/info/list',
        method: 'get',
        params: query
    })
}

export function generateAipOrder(data) {
    return request({
        url: '/aiprocess/generateAIP',
        method: 'post',
        data: data
    })
}
