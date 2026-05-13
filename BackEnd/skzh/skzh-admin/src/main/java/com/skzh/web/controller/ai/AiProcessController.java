package com.skzh.web.controller.ai;
import com.skzh.kit.*;
import com.skzh.aiProcess.domain.zhuantituVo;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.order.domain.OrderInfo;
import com.skzh.web.controller.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/aiprocess")
public class AiProcessController {
    private static final Logger log = LoggerFactory.getLogger(AiProcessController.class);
    @Autowired
    private haianxianRouter haianxianRouter;

    @Autowired
    private yangzhiquRouter yangzhiquRouter;

    @Autowired
    private maweizaoRouter maweizaoRouter;

    @Autowired
    private mapRouter mapRouter;

    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private hutaiRouter hutaiRouter;
    @Autowired
    private hutai2Router hutai2Router;

    @Autowired
    private maweizao2Router maweizao2Router;

    @Autowired
    private redtideRouter redtideRouter;

    @PostMapping("/generateAIP")
    public AjaxResult aiProcess_AIP(@RequestBody OrderInfo orderInfo) throws Exception {
        log.info("orderInfo: {}", orderInfo);
        if ("1".equals(orderInfo.getType())) {
            AjaxResult success = haianxianRouter.shorelineDetection(orderInfo.getId(), SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
            // haianxian
        } else if ("2".equals(orderInfo.getType())) {
            AjaxResult success = yangzhiquRouter.yangzhiDetection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else if ("3".equals(orderInfo.getType())) {
            AjaxResult success = hutaiRouter.hutaiDetection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else if ("5".equals(orderInfo.getType())) {
            AjaxResult success = redtideRouter.redtideDetection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else if ("6".equals(orderInfo.getType())) {
            AjaxResult success = maweizaoRouter.maweizaoDetection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else if ("10".equals(orderInfo.getType())) {
            AjaxResult success = maweizao2Router.mawizao2Detection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else if ("9".equals(orderInfo.getType())) {
            AjaxResult success = hutai2Router.hutai2Detection(orderInfo.getId(),SecurityUtils.getLoginUser().getUser());
            webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
            return success;
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/generateGTM")
    public AjaxResult aiProcess_MIP(@RequestBody zhuantituVo data) throws Exception {

        AjaxResult success = mapRouter.zhuantituGen(data, SecurityUtils.getLoginUser().getUser());
//        webSocketServer.sendMessageByUserName(SecurityUtils.getLoginUser().getUsername(), "任务完成，详情请查看结果文件");
        return success;
    }




}