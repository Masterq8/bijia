import request from '@/utils/request'

export function getDailyVisits() {
    return request({
        url: '/api/visits/daily',
        method: 'get'
    })
  }
  