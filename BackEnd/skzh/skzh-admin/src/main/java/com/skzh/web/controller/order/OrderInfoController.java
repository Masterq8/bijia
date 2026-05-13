package com.skzh.web.controller.order;
/*
    @author:郑瑞
    @description:订单信息controller
*/

import com.skzh.common.annotation.Log;
import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.page.TableDataInfo;
import com.skzh.common.enums.BusinessType;
import com.skzh.common.utils.StringUtils;
import com.skzh.common.utils.file.FileUtils;
import com.skzh.framework.config.ServerConfig;
import com.skzh.order.domain.OrderInfo;
import com.skzh.order.service.OrderInfoService;
import com.skzh.web.controller.common.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/order/orderInfo")
public class OrderInfoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询订单信息列表
     * */
    @RequestMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String type) {
//        startPage();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setType(type);
        List<OrderInfo> list = orderInfoService.selectOrderInfoList(orderInfo);
        if (!CollectionUtils.isEmpty(list)) {
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            prefixUrl = prefixUrl.replace("http", "https");
            System.out.println(prefixUrl);
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            for (OrderInfo info:list) {
                if (info.getThumbnail() != null && info.getThumbnail().indexOf(filePath) >= 0) {
                    String thumbnail = info.getThumbnail();
                    thumbnail = thumbnail.replace(filePath, prefixUrl+"/profile/upload");
                    info.setImageUrl(thumbnail);
                }
                if (info.getFilepath() != null && info.getFilepath().indexOf(filePath) >= 0) {
                    String fileUrl = info.getFilepath();
                    fileUrl = fileUrl.replace(filePath, prefixUrl+"/profile/upload");
                    info.setTifUrl(fileUrl);
                }
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/main/list")
    public AjaxResult listMain() {
        return AjaxResult.success(orderInfoService.listMain());
    }

    /**
     * 订单下载请求
     *
     * @param id 文件名称
     */
    @GetMapping("/download" )
    public void fileDownload(Long id, HttpServletResponse response) {
        try {
            OrderInfo orderInfo = orderInfoService.getOrderById(id);
            if (orderInfo == null) {
                throw new Exception(StringUtils.format("订单信息不存在，无法下载。 "));
            } else {
                if (StringUtils.isEmpty(orderInfo.getFilepath())) {
                    throw new Exception(StringUtils.format("文件未生成，暂时无法下载。 "));
                }
            }
            String filePath = orderInfo.getFilepath();// 文件地址
            String realFileName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String fileUrl = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl().replace("http:", "https:");// https://bijianet.sdust.edu.cn/
            filePath = filePath.replace(fileUrl, prefixUrl+"/profile/upload");
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载文件失败" , e);
        }
    }

    /**
     * 设置产品展示
     */
    @Log(title = "设置产品展示" , businessType = BusinessType.UPDATE)
    @PutMapping("/showSet")
    public AjaxResult showSet(@RequestBody OrderInfo orderInfo) {
        OrderInfo info = orderInfoService.getOrderById(orderInfo.getId());
        if ("1".equals(orderInfo.getIsShow())) {// 设置为产品展示
            if (!"1".equals(info.getState())) {
                return AjaxResult.error("非已完成状态，不可进行产品展示");
            }
        }
        try {
            orderInfoService.updateIsShow(orderInfo.getId(), orderInfo.getIsShow());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success("操作成功");
    }
}
