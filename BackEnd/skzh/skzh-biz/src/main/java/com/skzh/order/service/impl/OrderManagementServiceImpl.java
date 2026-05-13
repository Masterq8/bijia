package com.skzh.order.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.skzh.common.core.domain.model.LoginUser;
import com.skzh.common.utils.DateUtils;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.order.domain.OrderManagement;

import com.skzh.order.mapper.OrderManagementMapper;
import com.skzh.order.service.OrderManagementService;
import com.skzh.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 订单管理Service业务层处理
 *
 * @author skzh
 * @date 2024-09-02
 */
@Service
public class OrderManagementServiceImpl implements OrderManagementService {
    @Autowired
    private OrderManagementMapper orderManagementMapper;
    @Autowired
    private ISysConfigService configService;

//    /**
//     * 查询订单管理
//     *
//     * @param id 订单管理主键
//     * @return 订单管理
//     */
//    @Override
//    public OrderManagement selectorderManagementById(Long id) {
//        return orderManagementMapper.selectorderManagementById(id);
//    }

    @Override
    public OrderManagement selectorderManagementById(Long id) {
        return null;
    }

    /**
     * 查询单个用户订单管理列表
     *
     * @param orderManagement 订单管理
     * @return 订单管理
     */
    @Override
    public List<OrderManagement> selectorderManagementList(OrderManagement orderManagement) {
        return orderManagementMapper.selectorderManagementList(orderManagement);
    }

    /**
     * 查询全部订单管理列表
     *
     * @param orderManagement 订单管理
     * @return 订单管理
     */
    @Override
    public List<OrderManagement> selectorderManagementLists(OrderManagement orderManagement) {
        return orderManagementMapper.selectorderManagementLists(orderManagement);
    }

    /**
     * 新增订单管理
     *
     * @param orderManagement 订单管理
     * @return 结果
     */
    @Override
    public int insertorderManagement(OrderManagement orderManagement) {
                orderManagement.setCreateTime(DateUtils.getNowDate());
            return orderManagementMapper.insertorderManagement(orderManagement);
    }

    /**
     * 修改订单管理
     *
     * @param orderManagement 订单管理
     * @return 结果
     */
    @Override
    public int updateorderManagement(OrderManagement orderManagement) {
                orderManagement.setUpdateTime(DateUtils.getNowDate());
        return orderManagementMapper.updateorderManagement(orderManagement);
    }

    /**
     * 批量删除订单管理
     *
     * @param ids 需要删除的订单管理主键
     * @return 结果
     */
    @Override
    public int deleteorderManagementByIds(Long[] ids) {
        return orderManagementMapper.deleteorderManagementByIds(ids);
    }

    /**
     * 删除订单管理信息
     *
     * @param id 订单管理主键
     * @return 结果
     */
    @Override
    public int deleteorderManagementById(Long id) {
        return orderManagementMapper.deleteorderManagementById(id);
    }

    @Override
    public List<OrderManagement> selectorderManagementListByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("请勾选数据");
        }
        return orderManagementMapper.selectorderManagementListByIds(ids);
    }

    /**
     * 生成订单编号
     * account 用户账号；tTag 订单类型（AIP 数据包；GTM 专题图）
     */

    public static String IDGrn(String account, String tTag) throws NoSuchAlgorithmException {
        //获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = now.format(formatter);


        //使用MessageDigest生成MD5哈希值
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(account.getBytes());
        byte[] digest = md.digest();
        StringBuilder hash = new StringBuilder();
        for (byte b : digest){
            hash.append(String.format("%02x",b));
        }

        //取前4位哈希值
        String customString = hash.substring(0,4);

        //生成4位随机数
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[2];
        random.nextBytes(randomBytes);
        StringBuilder randomString = new StringBuilder();
        for(byte b : randomBytes){
            randomString.append(String.format("%02x",b));
        }

        //如果tTag未定义，默认为"AIP"
        tTag = ((tTag == null)||tTag.isEmpty()) ? "AIP" :tTag;


        return dateTimeString + customString + randomString + tTag;
    }

    /**
     * 专题图生成订单
     *
     * @param orderManagement 订单管理
     * @return 结果
     */
    @Override
    public int addMake(OrderManagement orderManagement) throws NoSuchAlgorithmException {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String accout = String.valueOf(loginUser.getUser());
        String tTag = "GTM";
        orderManagement.setOrderNumber(IDGrn(accout, tTag));
        orderManagement.setAccount(loginUser.getUsername());
        orderManagement.setUserId(loginUser.getUserId());
        orderManagement.setCategory(tTag);
        orderManagement.setOrderTime(DateUtils.getNowDate());
        orderManagement.setThumbnail(orderManagement.getImageCategory() + "提取");
        orderManagement.setOrderDetail("待处理数据"+orderManagement.getOrderDetail()+"提取操作");
        orderManagement.setType(orderManagement.getCategory());
        orderManagement.setFilepath(orderManagement.getImage());
        orderManagement.setSensorType(orderManagement.getSensorType());
        orderManagement.setSatelliteType(orderManagement.getSatelliteType());
        orderManagement.setPrice(configService.selectConfigByKey("The_priceOfThe_order"));

        return orderManagementMapper.addMake(orderManagement);
    }

}
