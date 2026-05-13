package com.skzh.collection.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.skzh.common.annotation.Excel;
import com.skzh.common.core.domain.BaseEntity;

/**
 * 收藏对象 collection_info
 *
 * @author 朱继龙
 * @date 2024-09-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CollectionInfo extends BaseEntity {
    private static final long serialVersionUID=1L;

    /** id主键 */
    private Long id;
/*
    *//*图像名称 *//*
            @Excel(name = "图像名称" )
    private String name;*/

    /** 图像速览 */
    @Excel(name = "图像速览" )
    private String image;

     /** 用户Id */
    private long userId;

    /** 地理位置 */
    @Excel(name = "地理位置" )
    private String region;

    /** 经纬度范围 */
    @Excel(name = "经纬度范围" )
    private String extent;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd" )
    @Excel(name = "采集时间" , width = 30, dateFormat = "yyyy-MM-dd" )
    private Date collectTime;

    /** 采集开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Excel(name = "采集开始时间" , width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss" )
    private Date startTime;

    /** 采集结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Excel(name = "采集结束时间" , width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss" )
    private Date endTime;

    /** 卫星类型 */
    @Excel(name = "卫星类型" )
    private String satelliteType;

    /** 传感器类型 */
    @Excel(name = "传感器类型" )
    private String sensorType;

    /** 接收器ID */
    @Excel(name = "接收器ID" )
    private String receivingId;

    /** 景Path */
    @Excel(name = "景Path" )
    private String viewPath;

    /** 景Row */
    @Excel(name = "景Row" )
    private String viewRow;

    /** 星下点Path */
    @Excel(name = "星下点Path" )
    private String starPath;

    /** 星下点Row */
    @Excel(name = "星下点Row" )
    private String starRow;

    /** 左上角纬度 */
    @Excel(name = "左上角纬度" )
    private String leftupLatitude;

    /** 左上角经度 */
    @Excel(name = "左上角经度" )
    private String leftupLongitude;

    /** 左下角纬度 */
    @Excel(name = "左下角纬度" )
    private String leftdownLatitude;

    /** 左下角经度 */
    @Excel(name = "左下角经度" )
    private String leftdownLongitude;

    /** 右上角纬度 */
    @Excel(name = "右上角纬度" )
    private String rightupLatitude;

    /** 右上角经度 */
    @Excel(name = "右上角经度" )
    private String rightupLongitude;

    /** 右下角纬度 */
    @Excel(name = "右下角纬度" )
    private String rightdownLatitude;

    /** 右下角经度 */
    @Excel(name = "右下角经度" )
    private String rightdownLongitude;

    private String province;

    private String city;

    private Long satelliteId;// 卫星id

}
