package com.skzh.map.dto;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 10:49 2024/11/7
 **/
public class MapPoint {

    /**
     * 经度
     */
    private double lng;
    /**
     * 纬度
     */
    private double lat;

    public MapPoint() {

    }

    public MapPoint(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapPoint) {
            MapPoint mapPoint = (MapPoint) obj;
            return (mapPoint.getLng() == lng && mapPoint.getLat() == lat) ? true : false;
        } else {
            return false;
        }
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
