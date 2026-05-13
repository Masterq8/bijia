package com.skzh.web.controller.record;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.common.core.domain.model.LoginUser;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.common.utils.StringUtils;
import com.skzh.framework.config.ServerConfig;
import com.skzh.map.domain.Satellite;
import com.skzh.map.dto.MapPoint;
import com.skzh.map.service.ISatelliteService;
import com.skzh.web.util.GraphMapUtils;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.skzh.record.domain.RecordInfo;
import com.skzh.record.service.IRecordInfoService;
import com.skzh.common.utils.poi.ExcelUtil;
import com.skzh.common.core.page.TableDataInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 我的数据Controller
 *
 * @author skzh
 * @date 2024-09-03
 */
@RestController
@RequestMapping("/record/info" )
public class RecordInfoController extends BaseController {
    @Autowired
    private IRecordInfoService recordInfoService;

    @Autowired
    private ISatelliteService satelliteService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询我的数据列表
     */
    @PreAuthorize("@ss.hasPermi('record:info:list')" )
    @GetMapping("/list" )
    public TableDataInfo list(RecordInfo recordInfo) {
    SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        if ("common".equals(loginUser.getRoles().get(0).getRoleKey())) {
            recordInfo.setUserId(loginUser.getUserId());
        }
        startPage();
        List<RecordInfo> list = recordInfoService.selectRecordInfoList(recordInfo);
        return getDataTable(list);
    }

    /**
     * 导出我的数据列表
     */
    @PreAuthorize("@ss.hasPermi('record:info:export')" )
    @Log(title = "我的数据" , businessType = BusinessType.EXPORT)
    @PostMapping("/export" )
    public void export(HttpServletResponse response, RecordInfo recordInfo) {
        List<RecordInfo> list = recordInfoService.selectRecordInfoList(recordInfo);
        ExcelUtil<RecordInfo> util = new ExcelUtil<RecordInfo>(RecordInfo. class);
        util.exportExcel(response, list, "我的数据数据" );
    }

