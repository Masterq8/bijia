package com.skzh.collection.mapper;

import java.util.List;

import com.skzh.collection.domain.CollectionInfo;

/**
 * 收藏Mapper接口
 *
 * @author 朱继龙
 * @date 2024-09-02
 */
public interface CollectionInfoMapper {
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
     * 删除收藏
     *
     * @param id 收藏主键
     * @return 结果
     */
    public int deleteCollectionInfoById(Long id);

    /**
     * 批量删除收藏
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCollectionInfoByIds(Long[] ids);
}
