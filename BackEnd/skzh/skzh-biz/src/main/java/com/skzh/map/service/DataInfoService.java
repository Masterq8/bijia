package com.skzh.map.service;

import com.skzh.map.domain.DataInfo;


import java.util.List;

public interface DataInfoService {

    public List<DataInfo> selectDataInfoListByType(String dictType);

}
