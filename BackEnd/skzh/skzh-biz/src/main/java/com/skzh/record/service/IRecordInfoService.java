package com.skzh.record.service;

import java.util.List;

import com.skzh.record.domain.RecordInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 我的数据Service接口
 *
 * @author skzh
 * @date 2024-09-03
 */
public interface IRecordInfoService {
    /**
     * 查询我的数据
     *
     * @param id 我的数据主键
     * @return 我的数据
     */
    public RecordInfo selectRecordInfoById(Long id);

    /**
     * 查询我的数据列表
     *
     * @param recordInfo 我的数据
     * @return 我的数据集合
     */
    public List<RecordInfo> selectRecordInfoList(RecordInfo recordInfo);

    /**
     * 新增我的数据
     *
     * @param recordInfo 我的数据
     * @return 结果
     */
    public int insertRecordInfo(RecordInfo recordInfo);

    /**
     * 批量加入我的数据
     * @param list
     */
    public void saveBatch(@Param("list")List<RecordInfo> list);

    /**
     * 修改我的数据
     *
     * @param recordInfo 我的数据
     * @return 结果
     */
    public int updateRecordInfo(RecordInfo recordInfo);

    /**
     * 批量删除我的数据
     *
     * @param ids 需要删除的我的数据主键集合
     * @return 结果
     */
    public int deleteRecordInfoByIds(Long[] ids);

    /**
     * 删除我的数据信息
     *
     * @param id 我的数据主键
     * @return 结果
     */
    public int deleteRecordInfoById(Long id);
}
