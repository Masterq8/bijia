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

import static com.skzh.kit.mapRouter.IDGrn;

//53行:跨平台 200行:python调用 178行：添加一行
@Slf4j
@Component
public class yangzhiquRouter {

    @Resource
    private OrderInfoMapper purchaseMapper;

    @Autowired
    private WebSocketServer webSocketServer;
    //    private final static Lock queueLock = new ReentrantLock();
    @Autowired
    private SatelliteMapper satelliteMapper1;

//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);


    private static final Logger logger = Logger.getLogger(yangzhiquRouter.class.getName());

//    private final static Lock queueLock = new ReentrantLock();

//    private String outputPath = "C:/Results/";
    private String outputPath = System.getProperty("os.name").toLowerCase().contains("win")
            ? "C:/Results/"
            : "/opt/results/";
//    private String queuePath = "./routes/cache.json";

    //    public static Object convertMapToObject(Map<String, Object> map, Class<?> objectClass) throws Exception {
//        Object obj = objectClass.newInstance();
//        Field[] fields = objectClass.getDeclaredFields();
//        for (Field field : fields) {
//            String fieldName = field.getName();
//            if (map.containsKey(fieldName)) {
//                field.setAccessible(true);
//                field.set(obj, map.get(fieldName));
//            }
//        }
//        return obj;
//    }
    private void processSubprocessBuffer(Process process, Logger logger) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("SPARK STDOUT: " + line);
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

