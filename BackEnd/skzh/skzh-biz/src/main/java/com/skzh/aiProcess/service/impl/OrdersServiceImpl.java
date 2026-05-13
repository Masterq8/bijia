//package com.skzh.aiProcess.service.impl;
//
//import com.skzh.aiProcess.domain.zhuantituVo;
//import com.skzh.aiProcess.service.OrdersService;
//import com.skzh.common.core.domain.AjaxResult;
//import com.skzh.common.core.domain.entity.SysUser;
//import com.skzh.map.domain.Satellite;
//import com.skzh.map.mapper.SatelliteMapper;
//import com.skzh.order.domain.OrderInfo;
//
//import com.skzh.order.mapper.OrderInfoMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.logging.Logger;
//
//@Service
//@Slf4j
//public class OrdersServiceImpl implements OrdersService {
//
//    String output = "";
//
//    private static final Logger logger = Logger.getLogger(OrdersServiceImpl.class.getName());
//
//    private void processSubprocessBuffer(Process process, Logger logger) {
//        new Thread(() -> {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    logger.info("SPARK STDOUT: " + line);
//                    output = line;
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread(() -> {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    logger.info("SPARK STDERR: " + line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    @Autowired
//    private OrderInfoMapper ordersMapper;
//
//    @Autowired
//    private SatelliteMapper satelliteMapper1;
//
//
//
//    @Override
//    public AjaxResult zhuantituGen(zhuantituVo data, SysUser account) throws Exception {
//
//        if (data.getExinfo() == null) {
//            data.setExinfo("处理结果 -> 专题图生成");
//        }
//        data.setTitle("专题图生成");
//        OrderInfo orderInfo = ordersMapper.getOrderById(data.getId());
//        Satellite satellite = satelliteMapper1.selectSatelliteById(Long.valueOf(orderInfo.getProdId()));
//
////        String filePath = "";
//        String outputPath = "F:/Results/";
////        String queuePath = "./routes/cache.json";
////        String dataPath = "F:/bijia/Arcpy pro/2/1.png";
//
//        String str = account.getUserName();
//
//        String ID = IDGrn(str, "GTM");
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 格式化日期
//        String formattedDate = LocalDateTime.now().format(formatter);
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////
////// 格式化日期
////        String formattedDate = sdf.format(LocalDateTime.now());
//
//// 现在您可以分割年月日
//        String[] imgDate = formattedDate.split("-");
////        String[] imgDate = data.getStarttime().toString().split("-");
//        String imgDate1 = imgDate[0] + "年" + imgDate[1] + "月" + imgDate[2] + "日";
//        OrderInfo data2DB = new OrderInfo();
//        data2DB.setAccount(str);
//        data2DB.setOrderNumber(ID);
//        data2DB.setOrderName(data.getTitle());
//        data2DB.setOrderDetail(data.getExinfo());
//        data2DB.setProdId("1");
//        data2DB.setPrice(10.0);
//        data2DB.setCategory("map");
//        data2DB.setMapid(data.getId());
//        data2DB.setOrderTime(formattedDate);
//        data2DB.setState("0");
//
//
//        //对数据库添加订单，尝试将data2DB（订单相关数据）插入数据库，创建新的订单。
//        ordersMapper.addpurchase(data2DB);
//
//
//        //此处应该查询文件路径，等待理解
//        //查询源文件路径
//
//        String SourceFilePath = satellite.getSourcefilepath();
//        String SourceFileName = satellite.getSourcefilename();
//
//
////        String sourcePath = SourceFilePath + SourceFileName;
////
////        data2DB.setFilepath(SourceFilePath);
//
//
//        //更新sourcePath
////        int update = ordersMapper.updateById1(data2DB);
//
//
//        OrderInfo result2DB = new OrderInfo();
//        result2DB.setAccount(str);
//        result2DB.setProdId("2");
//        result2DB.setFilepath(null);
////        result2DB.setYwid(ID);
//        result2DB.setOrderNumber(ID);
//        /*let py = spawn("D:/PyUnion/envs/arcgispro-py39/python.exe", ["D:/Work_Place/Py_Work_Place/ArcpyPro/main.py",
//        '--title', data['title'],
//                "--satellite", data['SatelliteID'],
//                "--sensor", data['SensorID'],
//                "--resolution", data['Resolution'],
//                "--date", imgDate,
//                "--template", data['type'],
//                // "--sourcePath", result['sourceFilePath'] + result['sourceFileName'],
//                // "--sourcePath", "D:\\Work_Place\\Py_Work_Place\\ArcpyPro\\lvchao\\yuan.tif",
//                "--sourcePath", data['sourcePath'],
//                "--dataPath", data['dataPath'],
//                // "--dataPath", "D:\\Work_Place\\Py_Work_Place\\ArcpyPro\\lvchao\\ap(1).tif",
//                "--ID", ID,
//                "--outputDir", outputPath + data['account'] + `/map`
//                ], {
//            cwd: "D:/Work_Place/Py_Work_Place/ArcpyPro/"
//        })*/
////        List<String> command = new ArrayList<>();
////
////        command.add("activate arcgispy");
////
////        command.add("python main.py");
////        command.add("--title");
////        command.add(data.getTitle());
////        command.add("--satellite");
////        command.add(data.getSatelliteType());
////        command.add("--sensor");
////        command.add(data.getSensorType());
////        command.add("--resolution");
////        command.add(data.getResolution());
////        command.add("--date");
////        command.add(imgDate1);
////        command.add("--template");
////        //属于purchase数据但是也是前端传过来的
////        command.add("专题图生成");
////        command.add("--sourcePath");
////        command.add(sourcePath);
////        command.add("--dataPath");
////        command.add(dataPath);
////        command.add("--ID");
////        command.add(ID);
////        command.add("--outputDir");
////        command.add(outputPath + data2DB.getAccount() + "/map");
////
////        ProcessBuilder processBuilder = new ProcessBuilder(command);
////        processBuilder.directory(new File("F:/bijia/Arcpy pro"));
////
////        Process process = processBuilder.start();
////        List<String> command = new ArrayList<>();
//        // 构建一个字符串，包含激活虚拟环境和运行Python脚本的完整命令
////        String activateCmd = "activate arcgispy && python main.py " +
////                "--title \"" + data.getTitle() + "\" " +
////                "--satellite \"" + data.getSatelliteType() + "\" " +
////                "--sensor \"" + data.getSensorType() + "\" " +
////                "--resolution \"" + data.getResolution() + "\" " +
////                "--date \"" + imgDate1 + "\" " +
////                "--template \"专题图生成\" " +
////                "--sourcePath \"" + sourcePath + "\" " +
////                "--dataPath \"" + dataPath + "\" " +
////                "--ID \"" + ID + "\" " +
////                "--outputDir \"" + outputPath + data2DB.getAccount() + "/map\"";
////
////        // 将整个命令序列作为一个单独的字符串添加到命令列表中
////        command.add("cmd.exe");
////        command.add("/c");
////        command.add(activateCmd);
//
////        ProcessBuilder processBuilder = new ProcessBuilder(
////                "cmd.exe", "/c", "activate arcgispy && python main.py --title \"shengcheng\" --satellite \"GF-1\" --sensor \"WFV\" --resolution \"16m\" --date \"2024年9月29日\" --template 1 --sourcePath \"F:/bijia/Arcpy Pro/2/GF1_WFV2_E111.9_N22.0_20141126_L1A0000478528.tiff\" --dataPath \"F:/bijia/Arcpy pro/2/1.png\" --ID 1 --outputDir \"F:/Result/map\""
////        );
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "cmd.exe", "/c",
//                "activate arcgispy && python main.py " +
//                        "--title \"" + data.getTitle() + "\" " +
//                        "--satellite \"" + satellite.getSatelliteType() + "\" " +
//                        "--sensor \"" + satellite.getSensorType() + "\" " +
//                        "--resolution \"" + satellite.getResolution() + "\" " +
//                        "--date \"" + imgDate1 + "\" " +
//                        "--template \"" + 1 + "\" " +
//                        "--sourcePath \"" + satellite.getSourcefilepath() + "\" " +
//                        "--dataPath \"" + satellite.getImage() + "\" " +
//                        "--ID \"" + satellite.getId() + "\" " +
//                        "--outputDir \"" + outputPath + data2DB.getAccount() + "/map\""
//        );
//
//        // 将整个命令序列作为一个单独的字符串添加到命令列表中
//
//
//        Thread taskThread = new Thread(() -> {
//            try {
//                processBuilder.directory(new File("F:/bijia/Arcpypro"));
//                Process process = processBuilder.start();
//                //处理标准输出
//                new OrdersServiceImpl().processSubprocessBuffer(process, logger);
//                int exitCode = process.waitFor();
//                System.out.println(exitCode);
//                if (exitCode == 0) {
//
//                    // Python脚本成功执行
//                    String thumbnail = "F:/Result/map/" + ID + "/" + "pic" + ID + ".jpg";
//                    String filePath = ":/Result/map/" + ID + "/" + "pic";
//                    // 解析filePath等信息
//                    //            String[] paths = resultOutput.replace("\n", "").replace("\r", "").split(";");
//                    //            filePath = paths[1];
//                    //            String thumbnail = paths[0] + "/" + ID + ".jpg";
//                    result2DB.setProdId("0");
//                    // 数据库操作
//                    // dbOp.alertPurchase(result2DB); // 模拟数据库操作
//                    result2DB.setThumbnail(thumbnail);
//                    //
//                    result2DB.setFilepath(filePath);
////                    ordersMapper.updateById1(result2DB);
//                    try {
//                        ordersMapper.updateById1(result2DB);
//                        //状态更新完就回调
////                        callback.run();
//                    } catch (Exception e) {
//                        log.error("数据库操作失败", e);
//                    }
//
//                } else {
//                    result2DB.setProdId("2");
//                    ordersMapper.updateById2(result2DB);
//                }
//
//            } catch (Exception e) {
//                log.error("进程启动或执行失败", e);
//                Thread.currentThread().interrupt();
//
//            }
//        });
//        taskThread.start();
//        try {
////            taskThread.join();
//            System.out.println("运行结果返回");
//            return AjaxResult.success("订单生成成功,请到个人中心/订单管理中查看订单详情");
//
//        } catch (Exception e) {
//            Thread.currentThread().interrupt(); // Restore the interrupted status
//            return AjaxResult.error("订单生成失败");
//        }
//    }
//
//
//    public static String IDGrn(String account, String tTag) throws NoSuchAlgorithmException {
//        //获取当前日期时间
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String dateTimeString = now.format(formatter);
//
//
//        //使用MessageDigest生成MD5哈希值
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(account.getBytes());
//        byte[] digest = md.digest();
//        StringBuilder hash = new StringBuilder();
//        for (byte b : digest) {
//            hash.append(String.format("%02x", b));
//        }
//
//        //取前4位哈希值
//        String customString = hash.substring(0, 4);
//
//        //生成4位随机数
//        SecureRandom random = new SecureRandom();
//        byte[] randomBytes = new byte[2];
//        random.nextBytes(randomBytes);
//        StringBuilder randomString = new StringBuilder();
//        for (byte b : randomBytes) {
//            randomString.append(String.format("%02x", b));
//        }
//
//        //如果tTag未定义，默认为"AIP"
//        tTag = ((tTag == null) || tTag.isEmpty()) ? "AIP" : tTag;
//
//
//        return dateTimeString + customString + randomString + tTag;
//    }
//}
