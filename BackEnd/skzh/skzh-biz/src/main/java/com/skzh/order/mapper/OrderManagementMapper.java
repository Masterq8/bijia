package com.skzh.order.mapper;

import java.util.List;

import com.skzh.order.domain.OrderManagement;

import org.apache.ibatis.annotations.Param;

/**
 * 订单管理Mapper接口
 *
 * @author skzh
 * @date 2024-09-02
 */
public interface OrderManagementMapper {
    /**
     * 查询订单管理
     *
     * @param id 订单管理主键
     * @return 订单管理
     */
    public OrderManagement selectorderManagementById(Long id);

    /**
     * 查询单个用户订单管理列表
     *
     * @param orderManagement 订单管理
     * @return 订单管理集合
     */
    public List<OrderManagement> selectorderManagementList(OrderManagement orderManagement);

    /**
     * 查询全部订单管理列表
     *
     * @param orderManagement 订单管理
     * @return 订单管理集合
     */
    public List<OrderManagement> selectorderManagementLists(OrderManagement orderManagement);

    /**
     * 新增订单管理
     *
     * @param orderManagement 订单管理
     * @return 结果
     */
    public int insertorderManagement(OrderManagement orderManagement);

    /**
     * 修改订单管理
     *
     * @param orderManagement 订单管理
     * @return 结果
     */
    public int updateorderManagement(OrderManagement orderManagement);

    /**
     * 删除订单管理
     *
     * @param id 订单管理主键
     * @return 结果
     */
    public int deleteorderManagementById(Long id);

    /**
     * 批量删除订单管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteorderManagementByIds(Long[] ids);

    List<OrderManagement> selectorderManagementListByIds(@Param("ids") List<Integer> ids);

    public int addMake(OrderManagement orderManagement);
}
