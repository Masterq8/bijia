package com.skzh.visits.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.skzh.order.domain.OrderInfo;


import java.util.List;
import java.util.Map;


public interface visitsMapper extends BaseMapper<OrderInfo> {

    List<Map<String, Object>> getDailyVisits();

}
