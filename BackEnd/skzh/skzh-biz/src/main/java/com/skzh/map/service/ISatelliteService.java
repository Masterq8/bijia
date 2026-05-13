package com.skzh.map.service;

import java.util.List;

import com.skzh.map.domain.Satellite;

/**
 * 卫星影像管理Service接口
 *
 * @author zr
 * @date 2024-09-03
 */
public interface ISatelliteService {
    /**
     * 查询卫星影像管理
     *
     * @param id 卫星影像管理主键
     * @return 卫星影像管理
     */
    public Satellite selectSatelliteById(Long id);

    /**
     * 查询卫星影像管理列表
     *
     * @param satellite 卫星影像管理
     * @return 卫星影像管理集合
     */
    public List<Satellite> selectSatelliteList(Satellite satellite);

    /**
     * 新增卫星影像管理
     *
     * @param satellite 卫星影像管理
     * @return 结果
     */
    public int insertSatellite(Satellite satellite);

    /**
     * 修改卫星影像管理
     *
     * @param satellite 卫星影像管理
     * @return 结果
     */
    public int updateSatellite(Satellite satellite);

    /**
     * 批量删除卫星影像管理
     *
     * @param ids 需要删除的卫星影像管理主键集合
     * @return 结果
     */
    public int deleteSatelliteByIds(Long[] ids);

    /**
     * 删除卫星影像管理信息
     *
     * @param id 卫星影像管理主键
     * @return 结果
     */
    public int deleteSatelliteById(Long id);

    /**
     * 获得加入我的数据id
     * @return
     */
    public List<Satellite> getListInIds(List<Long> ids);

    List<Satellite> selectSatelliteList1(Satellite satellite);

    /**
     * @Description 更新中心点坐标
     * @Author wangq
     * @Date 2024/11/8 10:58
     * @Param [id, centerLat, centerLng]
     * @return
     **/
    public void updateCenterPoint(Long id, double centerLat, double centerLng);

}
