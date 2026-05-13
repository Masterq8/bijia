package com.skzh.kit;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.map.domain.Satellite;
import com.skzh.map.mapper.SatelliteMapper;
import com.skzh.order.domain.OrderInfo;
import com.skzh.order.mapper.OrderInfoMapper;
import com.skzh.web.controller.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static com.skzh.order.service.impl.OrderManagementServiceImpl.IDGrn;

@Slf4j
@Component
public class redtideRouter {
    private static final Logger logger = Logger.getLogger(haianxianRouter.class.getName());
    public void processSubprocessBuffer(Process process, Logger logger, Long id, String userName) {
        Long orderId = id;
        String name = userName;
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("SPARK STDOUT222: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread jdThread = new Thread(() -> {
            webSocketServer.sendMessageByUserName("admin", "123123");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                logger.info(id+"");
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("预测进度") >= 0) {
                        // 推送websocket
                        String socketMsg = line;
                        socketMsg = socketMsg.substring(5, socketMsg.indexOf("%")+1);
                        socketMsg = socketMsg.trim();
                        socketMsg = (orderId+";"+"生成进度 "+socketMsg);
                        System.out.println(name);
                        System.out.println(socketMsg);
//                        webSocketServer.sendMessageByUserName(name, socketMsg);
                    }
                    logger.info("SPARK STDERR333: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jdThread.start();
    }

    @Resource
    private OrderInfoMapper purchaseMapper;

    @Autowired
    private WebSocketServer webSocketServer;
    //    private final static Lock queueLock = new ReentrantLock();
    @Autowired
    private SatelliteMapper satelliteMapper1;

    //线程池
//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);


    private String outputDir = "F:/Results/";

    public AjaxResult redtideDetection(Long id, SysUser account)  throws Exception {
        String workplace = "F:/redtide-16/";
        String ID = IDGrn(account.getUserName(),null);

        // 查询卫星数据
        Satellite data = satelliteMapper1.selectSatelliteById(id);

        // 修改订单信息
        OrderInfo info2DB = new OrderInfo();
        info2DB.setAccount(account.getUserName());
        info2DB.setUserId(account.getUserId());
        info2DB.setOrderNumber(ID);
        info2DB.setOrderName("赤潮提取");
        info2DB.setOrderDetail("待处理数据"+data.getSourcefilename().substring(0,data.getSourcefilename().length()-4)+"赤潮提取操作");
        info2DB.setProdId(String.valueOf(id));
        info2DB.setPrice(10.0);
        info2DB.setCategory("AIP");
        info2DB.setMapid(data.getId());
        info2DB.setOrderTime(String.valueOf(LocalDateTime.now()));

        // 插入订单
        try {
            purchaseMapper.addpurchase(info2DB);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
        }

        // 获取输入文件路径和输出文件路径
        String inputImagePath = data.getSourcefilepath();
        String outputPath = outputDir + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId();

        // 修改Python环境和脚本
        String pythonPath = "D:/anaconda3/envs/holitracer/python.exe";
        String scriptPath = "./tools/seg_infer.py";
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonPath,
                scriptPath,
                "--image_path", inputImagePath,
                "--result_dir", outputPath);

        // 启动线程执行Python脚本
        Thread taskThread = new Thread(() -> {
            try {
                processBuilder.directory(new File(workplace));
                Process process2 = processBuilder.start();

                // 读取Python脚本标准输出
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            logger.info("赤潮模型 STDOUT: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                // 读取Python脚本错误输出，推送进度
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getErrorStream()))) {
                        logger.info(id+"");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.indexOf("预测进度") >= 0) {
                                // 推送websocket
                                String socketMsg = line;
                                socketMsg = socketMsg.substring(5, socketMsg.indexOf("%")+1);
                                socketMsg = socketMsg.trim();
                                socketMsg = (info2DB.getId()+";"+"生成进度 "+socketMsg);
                                webSocketServer.sendMessageByUserName(account.getUserName(), socketMsg);
                            }
                            logger.info("赤潮 STDERR: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                // 等待进程结束
                int code = process2.waitFor();
                log.info("Python script exited with code " + code);
                if (code == 0) {
                    info2DB.setProdId("0");
                    String parentDir = outputDir + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/";
                    String fileNamePrefix = data.getSourcefilename().substring(0, data.getSourcefilename().length()-4);
                    String shpFile = parentDir + "shp/" + fileNamePrefix + "_pred.shp";
                    info2DB.setRawfile(shpFile);

                    String zipParentDir = "F:/Results/" + account.getUserName() + "/" + data.getSatelliteType() + "/";
                    String zipFilePath = zipParentDir + data.getId() + "_redtide_all.zip";
                    info2DB.setFilepath(zipFilePath);

                    // 打包文件
                    try {
                        ZipUtil.zip(parentDir, zipFilePath);
                        log.info("打包完成");
                    } catch (Exception e) {
                        log.error("打包失败", e);
                    }
                    // 更新订单
                    try {
                        purchaseMapper.updateById1(info2DB);
                    } catch (Exception e) {
                        log.error("数据库操作失败", e);
                    }
                    // 推送完成消息
                    webSocketServer.sendMessageByUserName(account.getUserName(), "任务完成，详情请查看结果文件");
                } else {
                    log.error("运行失败，退出码：" + code);
                }
            } catch (Exception e) {
                log.error("进程启动或执行失败", e);
                Thread.currentThread().interrupt();
            }
        });
        taskThread.start();
        try {
            System.out.println("运行结果返回");
            return AjaxResult.success("订单生成成功,请稍后到个人中心/订单管理中查看订单详情");
        } catch (Exception e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            return AjaxResult.error("订单生成失败");
        }
    }
}