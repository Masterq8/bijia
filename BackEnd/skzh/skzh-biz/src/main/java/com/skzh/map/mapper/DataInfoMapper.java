package com.skzh.map.mapper;

import com.skzh.map.domain.DataInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataInfoMapper {


    public List<DataInfo> selectDataInfoListByType(@Param("dictType") String dictType);
}

