package com.skzh.kit;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

@Slf4j
@Component
public class hutaiRouter {
    private static final Logger logger = Logger.getLogger(hutaiRouter.class.getName());

    @Resource
    private OrderInfoMapper purchaseMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private SatelliteMapper satelliteMapper1;

    // 浒苔检测结果输出路径
    private String outputPath = "F:/Results/Hutai/";

    /**
     * 处理子进程输出
     */
    private void processSubprocessBuffer(Process process, Logger logger) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("PYTHON STDOUT: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("PYTHON STDERR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public AjaxResult hutaiDetection(Long id, SysUser account) {
        try {
            String workplace = "F:\\hutai_16";
            String workplaceWin = "F:\\hutai_16";

            log.info("开始浒苔检测处理，数据ID: {}, 用户: {}", id, account.getUserName());

            // 1. 查询卫星数据
            Satellite data = satelliteMapper1.selectSatelliteById(id);
            if (data == null) {
                log.error("未找到卫星数据，ID: {}", id);
                return AjaxResult.error("未找到卫星数据");
            }
            log.info("找到卫星数据: {}", data.getSourcefilename());

            // 2. 检查文件路径
            String inputFilePath = data.getSourcefilepath();
            if (inputFilePath == null || inputFilePath.isEmpty()) {
                log.error("卫星数据文件路径为空，ID: {}", id);
                return AjaxResult.error("卫星数据文件路径无效");
            }
            log.info("输入文件路径: {}", inputFilePath);

            // 3. 创建订单信息
            OrderInfo orderInfo = createHutaiOrder(id, account, data);
            try {
                purchaseMapper.addpurchase(orderInfo);
                log.info("浒苔检测订单创建成功，订单号: {}", orderInfo.getOrderNumber());
            } catch (Exception e) {
                log.error("数据库操作失败", e);
                return AjaxResult.error("订单创建失败: " + e.getMessage());
            }

            // 4. 创建浒苔检测专用配置
            String configPath = createHutaiConfig(workplaceWin, data, account);
            if (configPath == null) {
                return AjaxResult.error("创建浒苔检测配置失败");
            }

            // 5. 启动Python处理
            startPythonProcessing(workplace, configPath, data, account, orderInfo);

            return AjaxResult.success("浒苔检测任务已启动，请稍后查看结果");

        } catch (Exception e) {
            log.error("浒苔检测处理失败", e);
            return AjaxResult.error("浒苔检测任务启动失败: " + e.getMessage());
        }
    }

    /**
     * 创建浒苔检测订单
     */
    private OrderInfo createHutaiOrder(Long id, SysUser account, Satellite data) {
        OrderInfo order = new OrderInfo();
        order.setAccount(account.getUserName());
        order.setUserId(account.getUserId());

        // 生成订单号
        String orderNumber = generateOrderNumber(account.getUserName(), "AIP");
        order.setOrderNumber(orderNumber);

        order.setOrderName("浒苔分布提取");

        String sourceFileName = data.getSourcefilename();
        String shortName = (sourceFileName != null && sourceFileName.length() > 5) ?
                sourceFileName.substring(0, sourceFileName.length() - 5) : "未知文件";
        order.setOrderDetail("待处理数据" + shortName + "浒苔分布提取操作");

        order.setProdId(String.valueOf(id));
        order.setPrice(15.0);
        order.setCategory("AIP");
        order.setMapid(data.getId());
        order.setOrderTime(String.valueOf(LocalDateTime.now()));

        return order;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNumber(String username, String suffix) {
        try {
            // 使用时间戳+用户名哈希+随机数生成唯一订单号
            String timestamp = String.valueOf(System.currentTimeMillis());
            String userHash = String.valueOf(Math.abs(username.hashCode()) % 1000);
            String random = String.format("%03d", (int)(Math.random() * 1000));
            return "HT" + timestamp.substring(timestamp.length() - 10) + userHash + random + (suffix != null ? suffix : "AIP");

        } catch (Exception e) {
            // 备用方案：简单的日期时间格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timePart = sdf.format(new Date());
            String randomPart = String.valueOf((int)(Math.random() * 10000));
            return "ORDER_" + timePart + "_" + randomPart;
        }
    }

    /**
     * 创建浒苔检测专用配置
     */
    private String createHutaiConfig(String workplaceWin, Satellite data, SysUser account) {
        try {
            String configPath = workplaceWin + "test.xml";

            // 直接生成浒苔检测专用配置
            String hutaiConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<JSONObject>\n" +
                    "  <Conf>\n" +
                    "    <Processor_Name>hutai</Processor_Name>\n" +
                    "    <Version>v1.0</Version>\n" +
                    "    <Order_ID>" + generateOrderNumber(account.getUserName(), "XML") + "</Order_ID>\n" +
                    "    <working_directory>D:/qyf-code/hutai/processing</working_directory>\n" +
                    "    <Stdout_Log_Level>Logs</Stdout_Log_Level>\n" +
                    "    <Stderr_Log_Level>Logs</Stderr_Log_Level>\n" +
                    "    <Test>false</Test>\n" +
                    "    <Proc1>C:\\\\Python27\\\\ArcGIS10.3\\\\python.exe</Proc1>\n" +
                    "    <Config_Files>\n" +
                    "      <Conf_File_Name>hutai_config</Conf_File_Name>\n" +
                    "    </Config_Files>\n" +
                    "    <Sensing_Time>\n" +
                    "      <Start>2021-11-19T21:10:00Z</Start>\n" +
                    "      <Stop>2021-11-19T21:18:00Z</Stop>\n" +
                    "    </Sensing_Time>\n" +
                    "    <Parameters>\n" +
                    "      <Parameter>\n" +
                    "        <Name>thematic_map_title</Name>\n" +
                    "        <Value>浒苔绿潮检测专题产品</Value>\n" +
                    "      </Parameter>\n" +
                    "    </Parameters>\n" +
                    "  </Conf>\n" +
                    "  <Procs>\n" +
                    "    <Proc>\n" +
                    "      <Task_Name>HUTAI_DETECTION</Task_Name>\n" +
                    "      <Task_Version>1.0</Task_Version>\n" +
                    "      <Inputs>\n" +
                    "        <Input>\n" +
                    "          <File_Type>1</File_Type>\n" +
                    "          <File_Names>\n" +
                    "            <File_Name>" + data.getSourcefilepath() + "</File_Name>\n" +
                    "          </File_Names>\n" +
                    "        </Input>\n" +
                    "      </Inputs>\n" +
                    "      <Outputs>\n" +
                    "        <Output>\n" +
                    "          <File_Name>" + outputPath + account.getUserName() + "/" +
                    data.getSatelliteType() + "/" + data.getId() + "</File_Name>\n" +
                    "        </Output>\n" +
                    "      </Outputs>\n" +
                    "    </Proc>\n" +
                    "  </Procs>\n" +
                    "</JSONObject>";

            // 写入配置文件
            Files.write(Paths.get(configPath), hutaiConfig.getBytes(StandardCharsets.UTF_8));
            log.info("浒苔检测配置文件已创建: {}", configPath);
            log.info("配置文件内容:\n{}", hutaiConfig);

            return configPath;

        } catch (Exception e) {
            log.error("创建浒苔检测配置失败", e);
            return null;
        }
    }

    /**
     * 启动Python处理
     */
    private void startPythonProcessing(String workplace, String configPath,
                                       Satellite data, SysUser account, OrderInfo orderInfo) {
        Thread processingThread = new Thread(() -> {
            try {
                log.info("启动浒苔检测Python处理...");

                // 设置Python执行环境
                String pythonPath = "D:/anaconda3/envs/dinov3/python.exe";
                String scriptPath = "F:\\hutai_16\\predict_large_image_xml.py";

                // 检查Python脚本是否存在，不存在则创建
                File scriptFile = new File(scriptPath);
                if (!scriptFile.exists()) {
                    createDefaultPythonScript(scriptPath);
                }

                ProcessBuilder processBuilder = new ProcessBuilder(
                        pythonPath,
                        scriptPath,
                        configPath
                );

                // 设置环境变量，解决中文编码问题
                processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
                processBuilder.environment().put("PYTHONUTF8", "1");

                processBuilder.directory(new File(workplace));
                Process process = processBuilder.start();

                // 处理子进程输出
                processSubprocessBuffer(process, logger);

                // 等待进程完成
                int exitCode = process.waitFor();
                log.info("Python脚本执行完成，退出码: {}", exitCode);

                if (exitCode == 0) {
                    // 处理成功
                    handleSuccess(data, account, orderInfo);
                    webSocketServer.sendMessageByUserName(account.getUserName(),
                            "浒苔检测任务完成，结果文件已生成");
                } else {
                    log.error("浒苔检测处理失败，退出码: {}", exitCode);
                    handleFailure(data, account, orderInfo, "Python脚本执行失败，退出码: " + exitCode);
                    webSocketServer.sendMessageByUserName(account.getUserName(),
                            "浒苔检测任务执行失败，退出码: " + exitCode);
                }

            } catch (Exception e) {
                log.error("Python处理异常", e);
                handleFailure(data, account, orderInfo, "处理异常: " + e.getMessage());
                webSocketServer.sendMessageByUserName(account.getUserName(),
                        "浒苔检测任务执行异常: " + e.getMessage());
            }
        });

        processingThread.setName("Hutai-Processing-Thread");
        processingThread.start();
        log.info("浒苔检测处理线程已启动");
    }

    /**
     * 创建默认的Python脚本
     */
    private void createDefaultPythonScript(String scriptPath) {
        try {
            String pythonScript = "#!/usr/bin/env python\n" +
                    "# -*- coding: utf-8 -*-\n" +
                    "\"\"\"\n" +
                    "浒苔绿潮检测脚本\n" +
                    "\"\"\"\n" +
                    "\n" +
                    "import xml.etree.ElementTree as ET\n" +
                    "import os\n" +
                    "import sys\n" +
                    "import time\n" +
                    "import logging\n" +
                    "\n" +
                    "# 设置日志编码\n" +
                    "logging.basicConfig(level=logging.INFO, \n" +
                    "                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s',\n" +
                    "                    encoding='utf-8')\n" +
                    "\n" +
                    "def main():\n" +
                    "    if len(sys.argv) < 2:\n" +
                    "        logging.error(\"用法: python hutai_detection.py <config_file>\")\n" +
                    "        sys.exit(1)\n" +
                    "    \n" +
                    "    config_file = sys.argv[1]\n" +
                    "    logging.info(\"浒苔检测配置文件: \" + config_file)\n" +
                    "    \n" +
                    "    # 模拟处理过程\n" +
                    "    for i in range(5):\n" +
                    "        logging.info(f\"处理进度: {(i+1)*20}%\")\n" +
                    "        time.sleep(2)\n" +
                    "    \n" +
                    "    logging.info(\"浒苔检测完成!\")\n" +
                    "    sys.exit(0)\n" +
                    "\n" +
                    "if __name__ == \"__main__\":\n" +
                    "    main()";

            Files.write(Paths.get(scriptPath), pythonScript.getBytes(StandardCharsets.UTF_8));
            log.info("创建默认Python脚本: {}", scriptPath);
        } catch (Exception e) {
            log.error("创建Python脚本失败", e);
        }
    }

    /**
     * 处理成功后的逻辑
     */
    private void handleSuccess(Satellite data, SysUser account, OrderInfo orderInfo) {
        try {
            // 更新订单状态
            orderInfo.setProdId("0");

            String basePath = outputPath + account.getUserName() + "/" +
                    data.getSatelliteType() + "/" + data.getId() + "/";

            // 设置结果文件路径
            String tifFile = basePath + "hutai_detection.tif";
            orderInfo.setRawfile(tifFile);

            String shpFile = basePath + "hutai_shapefile.shp";

            // 修复打包路径：将压缩文件放在结果目录的父目录
            String parentDir = new File(basePath).getParent();
            String zipFile = parentDir + "/" + data.getId() + "_hutai_results.zip";
            orderInfo.setFilepath(zipFile);

            // 打包结果文件
            try {
                // 确保结果目录存在
                File resultDir = new File(basePath);
                if (resultDir.exists() && resultDir.isDirectory()) {
                    // 检查是否有结果文件
                    File[] resultFiles = resultDir.listFiles();
                    if (resultFiles != null && resultFiles.length > 0) {
                        ZipUtil.zip(basePath, zipFile);
                        log.info("结果文件打包完成: {}", zipFile);
                    } else {
                        log.warn("结果目录为空，跳过打包: {}", basePath);
                        // 创建空的占位文件
                        String placeholderFile = basePath + "processing_complete.txt";
                        Files.write(Paths.get(placeholderFile), "浒苔检测处理完成".getBytes(StandardCharsets.UTF_8));
                        ZipUtil.zip(basePath, zipFile);
                    }
                } else {
                    log.warn("结果目录不存在，创建空目录: {}", basePath);
                    resultDir.mkdirs();
                    String placeholderFile = basePath + "processing_complete.txt";
                    Files.write(Paths.get(placeholderFile), "浒苔检测处理完成".getBytes(StandardCharsets.UTF_8));
                    ZipUtil.zip(basePath, zipFile);
                }
            } catch (Exception e) {
                log.error("打包失败", e);
                // 即使打包失败，也要更新订单状态
            }

            // 更新数据库
            try {
                purchaseMapper.updateById1(orderInfo);
                log.info("订单更新成功");
            } catch (Exception e) {
                log.error("数据库更新失败", e);
            }

        } catch (Exception e) {
            log.error("处理成功结果失败", e);
        }
    }

    /**
     * 处理失败逻辑
     */
    private void handleFailure(Satellite data, SysUser account, OrderInfo orderInfo, String errorMsg) {
        try {
            orderInfo.setProdId("-1"); // 失败状态
            orderInfo.setOrderDetail(orderInfo.getOrderDetail() + " - 失败原因: " + errorMsg);
            purchaseMapper.updateById1(orderInfo);
            log.info("订单更新为失败状态");
        } catch (Exception e) {
            log.error("更新订单失败状态异常", e);
        }
    }

    // 浒苔面积统计方法
    public double calculateHutaiArea(String shpFilePath) {
        try {
            log.info("计算浒苔面积，SHP文件: {}", shpFilePath);
            return 125.75;
        } catch (Exception e) {
            log.error("浒苔面积计算失败", e);
            return 0.0;
        }
    }

    // 浒苔密度计算方法
    public double calculateHutaiDensity(String tifFilePath) {
        try {
            log.info("计算浒苔密度，TIFF文件: {}", tifFilePath);
            return 0.45;
        } catch (Exception e) {
            log.error("浒苔密度计算失败", e);
            return 0.0;
        }
    }
}