    public AjaxResult yangzhiDetection(Long id, SysUser account) throws Exception {

        String workplace = "D:/bijia/yangzhiqu/";

        String ID = IDGrn(account.getUserName(), null);
        String workplaceWin = "D:\\bijia\\yangzhiqu\\";
        // 海水养殖区
        // xml文件信息读取，读取路径需自定义
        //String xmlData = FileUtil.readString(workplace + "test-org.xml", StandardCharsets.UTF_8);
//        String filePath = workplaceWin + "test.xml";big_test_config
//        String OutPath = workplace + "test.xml";
        String filePath = workplaceWin + "big_test_config.xml";
        String OutPath = workplace + "big_test_config.xml";
        String xmlData = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        System.out.println(xmlData);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LinkedHashMap<String, Object> result = xmlMapper.readValue(xmlData, LinkedHashMap.class);
        System.out.println("result:" + result);

        Satellite data = satelliteMapper1.selectSatelliteById(id);

//        OrderInfo info2DB = new OrderInfo();
        OrderInfo info2DB = new OrderInfo();
        info2DB.setAccount(account.getUserName());
        info2DB.setUserId(account.getUserId());
        info2DB.setOrderNumber(ID);
        info2DB.setOrderName("养殖区分类");
        info2DB.setOrderDetail("待处理数据"+data.getSourcefilename().substring(0,data.getSourcefilename().length()-5)+"养殖区提取操作");
        info2DB.setPrice(10.0);
        info2DB.setCategory("AIP");
        info2DB.setMapid(data.getId());
        info2DB.setOrderTime(String.valueOf(LocalDateTime.now()));
        try {
            purchaseMapper.addpurchase(info2DB);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
        }
//        info2DB.setId(data.getId());
//        info2DB.setAccount(data.getAccount());
//        info2DB.setState("2");

        // let queuePath = './routes/cache.json'

        String inputFilePath = data.getSourcefilepath();
        System.out.println(inputFilePath);

        String[] floNameArr = inputFilePath.split("/");
        System.out.println(floNameArr);

        String floName = floNameArr[floNameArr.length - 1];
        System.out.println(floName);

        String floname1 = data.getSourcefilename();

        String floname2 = floname1.substring(0, floname1.length() - 4);
//        System.out.println(floname1);

        String fileName = data.getSourcefilepath();

        JSONObject payload = new JSONObject(result);
        // JSON数据规范化，去除其中冗余的信息
//        String payloadStr = JSONObject.toJSONString(result);
//        System.out.println("payloadStr:"+payloadStr);
//        JSONObject payload = JSONObject.parseObject(payloadStr);
        System.out.println("payload" + payload);
        // 修改XML文件
        // 待处理文件路径《需要更换为传参的》
//        JSONObject jobOrder = JSON.parseArray(payload.getString("Job_Order"), JSONObject.class).get(0);
//        JSONObject procs = JSON.parseArray(jobOrder.getString("Procs"), JSONObject.class).get(0);
//        JSONObject proc = JSON.parseArray(procs.getString("Proc"), JSONObject.class).get(0);
//
//        JSONObject inputs = JSON.parseArray(proc.getString("Inputs"), JSONObject.class).get(0);
//        JSONObject input = JSON.parseArray(inputs.getString("Input"), JSONObject.class).get(0);
//        JSONObject file_names = JSON.parseArray(input.getString("File_Names"), JSONObject.class).get(0);
//        JSON.parseArray(file_names.getString("File_Name"), String.class).set(0, inputFilePath + fileName);
//
//        JSONObject outputs = JSON.parseArray(proc.getString("Outputs"), JSONObject.class).get(0);
//        JSONObject output = JSON.parseArray(outputs.getString("Output"), JSONObject.class).get(0);
//        JSONObject file_names2 = JSON.parseArray(output.getString("File_Names"), JSONObject.class).get(0);
//        JSON.parseArray(file_names2.getString("File_Name"), String.class).set(0, outputPath + data.getAccount() + "/" + data.getTag() + "/" + data.getId());
        payload.getJSONObject("Procs").getJSONObject("Proc").getJSONObject("Input").getJSONObject("File_Names").put("File_Name", fileName);
        payload.getJSONObject("Procs").getJSONObject("Proc").getJSONObject("Output").put("File_Name", outputPath + account.getUserName() + '/' + data.getSatelliteType() + '/' + data.getId());
 //       payload.getJSONObject("Procs").getJSONObject("Proc").put("Model_Weights", "/path/to/your/model/weights.h5");  // 你的模型权重路径
        System.out.println("修改后的payload" + payload);

        // XML构建器，将之前的xml文件信息写入，用作程序执行参数
        String xml = xmlMapper.writeValueAsString(payload);

        try {
            FileUtil.writeString(xml, workplace + "big_test_config.xml", StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("xml配置文件读写失败", e);
            return AjaxResult.error("xml配置文件读写失败");
        }

        String xmlData1 = new String(Files.readAllBytes(Paths.get(OutPath)), StandardCharsets.UTF_8);
        System.out.println(xmlData1);


//         使用ProcessBuilder来构建和执行命令
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "cmd.exe", "/c", "F:&&cd bijia/yangzhiqu&&RAA.exe F:/bijia/yangzhiqu/test2.xml"
//        );F:&&cd bijia\yangzhiqu&&RAA.exe F:\bijia\yangzhiqu\test2.xml
//        "RAA.exe ", "test2.xml"
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "cmd.exe", "/c", "RAA.exe D:/bijia/yangzhiqu/test2.xml"
//        );
//        String pythonPath = System.getProperty("os.name").toLowerCase().contains("win")
//                ? "D:/anaconda3/envs/hisup/python.exe"
//                : "/usr/bin/python3";  // 或你的Python路径
//        String pythonPath = "D:/anaconda3/envs/hisup/python.exe";
        String pythonPath = "D:/anaconda3/envs/rsbuilding/python.exe";
        String scriptPath = "D:/bijia/RSBuilding-main/tools/big_test_xml.py";
        String imagePath = data.getSourcefilepath();
//        String outputPath = "D:/bijia/yangzhiqu/result/test";
//        String configPath = "D:/bijia/yangzhiqu/lyg_hrnet48_300.yaml";
//        String checkpointPath = "D:/bijia/yangzhiqu/crowdai_hrnet48_e100.pth";
//        String threshold = "0.5";
//        String patchSize = "512";
//        String stride = "400";


//        System.out.println("处理遥感图像：" + imagePath);
//        String command = "D:\\anaconda3\\Scripts\\activate.bat D:\\anaconda3\\envs\\rsbuilding && python D:/bijia/RSBuilding-main/tools/big_test_xml.py"
//                + " --image " + imagePath;
//                + " --output " + outputPath
//                + " --config " + configPath
//                + " --checkpoint " + checkpointPath
//                + " --threshold " + threshold
//                + " --patch_size " + patchSize
//                + " --stride " + stride;

        // 1. xml 配置文件路径（你已经生成到 workplace + "big_test_config.xml"）
//        String xmlConfigFile = scriptPath;
        String xmlConfigFile = workplace + "big_test_config.xml";
// 2. 模型权重路径（按你实际路径写）
        String checkpointPath = "D:/bijia/RSBuilding-main/pretrain/Swin_T_lyg_loss6/iter_28000.pth";

// 3. 正确的命令：两个位置参数，去掉所有 --xxx
        String command = "D:\\anaconda3\\Scripts\\activate.bat D:\\anaconda3\\envs\\rsbuilding && python D:/bijia/RSBuilding-main/tools/big_test_xml.py "
                + xmlConfigFile + " " + checkpointPath;
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", command
        );
        processBuilder.directory(new File("D:/bijia/yangzhiqu"));
        Thread taskThread = new Thread(() -> {
            try {
                // 启动进程
                processBuilder.directory(new File("D:/bijia/yangzhiqu"));
                Process process = processBuilder.start();

                new yangzhiquRouter().processSubprocessBuffer(process, logger);


                // 等待进程结束
                int code = process.waitFor();
                log.info("Python script exited with code " + code);
                if (code == 0) {
                    info2DB.setProdId("0");
                    String rawFilePath = outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/" + data.getSourcefilename().substring(0,data.getSourcefilename().length()-5) + ".tif";
                    info2DB.setRawfile(rawFilePath);
                    System.out.println(info2DB.getRawfile());

                    String zipFilePath = outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId() + "/" + data.getSourcefilename().substring(0,data.getSourcefilename().length()-5) + ".zip";
                    info2DB.setFilepath(zipFilePath);
                    System.out.println(info2DB.getFilepath());

                    webSocketServer.sendMessageByUserName(account.getUserName(), "任务完成，详情请查看结果文件");

                    try {
                        ZipUtil.zip(outputPath + account.getUserName() + "/" + data.getSatelliteType() + "/" + data.getId()+"/"+data.getSourcefilename().substring(0,data.getSourcefilename().length()-5), info2DB.getFilepath());
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
//                    return AjaxResult.error("运行失败");
                }
            } catch (IOException | InterruptedException e) {
                log.error("进程启动或执行失败", e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
//                return AjaxResult.error("进程启动或运行失败");
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
