package com.skzh.map.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
        import com.skzh.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skzh.map.mapper.SatelliteMapper;
import com.skzh.map.domain.Satellite;
import com.skzh.map.service.ISatelliteService;

/**
 * 卫星影像管理Service业务层处理
 *
 * @author zr
 * @date 2024-09-03
 */
@Service
public class SatelliteServiceImpl implements ISatelliteService {
    @Autowired
    private SatelliteMapper satelliteMapper;

    /**
     * 查询卫星影像管理
     *
     * @param id 卫星影像管理主键
     * @return 卫星影像管理
     */
    @Override
    public Satellite selectSatelliteById(Long id) {
        return satelliteMapper.selectSatelliteById(id);
    }

    /**
     * 查询卫星影像管理列表
     *
     * @param satellite 卫星影像管理
     * @return 卫星影像管理
     */
    @Override
    public List<Satellite> selectSatelliteList(Satellite satellite) {
        return satelliteMapper.selectSatelliteList(satellite);
    }

    /**
     * 新增卫星影像管理
     *
     * @param satellite 卫星影像管理
     * @return 结果
     */
    @Override
    public int insertSatellite(Satellite satellite) {
                satellite.setCreateTime(DateUtils.getNowDate());
            return satelliteMapper.insertSatellite(satellite);
    }

    /**
     * 修改卫星影像管理
     *
     * @param satellite 卫星影像管理
     * @return 结果
     */
    @Override
    public int updateSatellite(Satellite satellite) {
                satellite.setUpdateTime(DateUtils.getNowDate());
        return satelliteMapper.updateSatellite(satellite);
    }

    /**
     * 批量删除卫星影像管理
     *
     * @param ids 需要删除的卫星影像管理主键
     * @return 结果
     */
    @Override
    public int deleteSatelliteByIds(Long[] ids) {
        return satelliteMapper.deleteSatelliteByIds(ids);
    }

    /**
     * 删除卫星影像管理信息
     *
     * @param id 卫星影像管理主键
     * @return 结果
     */
    @Override
    public int deleteSatelliteById(Long id) {
        return satelliteMapper.deleteSatelliteById(id);
    }


    /**
     * 查找添加我的数据id
     * @param ids
     * @return
     */
    @Override
    public List<Satellite> getListInIds(List<Long> ids) {
        return satelliteMapper.getListInIds(ids);
    }

    public List<Satellite> selectSatelliteList1(Satellite satellite) {
//        List<Satellite> satellites = satelliteMapper.selectSatelliteList1(satellite);
//        List<Satellite> filteredSatellites = new ArrayList<>(); // 创建一个新的列表来存储满足条件的Satellite对象
//
//        // 假设startCollectTime和endCollectTime是Date类型
//        Date startCollectTime = satellite.getStartCollectTime(); // 起始时间
//        Date endCollectTime = satellite.getEndCollectTime();   // 结束时间

        // 遍历satellites列表
//        for (Satellite s : satellites) {
//            // 检查collectTime是否在startCollectTime和endCollectTime之间
//            s.getCollectTime();
//            if (s.getCollectTime() != null && !s.getCollectTime().before(startCollectTime) && !s.getCollectTime().after(endCollectTime)) {
//                // collectTime在指定时间范围内，可以进行相应的处理
//                // 例如，将其添加到一个新的列表中
//                 filteredSatellites.add(s);
//            }
//        }
//
//        // 如果你需要返回过滤后的列表，可以在这里返回filteredSatellites
//         return filteredSatellites;
        return satelliteMapper.selectSatelliteList1(satellite);
    }

    @Override
    public void updateCenterPoint(Long id, double centerLat, double centerLng) {
        satelliteMapper.updateCenterPoint(id, centerLat, centerLng);
    }
}
