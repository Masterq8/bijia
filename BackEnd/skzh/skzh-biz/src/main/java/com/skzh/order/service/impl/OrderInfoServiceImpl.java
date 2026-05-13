package com.skzh.order.service.impl;

import com.skzh.order.domain.OrderInfo;
import com.skzh.order.mapper.OrderInfoMapper;
import com.skzh.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
    @author:郑瑞
    @description:订单信息Service业务处理层
*/
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

     /**
     * 查询订单信息列表
     * */
    @Override
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo) {
        return orderInfoMapper.selectOrderInfoList(orderInfo);
    }
    @Override
    public List<Map<String, Object>> listMain() {
        return orderInfoMapper.listMain();
    }

    @Override
    public OrderInfo getOrderById(Long id) {
        return orderInfoMapper.getOrderById(id);
    }

    @Override
    public void updateIsShow(Long id, String isShow) {
        orderInfoMapper.updateIsShow(id, isShow);
    }

}
