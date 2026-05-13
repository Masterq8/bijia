package com.skzh.record.service.impl;

import java.util.List;
        import com.skzh.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skzh.record.mapper.RecordInfoMapper;
import com.skzh.record.domain.RecordInfo;
import com.skzh.record.service.IRecordInfoService;

/**
 * 我的数据Service业务层处理
 *
 * @author skzh
 * @date 2024-09-03
 */
@Service
public class RecordInfoServiceImpl implements IRecordInfoService {
    @Autowired
    private RecordInfoMapper recordInfoMapper;

    /**
     * 查询我的数据
     *
     * @param id 我的数据主键
     * @return 我的数据
     */
    @Override
    public RecordInfo selectRecordInfoById(Long id) {
        return recordInfoMapper.selectRecordInfoById(id);
    }

    /**
     * 查询我的数据列表
     *
     * @param recordInfo 我的数据
     * @return 我的数据
     */
    @Override
    public List<RecordInfo> selectRecordInfoList(RecordInfo recordInfo) {
        return recordInfoMapper.selectRecordInfoList(recordInfo);
    }

    /**
     * 新增我的数据
     *
     * @param recordInfo 我的数据
     * @return 结果
     */
    @Override
    public int insertRecordInfo(RecordInfo recordInfo) {
                recordInfo.setCreateTime(DateUtils.getNowDate());
            return recordInfoMapper.insertRecordInfo(recordInfo);
    }

    /**
     * 批量加入我的数据
     * @param list
     */
    @Override
    public void saveBatch(List<RecordInfo> list) {
        recordInfoMapper.saveBatch(list);
    }


    /**
     * 修改我的数据
     *
     * @param recordInfo 我的数据
     * @return 结果
     */
    @Override
    public int updateRecordInfo(RecordInfo recordInfo) {
                recordInfo.setUpdateTime(DateUtils.getNowDate());
        return recordInfoMapper.updateRecordInfo(recordInfo);
    }

    /**
     * 批量删除我的数据
     *
     * @param ids 需要删除的我的数据主键
     * @return 结果
     */
    @Override
    public int deleteRecordInfoByIds(Long[] ids) {
        return recordInfoMapper.deleteRecordInfoByIds(ids);
    }

    /**
     * 删除我的数据信息
     *
     * @param id 我的数据主键
     * @return 结果
     */
    @Override
    public int deleteRecordInfoById(Long id) {
        return recordInfoMapper.deleteRecordInfoById(id);
    }
}
