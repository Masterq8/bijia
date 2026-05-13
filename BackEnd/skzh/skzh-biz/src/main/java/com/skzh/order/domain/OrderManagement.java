package com.skzh.order.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skzh.common.core.domain.entity.SysUser;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.skzh.common.annotation.Excel;
import com.skzh.common.core.domain.BaseEntity;

/**
 * 订单管理对象 orders
 *
 * @author skzh
 * @date 2024-09-02
 */
@Data
public class OrderManagement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    private Long id;

    private List<Integer> ids; // 存储ID数组

    private Integer pageNum;


    private Integer pageSize;



    /**
     * 产品订单名称
     */
    @Excel(name = "产品订单名称")
    private String orderName;

    /**
     * 订单详情
     */
    @Excel(name = "订单详情")
    private String orderDetail;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private String price;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下单时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;


    /**
     * 订单图片
     */
    private String image;

    /**
     * 是否展示（0:否；1：是）
     */
    private String isShow;

    /**
     * 专题图标题
     */
    private String title;

    /**
     * 产品ID
     */
    private String prodId;

    /**
     * 专题图类别
     */
    private String category;
    /**
     * 卫星类别
     */
    private String satelliteType;
    /**
     * 传感器类别
     */
    private String sensorType;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String state;

    /**
     * 所属用户账户
     */
    private String account;

    /**
     * 专题图png图片
     */
    private String thumbnail;

    /**
     * 专题图tif文件
     */
    private String filepath;

    /**
     * 专题图表格专题图名称
     */
    private String imageName;

    /**
     * 专题图类型
     */
    private String imageCategory;
    /**
     * 类型
     */
    private String type;

    /**
     * 遥感图像id
     */
    private String mapid;


    /**
     * .shp文件
     */
    private String rawfile;


    private Long userId;

    private String thumbnailUrl;

    private String stateName;
}
