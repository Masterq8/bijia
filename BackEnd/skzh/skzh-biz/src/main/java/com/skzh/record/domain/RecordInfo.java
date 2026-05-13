package com.skzh.record.domain;

import java.math.BigDecimal;
import java.security.interfaces.DSAKey;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skzh.map.domain.Satellite;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.skzh.common.annotation.Excel;
import com.skzh.common.core.domain.BaseEntity;

/**
 * 我的数据对象 record_info
 *
 * @author skzh
 * @date 2024-09-03
 */
@Data
public class RecordInfo extends BaseEntity
        {
private static final long serialVersionUID=1L;

    /** id */
    private Long id;

    /** 图像名称 */
    @Excel(name = "图像名称" )
    private String imageName;

    /** 图像速览 */
    @Excel(name = "图像速览" )
    private String image;

    /** 地理位置 */
    @Excel(name = "省份" )
    private String province;

    /* 城市*/
    private String city;

    /** 左上角经度 */
    private BigDecimal leftupLongitude;
    /** 左上角纬度*/
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

    /* 景path*/
    private Long viewPath;

    /*景row*/
    private Long viewRow;

    /*星下点path*/
    private Long starPath;

    /*星下点row*/
    private Long starRow;

    public String getSourcefilepath() {
        return sourcefilepath;
    }

    public void setSourcefilepath(String sourcefilepath) {
        this.sourcefilepath = sourcefilepath;
    }

    private String sourcefilepath;


    /** 采集开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Excel(name = "采集开始时间" , width = 30, dateFormat = "yyyy-MM-dd" )
    private Date startTime;

    /** 采集结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Excel(name = "采集结束时间" , width = 30, dateFormat = "yyyy-MM-dd" )
    private Date endTime;

    /** 卫星类型 */
    @Excel(name = "卫星类型" )
    private String satelliteType;

    /** 传感器类型 */
    @Excel(name = "传感器类型" )
    private String sensorType;

    /*地址 */
    private String region;

    /*采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd" )
    @Excel(name = "采集时间" , width = 30, dateFormat = "yyyy-MM-dd" )
    private Date collectTime;

    /** 使用账户 */
//    @Excel(name = "使用账户" )
    private Long userId;
            /*卫星类型名称*/
    private String satelliteName;
            /*传参用：卫星ids*/
    private List<Long> satelliteIds;
            /*卫星id*/
    private Long satelliteId;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }



            public List<Long> getSatelliteIds() {
                return satelliteIds;
            }

            public void setSatelliteIds(List<Long> satelliteIds) {
                this.satelliteIds = satelliteIds;
            }

            /*卫星名称*/
            public String getSatelliteName() {
                return satelliteName;
            }

            public void setSatelliteName(String satelliteName) {
                this.satelliteName = satelliteName;
            }

            public void setId(Long id)
            {
            this.id = id;
            }

            public Long getId()
            {
            return id;
            }

            public String getImageName() {
                return imageName;
            }

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public BigDecimal getLeftupLongitude() {
                return leftupLongitude;
            }

            public void setLeftupLongitude(BigDecimal leftupLongitude) {
                this.leftupLongitude = leftupLongitude;
            }

            public BigDecimal getLeftupLatitude() {
                return leftupLatitude;
            }

            public void setLeftupLatitude(BigDecimal leftupLatitude) {
                this.leftupLatitude = leftupLatitude;
            }

            public BigDecimal getRightdownLongitude() {
                return rightdownLongitude;
            }

            public void setRightdownLongitude(BigDecimal rightdownLongitude) {
                this.rightdownLongitude = rightdownLongitude;
            }

            public BigDecimal getRightdownLatitude() {
                return rightdownLatitude;
            }

            public void setRightdownLatitude(BigDecimal rightdownLatitude) {
                this.rightdownLatitude = rightdownLatitude;
            }

            public Long getViewPath() {
                return viewPath;
            }

            public void setViewPath(Long viewPath) {
                this.viewPath = viewPath;
            }

            public Long getViewRow() {
                return viewRow;
            }

            public void setViewRow(Long viewRow) {
                this.viewRow = viewRow;
            }

            public Long getStarPath() {
                return starPath;
            }

            public void setStarPath(Long starPath) {
                this.starPath = starPath;
            }

            public Long getStarRow() {
                return starRow;
            }

            public void setStarRow(Long starRow) {
                this.starRow = starRow;
            }

            public String getSatelliteType() {
                return satelliteType;
            }

            public void setSatelliteType(String satelliteType) {
                this.satelliteType = satelliteType;
            }

            public String getSensorType() {
                return sensorType;
            }

            public void setSensorType(String sensorType) {
                this.sensorType = sensorType;
            }

            public void setLeftdownLongitude(BigDecimal leftdownLongitude)
            {
            this.leftdownLongitude = leftdownLongitude;
            }

    public BigDecimal getLeftdownLongitude()
            {
            return leftdownLongitude;
            }
    public void setLeftdownLatitude(BigDecimal leftdownLatitude)
            {
            this.leftdownLatitude = leftdownLatitude;
            }

    public BigDecimal getLeftdownLatitude()
            {
            return leftdownLatitude;
            }
    public void setRightupLongitude(BigDecimal rightupLongitude)
            {
            this.rightupLongitude = rightupLongitude;
            }

    public BigDecimal getRightupLongitude()
            {
            return rightupLongitude;
            }
    public void setRightupLatitude(BigDecimal rightupLatitude)
            {
            this.rightupLatitude = rightupLatitude;
            }

    public BigDecimal getRightupLatitude()
            {
            return rightupLatitude;
            }
//    public void setStartTime(Date startTime)
//            {
//            this.startTime = startTime;
//            }
//
//    public Date getStartTime()
//            {
//            return startTime;
//            }
//    public void setEndTime(Date endTime)
//            {
//            this.endTime = endTime;
//            }
//
//    public Date getEndTime()
//            {
//            return endTime;
//            }


            public Long getUserId() {
                return userId;
            }

            public void setUserId(Long userId) {
                this.userId = userId;
            }

            @Override
public String toString(){
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id" ,getId())
            .append("imageName" ,getImageName())
            .append("imagePath" ,getImage())
            .append("province", getProvince())
            .append("city", getCity())
            .append("leftdownLongitude" ,getLeftdownLongitude())
            .append("leftdownLatitude" ,getLeftdownLatitude())
            .append("rightupLongitude" ,getRightupLongitude())
            .append("rightupLatitude" ,getRightupLatitude())
            .append("startTime" ,getStartTime())
            .append("endTime" ,getEndTime())
            .append("satelliteId" ,getSatelliteType())
            .append("sensorId" ,getSensorType())
            .append("userId" ,getUserId())
            .append("createBy" ,getCreateBy())
            .append("createTime" ,getCreateTime())
            .append("updateBy" ,getUpdateBy())
            .append("updateTime" ,getUpdateTime())
            .append("viewPath", getViewPath())
            .append("viewRow",getViewRow())
            .append("starPath",getStarPath())
            .append("starRow",getStarRow())
            .append("satelliteIds",getSatelliteIds())
            .append("region",getRegion())
            .append("collectTime",getCollectTime())
        .toString();
        }
        }
