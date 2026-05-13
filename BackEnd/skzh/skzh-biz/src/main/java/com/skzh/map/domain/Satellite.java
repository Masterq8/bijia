package com.skzh.map.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.type.LatLng;
import com.skzh.map.dto.MapPoint;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.skzh.common.annotation.Excel;
import com.skzh.common.core.domain.BaseEntity;

/**
 * 卫星影像管理对象 satellite
 *
 * @author zr
 * @date 2024-09-03
 */
@Data
public class Satellite extends BaseEntity {

    private static final long serialVersionUID=1L;

    /** 主键 */
    @Excel(name = "主键" )
    private Long id;

    /** 地址 */
    @Excel(name = "地址" )
    private String region;

    /** 景path */
    @Excel(name = "景path" )
    private Long viewPath;

    /** 景row */
    @Excel(name = "景row" )
    private Long viewRow;

    /** 星下点path */
    @Excel(name = "星下点path" )
    private Long starPath;

    /** 星下点row */
    @Excel(name = "星下点row" )
    private Long starRow;

    /** 左上角经度 */
    @Excel(name = "左上角经度" )
    private BigDecimal leftupLongitude;

    /** 左上角纬度 */
    @Excel(name = "左上角纬度" )
    private BigDecimal leftupLatitude;

    /** 左下角经度 */
    @Excel(name = "左下角经度" )
    private BigDecimal leftdownLongitude;

    /** 左下角纬度 */
    @Excel(name = "左下角纬度" )
    private BigDecimal leftdownLatitude;

    /** 右上角经度 */
    @Excel(name = "右上角经度" )
    private BigDecimal rightupLongitude;

    /** 右上角纬度 */
    @Excel(name = "右上角纬度" )
    private BigDecimal rightupLatitude;

    /** 右下角经度 */
    @Excel(name = "右下角经度" )
    private BigDecimal rightdownLongitude;

    /** 右下角纬度 */
    @Excel(name = "右下角纬度" )
    private BigDecimal rightdownLatitude;

    /** 传感器类型 */
    @Excel(name = "传感器类型" )
    private String sensorType;

    /** 卫星类型 */
    @Excel(name = "卫星类型" )
    private String satelliteType;
    /** 卫星类型集合 */
    private List<String> satelliteTypes;

    @JsonFormat(pattern = "yyyy-MM-dd" )
    private Date startCollectTime;
    @JsonFormat(pattern = "yyyy-MM-dd" )
    private Date endCollectTime;

    /** 卫星图片 */
    @Excel(name = "卫星图片" )
    private String image;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd" )
    @Excel(name = "采集时间" , width = 30, dateFormat = "yyyy-MM-dd" )
    private Date collectTime;

    /** 省份*/
    @Excel(name = "省份" )
    private String province;

    /** 城市*/
    @Excel(name = "城市" )
    private String city;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acquisitiontime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivedtime;

    // 中心点维度
    private BigDecimal centerlatitude;

    // 中心点经度
    private BigDecimal centerlongitude;

    private String orbitid;

    private String sourcefilepath;

    private String sourcefilename;

    private String resolution;

    private String title;

    private String exInfo;

    private String sourcefilepathMl;// 目录path

    private List<String> points;// 自定义经纬度范围

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("region", getRegion())
                .append("viewPath", getViewPath())
                .append("viewRow", getViewRow())
                .append("starPath", getStarPath())
                .append("starRow", getStarRow())
                .append("leftupLongitude", getLeftupLongitude())
                .append("leftupLatitude", getLeftupLatitude())
                .append("leftdownLongitude", getLeftdownLongitude())
                .append("leftdownLatitude", getLeftdownLatitude())
                .append("rightupLongitude", getRightupLongitude())
                .append("rightupLatitude", getRightupLatitude())
                .append("rightdownLongitude", getRightdownLongitude())
                .append("rightdownLatitude", getRightdownLatitude())
                .append("sensorType", getSensorType())
                .append("satelliteType", getSatelliteType())
                .append("image", getImage())
                .append("collectTime", getCollectTime())
                .append("province", getProvince())
                .append("city", getCity())
                .append("starttime", getStarttime())
                .append("endtime", getEndtime())
                .append("acquisitiontime", getAcquisitiontime())
                .append("receivedtime", getReceivedtime())
                .append("centerlatitude", getCenterlatitude())
                .append("centerlongitude", getCenterlongitude())
                .append("orbitid", getOrbitid())
                .append("sourcefilepath", getSourcefilepath())
                .append("sourcefilename", getSourcefilename())
                .append("resolution", getResolution())
                .append("title", getTitle())
                .append("exInfo", getExInfo())
                .append("sourcefilepathMl", getSourcefilepathMl())
                .toString();
    }
}
