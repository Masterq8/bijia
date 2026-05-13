package com.skzh.collection.service.impl;

import java.util.List;
        import com.skzh.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skzh.collection.mapper.CollectionInfoMapper;
import com.skzh.collection.domain.CollectionInfo;
import com.skzh.collection.service.ICollectionInfoService;

/**
 * 收藏Service业务层处理
 *
 * @author 朱继龙
 * @date 2024-09-02
 */
@Service
public class CollectionInfoServiceImpl implements ICollectionInfoService {
    @Autowired
    private CollectionInfoMapper collectionInfoMapper;

    /**
     * 查询收藏
     *
     * @param id 收藏主键
     * @return 收藏
     */
    @Override
    public CollectionInfo selectCollectionInfoById(Long id) {
        return collectionInfoMapper.selectCollectionInfoById(id);
    }

    /**
     * 查询收藏列表
     *
     * @param collectionInfo 收藏
     * @return 收藏
     */
    @Override
    public List<CollectionInfo> selectCollectionInfoList(CollectionInfo collectionInfo) {
        return collectionInfoMapper.selectCollectionInfoList(collectionInfo);
    }

    /**
     * 新增收藏
     *
     * @param collectionInfo 收藏
     * @return 结果
     */
    @Override
    public int insertCollectionInfo(CollectionInfo collectionInfo) {
                collectionInfo.setCreateTime(DateUtils.getNowDate());
            return collectionInfoMapper.insertCollectionInfo(collectionInfo);
    }

    /**
     * 修改收藏
     *
     * @param collectionInfo 收藏
     * @return 结果
     */
    @Override
    public int updateCollectionInfo(CollectionInfo collectionInfo) {
                collectionInfo.setUpdateTime(DateUtils.getNowDate());
        return collectionInfoMapper.updateCollectionInfo(collectionInfo);
    }

    /**
     * 批量删除收藏
     *
     * @param ids 需要删除的收藏主键
     * @return 结果
     */
    @Override
    public int deleteCollectionInfoByIds(Long[] ids) {
        return collectionInfoMapper.deleteCollectionInfoByIds(ids);
    }

    /**
     * 删除收藏信息
     *
     * @param id 收藏主键
     * @return 结果
     */
    @Override
    public int deleteCollectionInfoById(Long id) {
        return collectionInfoMapper.deleteCollectionInfoById(id);
    }
}
