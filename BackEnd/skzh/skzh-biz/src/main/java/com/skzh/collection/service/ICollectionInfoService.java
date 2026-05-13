package com.skzh.collection.service;

import java.util.List;

import com.skzh.collection.domain.CollectionInfo;

/**
 * 收藏Service接口
 *
 * @author 朱继龙
 * @date 2024-09-02
 */
public interface ICollectionInfoService {
    /**
     * 查询收藏
     *
     * @param id 收藏主键
     * @return 收藏
     */
    public CollectionInfo selectCollectionInfoById(Long id);

    /**
     * 查询收藏列表
     *
     * @param collectionInfo 收藏
     * @return 收藏集合
     */
    public List<CollectionInfo> selectCollectionInfoList(CollectionInfo collectionInfo);

    /**
     * 新增收藏
     *
     * @param collectionInfo 收藏
     * @return 结果
     */
    public int insertCollectionInfo(CollectionInfo collectionInfo);

    /**
     * 修改收藏
     *
     * @param collectionInfo 收藏
     * @return 结果
     */
    public int updateCollectionInfo(CollectionInfo collectionInfo);

    /**
     * 批量删除收藏
     *
     * @param ids 需要删除的收藏主键集合
     * @return 结果
     */
    public int deleteCollectionInfoByIds(Long[] ids);

    /**
     * 删除收藏信息
     *
     * @param id 收藏主键
     * @return 结果
     */
    public int deleteCollectionInfoById(Long id);
}
