import request from '@/utils/request'

/**
 * 查询卫星数据列表（支持多种查询条件）
 * @param {Object} data - 查询参数
 * @param {number} data.pageNum - 页码，默认1
 * @param {number} data.pageSize - 每页条数，默认10
 * @param {string} data.province - 省份名称（可选）
 * @param {string} data.city - 城市名称（可选）
 * @param {Array<string>} data.points - 自定义多边形坐标数组 ["lat,lng", ...] - 与province/city互斥
 * @param {Array<string>} data.satelliteTypes - 卫星类型数组 ["高分系列", ...]
 * @param {string} data.startCollectTime - 起始采集时间 "yyyy-MM-dd HH:mm:ss"
 * @param {string} data.endCollectTime - 结束采集时间 "yyyy-MM-dd HH:mm:ss"
 * @returns {Promise}
 */
export function listSatellite1(data) {
  return request({
    url: '/map/satellite/list1',
    method: 'get',
    params: data
  })
}

/**
 * 获取卫星数据详情
 * @param {number} id - 卫星ID
 * @returns {Promise}
 */
export function getSatellite(id) {
  return request({
    url: `/map/satellite/${id}`,
    method: 'get'
  })
}

/**
 * 智能问答接口
 * @param {string} content - 用户提问内容
 * @returns {Promise}
 */
export function getAnswer(content) {
  return request({
    url: '/answer/getByContent',
    method: 'get',
    params: {
      content: content
    }
  })
}

/**
 * 通用文件上传接口 - 用于上传压缩包文件
 * @param {FormData} data - 包含文件数据的FormData对象
 * @returns {Promise}
 */
export function uploadFile(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 添加卫星数据信息 - 用于提交上传的卫星数据元数据
 * @param {Object} data - 卫星数据元数据
 * @param {string} data.collectTime - 采集时间
 * @param {string} data.province - 省份
 * @param {string} data.city - 城市
 * @param {string} data.sourcefilepath - 源文件路径（上传后返回的路径）
 * @param {string} data.sourcefilename - 源文件名称
 * @param {string} data.image - 缩略图路径（可选）
 * @returns {Promise}
 */
export function addSatelliteInfo(data) {
  return request({
    url: '/map/satellite/add',
    method: 'post',
    data: data
  })
}

/**
 * 批量添加卫星数据信息
 * @param {Array} dataList - 卫星数据元数据数组
 * @returns {Promise}
 */
export function addBatchSatelliteInfo(dataList) {
  return request({
    url: '/map/satellite/addBatch',
    method: 'post',
    data: {
      list: dataList
    }
  })
}
