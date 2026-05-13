package com.skzh.order.service;

import com.skzh.order.domain.OrderInfo;

import java.util.List;
import java.util.Map;

/**
 * 订单信息Service接口
 *
 * @author 郑瑞
 * @date 2024-08-29
 * */
public interface OrderInfoService {

    /**
     * 查询订单信息列表
     * */
    List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    List<Map<String,Object>> listMain();

    /**
     * @Description 根据id查询订单信息
     * @Author wangq
     * @Date 2024/9/29 15:56
     * @Param [id]
     * @return
     **/
    public OrderInfo getOrderById(Long id);

    /**
     * @Description 更新展示状态
     * @Author wangq
     * @Date 2024/10/17 11:03
     * @Param [id, isShow]
     * @return
     **/
    public void updateIsShow(Long id, String isShow);

}
