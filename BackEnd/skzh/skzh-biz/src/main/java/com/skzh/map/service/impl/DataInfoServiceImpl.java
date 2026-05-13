package com.skzh.map.service.impl;

import com.skzh.map.domain.DataInfo;
import com.skzh.map.mapper.DataInfoMapper;
import com.skzh.map.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataInfoServiceImpl implements DataInfoService {


    @Autowired
    private DataInfoMapper DataInfoMapper;

    @Override
    public List<DataInfo> selectDataInfoListByType(String dictType) {
        return DataInfoMapper.selectDataInfoListByType(dictType);
    }
}