    /**
     * 获取我的数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('record:info:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return success(recordInfoService.selectRecordInfoById(id));
    }


    /**
     * 加入我的数据
     */
    @PreAuthorize("@ss.hasPermi('record:info:addDate')" )
    @Log(title = "我的数据" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addDate(@RequestBody RecordInfo recordInfo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Date currentTime = new Date();
        // 从卫星表里面获取卫星id
        try {
            List<Satellite> sateList = satelliteService.getListInIds(recordInfo.getSatelliteIds());
            List<RecordInfo> recordList = new ArrayList<>();
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            //遍历里面的并添加到recordList里面
            sateList.forEach(item -> {
                RecordInfo info = new RecordInfo();
                info.setSatelliteId(item.getId());
                info.setUserId(loginUser.getUserId());
                info.setSatelliteType(item.getSatelliteType());
                info.setSensorType(item.getSensorType());
                info.setCollectTime(item.getCollectTime());
                if (item.getImage().indexOf(filePath) >= 0) {
                    String image = item.getImage();
                    image = image.replace(filePath, prefixUrl+"/profile/upload");
                    info.setImage(image);
                }
                info.setRegion(item.getRegion());
                info.setProvince(item.getProvince());
                info.setCity(item.getCity());
                info.setLeftupLongitude(item.getLeftupLongitude());
                info.setLeftupLatitude(item.getLeftupLatitude());
                info.setLeftdownLongitude(item.getLeftdownLongitude());
                info.setLeftdownLatitude(item.getLeftdownLatitude());
                info.setRightupLongitude(item.getRightupLongitude());
                info.setRightupLatitude(item.getRightupLatitude());
                info.setRightdownLongitude(item.getRightdownLongitude());
                info.setRightdownLatitude(item.getRightdownLatitude());
                info.setViewPath(item.getViewPath());
                info.setViewRow(item.getViewRow());
                info.setStarPath(item.getStarPath());
                info.setStarRow(item.getStarRow());
                info.setCreateBy(loginUser.getUsername());
                info.setCreateTime(currentTime);
                recordList.add(info);
            });
            recordInfoService.saveBatch(recordList);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("添加成功");
    }


    /**
     * 新增我的数据
     */
    @PreAuthorize("@ss.hasPermi('record:info:add')" )
    @Log(title = "我的数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RecordInfo recordInfo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        recordInfo.setCreateBy(loginUser.getUsername());
        recordInfo.setUserId(loginUser.getUserId());
        Date currentTime  = new Date();
        recordInfo.setCreateTime(currentTime);
        return toAjax(recordInfoService.insertRecordInfo(recordInfo));
    }

    /**
     * 卫星影像管理--【添加卫星影像】
     */
    @PreAuthorize("@ss.hasPermi('record:info:add')")
    @Log(title = "添加卫星影像", businessType = BusinessType.INSERT)
    @PostMapping("/zip")
    public AjaxResult addZip(@RequestBody RecordInfo recordInfo, HttpServletRequest request) {
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();

        Date collectTime = recordInfo.getCollectTime();
        String province = recordInfo.getProvince();
        String city = recordInfo.getCity();
        String remark = recordInfo.getRemark();

        // 获取文件路径
        String sourcefilepath = recordInfo.getSourcefilepath();
        if (StringUtils.isNotEmpty(sourcefilepath)) {
            sourcefilepath = sourcefilepath.replace(prefixUrl+"/profile/upload", filePath);
            recordInfo.setSourcefilepath(sourcefilepath);
        }

        try {
            // 解压压缩包
            if (FilenameUtils.isExtension(sourcefilepath, "zip")) {
                System.out.println("准备解压zip文件");
                String tempTarPath = createTempZipFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("zip文件不为空，准备解压zip文件");
                    unZipFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "0");
                    System.out.println("zip文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 ZRP 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            } else if (FilenameUtils.isExtension(sourcefilepath, "rar")) {
                System.out.println("准备解压rar文件");
                String tempTarPath = createTempRarFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("rar文件不为空，准备解压rar文件");
                    unRarFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "0");
                    System.out.println("rar文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 RAR 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            } else if (FilenameUtils.isExtension(sourcefilepath, "gz") || FilenameUtils.isExtension(sourcefilepath, "tar.gz")) {
                System.out.println("准备解压tar.gz文件");
                String tempTarPath = unGzipFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("tar文件不为空，准备解压tar文件");
                    unTarFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "0");
                    System.out.println("tar文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 TAR 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            }

            // 返回成功响应
            return AjaxResult.success("压缩包已成功上传并解压");
        } catch (IOException e) {
            // 返回错误响应
            return AjaxResult.error("解压失败: " + e.getMessage());
        }
    }

    /**
     * 解压GZ文件到指定目录，并返回解压后的TAR文件路径
     *
     * @param gzFilePath GZ文件的路径
     * @param destDirectory 目标目录的路径
     * @return 解压后的TAR文件路径
     * @throws IOException 如果发生I/O错误
     */
    private String unGzipFile(String gzFilePath, String destDirectory) throws IOException {

        System.out.println("开始解压tar.gz文件");

        // 确保目标目录存在，如果不存在则创建它
        Files.createDirectories(Paths.get(destDirectory));

        // 获取目标文件的路径，这行代码构造了目标文件的完整路径
        String targetFilePath = destDirectory + File.separator + FilenameUtils.getName(gzFilePath).replaceAll("\\.gz$", ".tar");

        // 创建GZIP输入流
        try (GZIPInputStream gzipIn = new GZIPInputStream(new FileInputStream(gzFilePath));
             FileOutputStream fos = new FileOutputStream(targetFilePath)) {
            byte[] buffer = new byte[4096];
            int len;
            // 读取并写入文件
            while ((len = gzipIn.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
        return targetFilePath;
    }

    /**
     * 解压GZ文件到指定目录，并返回解压后的TAR文件路径
     *
     * @param sourceFilePath zip文件的路径
     * @param destDirectory 目标目录的路径
     * @return 解压后的TAR文件路径
     * @throws IOException 如果发生I/O错误
     */
    private String createTempZipFile(String sourceFilePath, String destDirectory) throws IOException {

        System.out.println("开始解压zip文件");
        // 目标目录路径
        Path targetDir = Paths.get(destDirectory);
        // 源ZIP文件路径
        Path sourceZip = Paths.get(sourceFilePath);
        String fileName = FilenameUtils.getName(sourceFilePath);
        // 获取目标文件的路径，这行代码构造了目标文件的完整路径
        String targetFilePath = destDirectory + File.separator + fileName;
        try {
            // 确保目标目录存在
            Files.createDirectories(targetDir);
            // 目标ZIP文件路径
            Path targetZip = targetDir.resolve(fileName);
            // 复制文件
            Files.copy(sourceZip, targetZip, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFilePath;
    }

    /**
     * 解压GZ文件到指定目录，并返回解压后的TAR文件路径
     *
     * @param sourceFilePath zip文件的路径
     * @param destDirectory 目标目录的路径
     * @return 解压后的TAR文件路径
     * @throws IOException 如果发生I/O错误
     */
    private String createTempRarFile(String sourceFilePath, String destDirectory) throws IOException {

        System.out.println("开始解压rar文件");
        String fileName = FilenameUtils.getName(sourceFilePath);
        // 获取目标文件的路径，这行代码构造了目标文件的完整路径
        String targetFilePath = destDirectory + File.separator + fileName;
        try (InputStream inputStream = new FileInputStream(sourceFilePath);
             OutputStream outputStream = new FileOutputStream(targetFilePath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成RAR临时文件时发生错误。");
        }
        return targetFilePath;
    }

    /**
     * 解压tar压缩文件
     * @param tarFilePath
     * @param destDirectory
     * @throws IOException
     */
    private Long unTarFile(String tarFilePath, String destDirectory, String province, String city,
                           String remark, Date collectTime, String isSaveRecord) throws IOException {
        System.out.println("开始解压tar文件");
        Satellite satellite = new Satellite();
        // 确保目标目录存在
        Files.createDirectories(Paths.get(destDirectory));

        String jpgFilePath = null;
        String tiffFilePath = null;
        String tiffFileName = null;

        // 创建TAR输入流
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(tarFilePath))) {
            TarArchiveEntry entry;
            // 循环遍历TAR内的条目
            while ((entry = tarIn.getNextTarEntry()) != null) {
                String fileName = entry.getName();
                String filePath = destDirectory + "/" + entry.getName();
                if (entry.isDirectory()) {
                    // 如果是目录，则创建目录
                    Files.createDirectories(Paths.get(filePath));
                } else {
                    // 如果是文件，则解压
                    extractFile(tarIn, filePath);

                    // 检查文件扩展名
                    if (fileName.endsWith(".jpg")) {
                        // JPG文件
                        System.out.println("发现JPG文件: " + filePath);
//                        jpgFilePath = filePath;
                        satellite.setImage(filePath);
                    } else if (fileName.endsWith(".tiff")|| fileName.endsWith(".tif")) {
                        // TIFF文件
                        System.out.println("发现TIFF文件: " + filePath);
//                        tiffFilePath = filePath;
                        satellite.setSourcefilepath(filePath);
                        satellite.setSourcefilename(entry.getName());
//                        tiffFileName = entry.getName(); // 获取TIFF文件的文件名
                    } else if (fileName.endsWith(".xml")) {
                        System.out.println("发现XML文件: " + filePath);
                        try {
                            satellite = readXMLFile(filePath, province, city, remark, collectTime, satellite);
                        } catch (Exception e) {
                            // 处理读取XML文件时可能发生的异常
                            System.err.println("Error reading XML file: " + e.getMessage());
                            // 可以选择重新抛出异常，或者处理它，例如记录日志等
                            // throw e;
                        }
                    }
                }
            }
            satelliteService.insertSatellite(satellite);
            if ("1".equals(isSaveRecord)) {
                // 封装并保存收藏记录
                saveRecordInfo(satellite);
            }
        }

        // 打印JPG文件的路径
        if (jpgFilePath != null) {
            System.out.println("JPG文件的路径: " + jpgFilePath);
        } else {
            System.out.println("没有发现JPG文件");
        }

        // 打印TIFF文件的路径和文件名
        if (tiffFilePath != null) {
            System.out.println("TIFF文件的路径: " + tiffFilePath);
            System.out.println("TIFF文件的文件名: " + tiffFileName);
        } else {
            System.out.println("没有发现TIFF文件");
        }
        return satellite.getId();
    }

    /**
     * 从输入流中提取文件
     *
     * @param in 输入流
     * @param filePath 要提取的文件路径
     * @throws IOException 如果发生I/O错误
     */
    private void extractFile(InputStream in, String filePath) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            while ((len = in.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }
    /**
     * 读取XML文件并打印内容
     *
     * @param xmlFilePath XML文件的路径
     * @throws Exception 如果发生解析错误
     */
    private Satellite readXMLFile(String xmlFilePath, String province, String city, String remark, Date collectTime,
                                  Satellite satellite) {
        try {
            // 创建DocumentBuilderFactory对象
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象,这个对象将用于解析XML文件。
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // 解析XML文件
            File xmlFile = new File(xmlFilePath);
            //检查File对象是否指向一个存在的文件，并且不是一个目录。
            if (!xmlFile.exists() || !xmlFile.isFile()) {
                System.out.println("XML file does not exist or is not a file: " + xmlFilePath);
            }
            //使用DocumentBuilder对象解析File对象指向的XML文件，并返回一个Document对象，该对象代表了XML文档的结构。
            Document doc = dBuilder.parse(xmlFile);

            // 检查Document是否为空
            if (doc == null) {
                System.out.println("Parsed document is null");
            }

            // 调用Document对象的normalize方法，以标准化其内部结构，确保所有实体引用都被展开，并且所有元素都被正确地格式化
            doc.getDocumentElement().normalize();

            // 获取根元素,获取Document对象的根元素，即XML文档的顶层元素。
            Element rootElement = doc.getDocumentElement();
            //检查根元素是否为null，如果是，则打印错误信息并返回。否则，打印根元素的节点名称。
            if (rootElement == null) {
                System.out.println("Root element is null");
            }
            System.out.println("Root element: " + rootElement.getNodeName());

            // 获取根元素下的所有子元素,获取根元素下的所有子节点，并将其存储在一个NodeList对象中。
            NodeList nodeList = rootElement.getChildNodes();
            System.out.println(nodeList);
            List<MapPoint> points = new ArrayList<>();
            MapPoint ltPoint = new MapPoint();
            MapPoint ldPoint = new MapPoint();
            MapPoint rtPoint = new MapPoint();
            MapPoint rdPoint = new MapPoint();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //遍历NodeList中的每个节点，检查节点类型是否为元素节点。如果是，将其转换为Element对象。
            for (int i = 0; i < nodeList.getLength(); i++) {
                // 获取当前节点
                Node node = nodeList.item(i);
                // 确保节点是元素节点
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // 打印节点名称和文本内容
                    System.out.println("Node Name: " + element.getNodeName());
                    System.out.println("Node Value: " + element.getNodeValue());//.getTextContent()

                    // 对于每个元素节点，检查其节点名称，并使用相应的setter方法将节点的文本内容设置到Satellite对象的属性中。
                    //把信息封装到Satellite对应属性内
                    if ("SatelliteID".equals(element.getNodeName())) {
                        satellite.setSatelliteType(element.getTextContent());
                    } else if ("SensorID".equals(element.getNodeName())) {
                        satellite.setSensorType(element.getTextContent());
                    }
                    else if ("ReceiveTime".equals(element.getNodeName())) {
                        satellite.setCollectTime(collectTime);
                    } else if ("OrbitID".equals(element.getNodeName())) {
                        satellite.setOrbitid(element.getTextContent());
                       // recordInfo.setOrbitid(element.getTextContent());
                    } else if ("ScenePath".equals(element.getNodeName())) {
                        satellite.setViewPath(Long.parseLong(element.getTextContent()));
                    } else if ("SceneRow".equals(element.getNodeName())) {
                        satellite.setViewRow(Long.parseLong(element.getTextContent()));
                    } else if ("SatPath".equals(element.getNodeName())) {
                        satellite.setStarPath(Long.parseLong(element.getTextContent()));
                    } else if ("SatRow".equals(element.getNodeName())) {
                        satellite.setStarRow(Long.parseLong(element.getTextContent()));
                    }
                  // 获取的字符串日期，无法成功转换为date类型，现在就先把实体类date换为string
                    else if ("StartTime".equals(element.getNodeName())) {
                        satellite.setStarttime(sdf.parse(element.getTextContent()+""));
                    } else if ("EndTime".equals(element.getNodeName())) {
                        satellite.setEndtime(sdf.parse(element.getTextContent()+""));
                    }
                    else if ("TopLeftLatitude".equals(element.getNodeName())) {
                        satellite.setLeftupLatitude(new BigDecimal(element.getTextContent()));
                        ltPoint.setLat(satellite.getLeftupLatitude().doubleValue());
                    } else if ("TopLeftLongitude".equals(element.getNodeName())) {
                        satellite.setLeftupLongitude(new BigDecimal(element.getTextContent()));
                        ltPoint.setLng(satellite.getLeftupLongitude().doubleValue());
                    } else if ("TopRightLatitude".equals(element.getNodeName())) {
                        satellite.setRightupLatitude(new BigDecimal(element.getTextContent()));
                        rtPoint.setLat(satellite.getRightupLatitude().doubleValue());
                    } else if ("TopRightLongitude".equals(element.getNodeName())) {
                        satellite.setRightupLongitude(new BigDecimal(element.getTextContent()));
                        rtPoint.setLng(satellite.getRightupLongitude().doubleValue());
                    } else if ("BottomRightLatitude".equals(element.getNodeName())) {
                        satellite.setRightdownLatitude(new BigDecimal(element.getTextContent()));
                        rdPoint.setLat(satellite.getRightdownLatitude().doubleValue());
                    } else if ("BottomRightLongitude".equals(element.getNodeName())) {
                        satellite.setRightdownLongitude(new BigDecimal(element.getTextContent()));
                        rdPoint.setLng(satellite.getRightdownLongitude().doubleValue());
                    } else if ("BottomLeftLatitude".equals(element.getNodeName())) {
                        satellite.setLeftdownLatitude(new BigDecimal(element.getTextContent()));
                        ldPoint.setLat(satellite.getLeftdownLatitude().doubleValue());
                    } else if ("BottomLeftLongitude".equals(element.getNodeName())) {
                        satellite.setLeftdownLongitude(new BigDecimal(element.getTextContent()));
                        ldPoint.setLng(satellite.getLeftdownLongitude().doubleValue());
                    }
                }
            }
            satellite.setResolution("4");
            satellite.setProvince(province);
            satellite.setCity(city);
            satellite.setExInfo(remark);

            // 封装中心点经纬度
            points.add(ltPoint);
            points.add(ldPoint);
            points.add(rtPoint);
            points.add(rdPoint);
            MapPoint centerPoint = GraphMapUtils.getCenterPointFromList(points);
            satellite.setCenterlatitude(new BigDecimal(centerPoint.getLat()));
            satellite.setCenterlongitude(new BigDecimal(centerPoint.getLng()));
        } catch (Exception e) {
            System.out.println("Error parsing XML file: " + e.getMessage());
            e.printStackTrace();
        }
        return satellite;
    }



    /**
     * 修改我的数据
     */
    @PreAuthorize("@ss.hasPermi('record:info:edit')" )
    @Log(title = "我的数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RecordInfo recordInfo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        recordInfo.setCreateBy(loginUser.getUsername());
        return toAjax(recordInfoService.updateRecordInfo(recordInfo));
    }

    /**
     * 删除我的数据
     */
    @PreAuthorize("@ss.hasPermi('record:info:remove')" )
    @Log(title = "我的数据" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(recordInfoService.deleteRecordInfoByIds(ids));
    }

    /**
     * @Description 上传我的数据--本地影像
     * @Param [recordInfo, request]
     * @return
     **/
    @Log(title = "上传我的数据--本地影像", businessType = BusinessType.INSERT)
    @PostMapping("/dealZip")
    public AjaxResult dealZip(@RequestBody RecordInfo recordInfo, HttpServletRequest request) {
        Long sateId = null;// 卫星影像id
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();

        Date collectTime = recordInfo.getCollectTime();
        String province = recordInfo.getProvince();
        String city = recordInfo.getCity();
        String remark = recordInfo.getRemark();

        // 获取文件路径
        String sourcefilepath = recordInfo.getSourcefilepath();
        System.out.println("sourcefilepath: " + sourcefilepath);
        if (StringUtils.isNotEmpty(sourcefilepath)) {
            sourcefilepath = sourcefilepath.replace(prefixUrl+"/profile/upload", filePath);
            recordInfo.setSourcefilepath(sourcefilepath);
        }
        System.out.println("sourcefilepath: " + sourcefilepath);

        try {
            // 解压压缩包
            if (FilenameUtils.isExtension(sourcefilepath, "zip")) {
                System.out.println("准备解压zip文件");
                String tempTarPath = createTempZipFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("zip文件不为空，准备解压zip文件");
                    sateId = unZipFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "1");
                    System.out.println("zip文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 ZRP 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            } else if (FilenameUtils.isExtension(sourcefilepath, "rar")) {
                System.out.println("准备解压rar文件");
                String tempTarPath = createTempRarFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("rar文件不为空，准备解压rar文件");
                    sateId = unRarFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "1");
                    System.out.println("rar文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 RAR 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            } else if (FilenameUtils.isExtension(sourcefilepath, "gz") || FilenameUtils.isExtension(sourcefilepath, "tar.gz")) {
                System.out.println("准备解压tar.gz文件");
                String tempTarPath = unGzipFile(sourcefilepath, filePath + "/temp");
                if (tempTarPath != null) {
                    System.out.println("tar文件不为空，准备解压tar文件");
                    sateId = unTarFile(tempTarPath, filePath + "/temp", province, city, remark, collectTime, "1");
                    System.out.println("tar文件解压完成");
                    Files.delete(Paths.get(tempTarPath)); // 删除临时 TAR 文件
                    Files.delete(Paths.get(recordInfo.getSourcefilepath())); // 删除上传的压缩包文件
                }
            }
            // 返回成功响应
            return AjaxResult.success("压缩包已成功上传并解压", sateId);
        } catch (IOException e) {
            // 返回错误响应
            return AjaxResult.error("解压失败: " + e.getMessage());
        }
    }

    /**
     * 解压tar压缩文件
     * @param tarFilePath
     * @param destDirectory
     * @throws IOException
     */
    private Long unZipFile(String tarFilePath, String destDirectory, String province, String city,
                           String remark, Date collectTime, String isSaveRecord) throws IOException {
        System.out.println("开始解压zip文件");
        Satellite satellite = new Satellite();
        // 确保目标目录存在
        Files.createDirectories(Paths.get(destDirectory));

        String jpgFilePath = null;
        String tiffFilePath = null;
        String tiffFileName = null;

        // 创建TAR输入流
        try (ZipInputStream tarIn = new ZipInputStream(new FileInputStream(tarFilePath))) {
            ZipEntry entry;
            // 循环遍历TAR内的条目
            while ((entry = tarIn.getNextEntry()) != null) {
                String fileName = entry.getName();
                String filePath = destDirectory + "/" + entry.getName();
                if (entry.isDirectory()) {
                    // 如果是目录，则创建目录
                    Files.createDirectories(Paths.get(filePath));
                } else {
                    // 如果是文件，则解压
                    extractFile(tarIn, filePath);

                    // 检查文件扩展名
                    if (fileName.endsWith(".jpg")) {
                        // JPG文件
                        System.out.println("发现JPG文件: " + filePath);
//                        jpgFilePath = filePath;
                        satellite.setImage(filePath);
                    } else if (fileName.endsWith(".tiff")||fileName.endsWith(".tif")){
                        // TIFF文件
                        System.out.println("发现TIFF文件: " + filePath);
//                        tiffFilePath = filePath;
//                        tiffFileName = entry.getName(); // 获取TIFF文件的文件名
                        satellite.setSourcefilepath(filePath);
                        satellite.setSourcefilename(entry.getName());
                    } else if (fileName.endsWith(".xml")) {
                        System.out.println("发现XML文件: " + filePath);
                        try {
                            satellite = readXMLFile(filePath, province, city, remark, collectTime, satellite);
                        } catch (Exception e) {
                            // 处理读取XML文件时可能发生的异常
                            System.err.println("Error reading XML file: " + e.getMessage());
                            // 可以选择重新抛出异常，或者处理它，例如记录日志等
                            // throw e;
                        }
                    }
                }
            }
            satelliteService.insertSatellite(satellite);
            if ("1".equals(isSaveRecord)) {
                // 封装并保存收藏记录
                saveRecordInfo(satellite);
            }
        }

        // 打印JPG文件的路径
        if (jpgFilePath != null) {
            System.out.println("JPG文件的路径: " + jpgFilePath);
        } else {
            System.out.println("没有发现JPG文件");
        }

        // 打印TIFF文件的路径和文件名
        if (tiffFilePath != null) {
            System.out.println("TIFF文件的路径: " + tiffFilePath);
            System.out.println("TIFF文件的文件名: " + tiffFileName);
        } else {
            System.out.println("没有发现TIFF文件");
        }
        return satellite.getId();
    }

    /**
     * 解压rar4压缩文件(暂不支持rar5.0及以上版本解压缩)
     * @param rarFilePath
     * @param destDirectory
     * @throws IOException
     */
    private Long unRarFile(String rarFilePath, String destDirectory, String province, String city,
                           String remark, Date collectTime, String isSaveRecord) throws IOException {
        System.out.println("开始解压rar文件");
        Satellite satellite = new Satellite();
        // 确保目标目录存在
        Files.createDirectories(Paths.get(destDirectory));

        try (Archive archive = new Archive(new File(rarFilePath))) {
            FileHeader fileHeader;
            while ((fileHeader = archive.nextFileHeader()) != null) {
                String fileName = fileHeader.getFileNameString().trim();
                String filePath = destDirectory + "/" + fileName;
                File extractedFile = new File(destDirectory, fileName);
                if (fileHeader.isDirectory()) {
                    extractedFile.mkdirs();
                } else {
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        archive.extractFile(fileHeader, outputStream);
                    }

                    // 检查文件扩展名
                    if (fileName.endsWith(".jpg")) {// JPG文件
                        System.out.println("发现JPG文件: " + filePath);
                        satellite.setImage(filePath);
                    } else if (fileName.endsWith(".tiff")||fileName.endsWith(".tif")) {// TIFF文件
                        System.out.println("发现TIFF文件: " + filePath);
                        satellite.setSourcefilepath(filePath);
                        satellite.setSourcefilename(fileName);
                    } else if (fileName.endsWith(".xml")) {
                        System.out.println("发现XML文件: " + filePath);
                        try {
                            satellite = readXMLFile(filePath, province, city, remark, collectTime, satellite);
                        } catch (Exception e) {
                            System.out.println("Error reading XML file: " + e.getMessage());
                        }
                    }
                }
            }
            satelliteService.insertSatellite(satellite);
            if ("1".equals(isSaveRecord)) {
                // 封装并保存收藏记录
                saveRecordInfo(satellite);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            throw new RuntimeException("解压失败");
        }
        return satellite.getId();
    }

    /**
     * 保存收藏记录
     * @param satellite
     */
    private void saveRecordInfo(Satellite satellite) {
        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setSatelliteId(satellite.getId());
        recordInfo.setSatelliteType(satellite.getSatelliteType());
        recordInfo.setSensorType(satellite.getSensorType());
        recordInfo.setCollectTime(satellite.getCollectTime());
        recordInfo.setViewPath(satellite.getViewPath());
        recordInfo.setViewRow(satellite.getViewRow());
        recordInfo.setStarPath(satellite.getStarPath());
        recordInfo.setStarRow(satellite.getStarRow());
        recordInfo.setStartTime(satellite.getStarttime());
        recordInfo.setEndTime(satellite.getEndtime());
        recordInfo.setLeftupLatitude(satellite.getLeftupLatitude());
        recordInfo.setLeftupLongitude(satellite.getLeftupLongitude());
        recordInfo.setRightupLatitude(satellite.getRightupLatitude());
        recordInfo.setRightupLongitude(satellite.getRightupLongitude());
        recordInfo.setRightdownLatitude(satellite.getRightdownLatitude());
        recordInfo.setRightdownLongitude(satellite.getRightdownLongitude());
        recordInfo.setLeftdownLatitude(satellite.getLeftdownLatitude());
        recordInfo.setLeftdownLongitude(satellite.getLeftdownLongitude());
        LoginUser loginUser = SecurityUtils.getLoginUser();
        recordInfo.setCreateBy(loginUser.getUsername());
        recordInfo.setUserId(loginUser.getUserId());
        recordInfo.setProvince(satellite.getProvince());
        recordInfo.setCity(satellite.getCity());
        recordInfo.setCreateBy(loginUser.getUsername());
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();  //本地服务器地址

        String image = satellite.getImage();
        image = image.replace(filePath, prefixUrl+"/profile/upload");
        recordInfo.setImage(image);
        recordInfoService.insertRecordInfo(recordInfo);
    }

}


