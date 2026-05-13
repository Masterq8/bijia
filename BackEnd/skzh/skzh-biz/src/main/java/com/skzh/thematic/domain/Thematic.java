package com.skzh.thematic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skzh.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class Thematic extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 专题图名称
     */
    private String imageName;

    /**
     * 专题图类型
     */
    private String imageCategory;

    /**
     * 类型编码
     */
    private String categoryCode;

    /**
     * 专题图
     */
    private String image;

    /**
     * 专题图备注信息
     */
    private String remarksInformation;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;
    /**
     * 创建人
     */
    private String createBy;

    private String image2;
}
