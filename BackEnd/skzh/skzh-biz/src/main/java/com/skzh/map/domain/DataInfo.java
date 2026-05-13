package com.skzh.map.domain;

import com.skzh.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class DataInfo {
    private  Integer id;

    private String value;

    private String label;

    private String dictValue;

    private List<DataInfo> children;


}
