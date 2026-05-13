package com.skzh.web.controller.orderManagement;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.framework.config.ServerConfig;
import com.skzh.order.domain.OrderInfo;
import com.skzh.order.domain.OrderManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skzh.common.annotation.Log;
import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.enums.BusinessType;
import com.skzh.order.service.OrderManagementService;
import com.skzh.common.utils.poi.ExcelUtil;
import com.skzh.common.core.page.TableDataInfo;

/**
 * 订单管理Controller
 *
 * @author skzh
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/orderManagement/orderManagement" )
public class OrderManagementController extends BaseController {
    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询订单管理列表
     */
    @GetMapping("/list" )
    public TableDataInfo list(OrderManagement orderManagement) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<OrderManagement> list = null;
        startPage();
        if ("common".equals(user.getRoles().get(0).getRoleKey())) {
            orderManagement.setUserId(user.getUserId());
            list = orderManagementService.selectorderManagementList(orderManagement);
        }else {
            list = orderManagementService.selectorderManagementLists(orderManagement);
        }
        if (!CollectionUtils.isEmpty(list)) {
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            for (OrderManagement info:list) {
                if (info.getThumbnail() != null && info.getThumbnail().indexOf(filePath) >= 0) {
                    String thumbnail = info.getThumbnail();
                    thumbnail = thumbnail.replace(filePath, prefixUrl+"/profile/upload");
                    info.setThumbnailUrl(thumbnail);
                }
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/list2" )
    public TableDataInfo list2(OrderManagement orderManagement) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        orderManagement.setUserId(user.getUserId());
        startPage();
        List<OrderManagement> list = orderManagementService.selectorderManagementList(orderManagement);
        if (!CollectionUtils.isEmpty(list)) {
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            for (OrderManagement info:list) {
                if (info.getThumbnail() != null && info.getThumbnail().indexOf(filePath) >= 0) {
                    String thumbnail = info.getThumbnail();
                    thumbnail = thumbnail.replace(filePath, prefixUrl+"/profile/upload");
                    info.setThumbnailUrl(thumbnail);
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出订单管理列表
     */
    @Log(title = "订单管理" , businessType = BusinessType.EXPORT)
    @PostMapping("/export" )
    public void export(HttpServletResponse response, OrderManagement orderManagement) {
        List<OrderManagement> list = orderManagementService.selectorderManagementList(orderManagement);
        ExcelUtil<OrderManagement> util = new ExcelUtil<OrderManagement>(OrderManagement. class);
        util.exportExcel(response, list, "订单管理数据" );
    }

    /**
     *导出选中行数据
     */
    @Log(title = "订单管理" , businessType = BusinessType.EXPORT)
    @PostMapping("/export1" )
    public void export1(HttpServletResponse response, OrderManagement orderManagement) {
        System.out.println("orderManagement: " + orderManagement);
        List<Integer> ids = orderManagement.getIds();
        System.out.println("ids:" + ids);
        List<OrderManagement> list = orderManagementService.selectorderManagementListByIds(ids);
        ExcelUtil<OrderManagement> util = new ExcelUtil<OrderManagement>(OrderManagement. class);
        util.exportExcel(response, list, "订单管理数据" );
    }

    /**
     * 获取订单管理详细信息
     */
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return success(orderManagementService.selectorderManagementById(id));
    }

    /**
     * 新增订单管理
     */
    @Log(title = "订单管理" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderManagement orderManagement) {
        return toAjax(orderManagementService.insertorderManagement(orderManagement));
    }

    /**
     * 修改订单管理
     */
    @Log(title = "订单管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderManagement orderManagement) {
        return toAjax(orderManagementService.updateorderManagement(orderManagement));
    }

    /**
     * 删除订单管理
     */
    @Log(title = "订单管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(orderManagementService.deleteorderManagementByIds(ids));
    }

    /**
     * 专题图制作生成订单
     */
    @Log(title = "订单管理" , businessType = BusinessType.DELETE)
    @PostMapping("/addMake")
    public AjaxResult ajaxResult(@RequestBody OrderManagement orderManagement) {
        try {
            return toAjax(orderManagementService.addMake(orderManagement));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
