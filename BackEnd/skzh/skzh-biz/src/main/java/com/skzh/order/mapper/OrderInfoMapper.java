package com.skzh.order.mapper;

import com.skzh.order.domain.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/*
    @author:郑瑞
    @description:订单信息Mapper接口
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo>{

    /**
     * 查询订单信息列表
     * */
    List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    List<Map<String, Object>> listMain();

    /**
     * @Description 根据id查询订单信息
     * @Author wangq
     * @Date 2024/9/29 15:56
     * @Param [id]
     * @return
     **/
    public OrderInfo getOrderById(@Param("id") Long id);

    /**
     * @Description 更新展示状态
     * @Author wangq
     * @Date 2024/10/17 11:03
     * @Param [id, isShow]
     * @return
     **/
    public void updateIsShow(@Param("id") Long id, @Param("isShow") String isShow);


    void addpurchase(OrderInfo data2DB);


    int updateById1(@Param("entity") OrderInfo data2DB);

    void updateById2(@Param("entity") OrderInfo result2DB);
}
