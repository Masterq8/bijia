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
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static com.skzh.order.service.impl.OrderManagementServiceImpl.IDGrn;

@Slf4j
@Component
public class haianxianRouter {
    private static final Logger logger = Logger.getLogger(haianxianRouter.class.getName());
    private void processSubprocessBuffer(Process process, Logger logger) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("SPARK STDOUT111: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("SPARK STDERR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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


    private String outputPath = "F:/Results/";

    public AjaxResult shorelineDetection(Long id, SysUser account) throws Exception {

        String workplace = "D:/bijia/line/";

        String ID = IDGrn(account.getUserName(),null);

        String workplaceWin = "D:\\bijia\\line\\";
        // 海水养殖区
        // xml文件信息读取，读取路径需自定义
        //String xmlData = FileUtil.readString(workplace + "test-org.xml", StandardCharsets.UTF_8);
        String filePath = workplaceWin + "test-org.xml";
        String OutPath = workplace + "test.xml";
        String xmlData = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        System.out.println(xmlData);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LinkedHashMap<String, Object> result = xmlMapper.readValue(xmlData, LinkedHashMap.class);
        System.out.println("result:" + result);


        Satellite data = satelliteMapper1.selectSatelliteById(id);

//        OrderInfo info2DB = new OrderInfo();
//        info2DB.setId(data.getId());
//        info2DB.setAccount(data.getAccount());
//        info2DB.setState("2");
        OrderInfo info2DB = new OrderInfo();
        info2DB.setAccount(account.getUserName());
        info2DB.setUserId(account.getUserId());
        info2DB.setOrderNumber(ID);
        info2DB.setOrderName("海岸线信息提取");
        info2DB.setOrderDetail("待处理数据"+data.getSourcefilename().substring(0,data.getSourcefilename().length()-5)+"海岸线信息提取操作");
        info2DB.setProdId(String.valueOf(id));
        info2DB.setPrice(10.0);
        info2DB.setCategory("AIP");
        info2DB.setMapid(data.getId());
        info2DB.setOrderTime(String.valueOf(LocalDateTime.now()));

        try {
            purchaseMapper.addpurchase(info2DB);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
        }

        // let queuePath = './routes/cache.json'

        String inputFilePath = data.getSourcefilepath();
        System.out.println(inputFilePath);

        String[] floNameArr = inputFilePath.split("/");
        System.out.println(floNameArr);

        String floName = floNameArr[floNameArr.length - 1];
        System.out.println(floName);
        String floname1 = floName.substring(14, floName.length() - 5);
//        String floname1 = floName.substring(floName.lastIndexOf("'\'") + 1);

        String fileName = data.getSourcefilepath();

        JSONObject payload = new JSONObject(result);
        // JSON数据规范化，去除其中冗余的信息
//        String payloadStr = JSONObject.toJSONString(result);
//        System.out.println("payloadStr:"+payloadStr);
//        JSONObject payload = JSONObject.parseObject(payloadStr);
        System.out.println("payload"+ payload);
        payload.getJSONObject("Procs").getJSONObject("Proc").getJSONObject("Inputs").getJSONObject("Input").getJSONObject("File_Names").put("File_Name", fileName);
        payload.getJSONObject("Procs").getJSONObject("Proc").getJSONObject("Outputs").getJSONObject("Output").put("File_Name", outputPath + account.getUserName()+'/'+data.getSatelliteType()+'/'+data.getId());
        System.out.println("修改后的payload"+ payload);


        // XML构建器，将之前的xml文件信息写入，用作程序执行参数
        String xml = xmlMapper.writeValueAsString(payload);

        try {
            FileUtil.writeString(xml, workplaceWin + "test.xml", StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("xml配置文件读写失败", e);
            return AjaxResult.error("xml配置文件读写失败");
        }

        String xmlData1 = new String(Files.readAllBytes(Paths.get(OutPath)), StandardCharsets.UTF_8);
        System.out.println(xmlData1);


        // 使用ProcessBuilder来构建和执行命令
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "cmd.exe","/c","activate arcgispro-py3-clone&&D:/anaconda3/envs/arcgispro-py3-clone/python.exe D:/bijia/line/CoastlineDetection.py"
//        );
        String pythonPath = "D:/anaconda3/envs/arcgispropy3/python.exe";
        String scriptPath = "D:/qyf-code/line/CoastlineDetection.py";
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonPath,
                scriptPath
        );


        Thread taskThread = new Thread(() -> {
            try {
                // 启动进程
                processBuilder.directory(new File(workplace));
                Process process2 = processBuilder.start();
//                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process2.getInputStream()));
//                String s;
//                System.out.println("Standard Output of Python Script: ");
//                while ((s = stdInput.readLine()) != null) {
//                    System.out.println(s);
//                }
                new haianxianRouter().processSubprocessBuffer(process2, logger);

//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            StringBuilder errorOutput = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                errorOutput.append(line).append("\n");
//            }

                // 等待进程结束
                int code = process2.waitFor();
                log.info("Python script exited with code " + code);
                if (code == 0) {
                    info2DB.setProdId("0");
                    String rawFilePath = outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/" + data.getSourcefilename().substring(0,data.getSourcefilename().length()-5) + "/coastline.shp";
                    info2DB.setRawfile(rawFilePath);
                    System.out.println(info2DB.getRawfile());

                    String zipFilePath = outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/" + data.getSourcefilename().substring(0,data.getSourcefilename().length()-5) + ".zip";
                    info2DB.setFilepath(zipFilePath);
                    System.out.println(info2DB.getFilepath());

                    //websocket通信传消息给前端py文件处理完成
                    webSocketServer.sendMessageByUserName(account.getUserName(), "任务完成，详情请查看结果文件");

                    try {
                        ZipUtil.zip(outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/" + data.getSourcefilename().substring(0,data.getSourcefilename().length()-5), info2DB.getFilepath());
                        log.info("打包完成");
                    } catch (Exception e) {
                        log.error("打包失败", e);
                    }
                    try {
                        purchaseMapper.updateById1(info2DB);
                    } catch (Exception e) {
                        log.error("数据库操作失败", e);
                    }

                } else {
                    log.error("运行失败，退出码：" + code);
//                    return false;
                }
            } catch (Exception e) {
                log.error("进程启动或执行失败", e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
//                return false;
            }
        });
        taskThread.start();
        try {
//            taskThread.join();
            System.out.println("运行结果返回");
            return AjaxResult.success("订单生成成功,请稍后到个人中心/订单管理中查看订单详情");
        } catch (Exception e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            return AjaxResult.error("订单生成失败");
        }
    }
}