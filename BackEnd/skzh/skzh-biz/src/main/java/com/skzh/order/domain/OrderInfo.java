package com.skzh.order.domain;
/*
    @author:郑瑞
    @description:订单信息
*/

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skzh.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class OrderInfo extends BaseEntity {

    /** ID*/
    private Long id;

    /** 产品订单名称*/
    private String orderName;

    /** 订单详情*/
    private String orderDetail;

    /** 价格*/
    private Double price;

    /** 下单时间*/
    @JsonFormat(pattern = "yyyy-MM-dd" )
    private String orderTime;

    /** 订单图片*/
    private String image;

    /** 是否展示（0:否；1：是）*/
    private String isShow;

    /** 专题图标题*/
    private String title;

    /** 产品ID*/
    private String prodId;

    /** 专题图类别*/
    private String category;

    /** 类别*/
    private String type;

    /** 卫星类型*/
    private String satelliteType;

    //账户
    private String account;

    //python业务用id
    private String ywid;

    //生成专题图路径
    private String filepath;

    //专题图全称
    private String thumbnail;

    //用到的遥感图像id
    private Long mapid;

    //.shp文件
    private String rawfile;

    // 订单状态(0:生成中1:已完成2生成失败)
    private String state;

    //订单编号
    private String orderNumber;

    // 用户id
    private Long userId;

    private String imageUrl;

    private String filepathUrl;

    private String tifUrl;

    private String shpurl;
}
