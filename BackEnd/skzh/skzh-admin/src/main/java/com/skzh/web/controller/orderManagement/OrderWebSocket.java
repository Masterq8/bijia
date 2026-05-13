package com.skzh.web.controller.orderManagement;

import com.skzh.common.core.domain.AjaxResult;
import com.skzh.order.domain.OrderManagement;
import com.skzh.order.service.OrderManagementService;
import com.skzh.system.service.ISysMenuService;
import com.skzh.web.controller.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 第三方接口类
 * @author wangkun
 */
@RestController
@RequestMapping("/orderManagement/orderWebSocket")
public class OrderWebSocket {

    @Resource
    private WebSocketServer webSocketServer;

    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private ISysMenuService iSysMenuService;

    /**
     *   推送订单管理实时更新的数据
     */
    @PostMapping("/updateTable")
    public AjaxResult updateTable(String orderNumber, String status) {

        try{
            System.out.println("-----------------订单管理页面数据更新：" + orderNumber);
            OrderManagement orderManagement = new OrderManagement();
            orderManagement.setOrderName(orderNumber);
            List<OrderManagement> user = orderManagementService.selectorderManagementList(orderManagement);
            List<String> obj = iSysMenuService.findUsers("admin2");
            obj.add(user.get(0).getAccount());
            Map<String, Object> map = new HashMap<>();
            map.put("title", "订单状态更新提醒");
            map.put("type", "1");
            for (String abc : obj) {
                webSocketServer.sendObjMsg(abc, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("推送失败");
        }

        return AjaxResult.success("推送成功");
    }



}
