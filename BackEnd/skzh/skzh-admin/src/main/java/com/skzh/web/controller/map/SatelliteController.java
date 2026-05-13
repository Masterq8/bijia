package com.skzh.web.controller.map;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.skzh.common.config.SkzhConfig;
import com.skzh.common.utils.StringUtils;
import com.skzh.framework.config.ServerConfig;
import com.skzh.map.dto.MapPoint;
import com.skzh.web.util.GraphMapUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.skzh.map.domain.Satellite;
import com.skzh.map.service.ISatelliteService;
import com.skzh.common.utils.poi.ExcelUtil;
import com.skzh.common.core.page.TableDataInfo;

/**
 * 卫星影像管理Controller
 *
 * @author zr
 * @date 2024-09-03
 */
@RestController
@RequestMapping("/map/satellite" )
public class SatelliteController extends BaseController {
    @Autowired
    private ISatelliteService satelliteService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询卫星影像管理列表
     */
    //@PreAuthorize("@ss.hasPermi('map:satellite:list')" )
    @GetMapping("/list" )
    public TableDataInfo list(Satellite satellite) {
        startPage();
        List<Satellite> list = satelliteService.selectSatelliteList(satellite);
        if (!CollectionUtils.isEmpty(list)) {
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            for (Satellite sl:list) {
                if (sl.getImage().indexOf(filePath) >= 0) {
                    String image = sl.getImage();
                    image = image.replace(filePath, prefixUrl+"/profile/upload");
                    sl.setImage(image);
                }
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/list1" )
    public TableDataInfo list1(Satellite satellite) {
        if (satellite.getPoints() == null) {// 省市检索
            startPage();
            List<Satellite> list = satelliteService.selectSatelliteList1(satellite);
            if (!CollectionUtils.isEmpty(list)) {
                // 上传文件路径
                String filePath = SkzhConfig.getUploadPath();
                String prefixUrl = serverConfig.getUrl();
                for (Satellite sl : list) {
                    if (sl.getImage().indexOf(filePath) >= 0) {
                        String image = sl.getImage();
                        image = image.replace(filePath, prefixUrl + "/profile/upload");
                        sl.setImage(image);
                    }
                }
            }
            return getDataTable(list);
        } else {// 自定义区域检索
            List<Satellite> resList = new ArrayList<>();
            List<Satellite> list = satelliteService.selectSatelliteList1(satellite);
            if (!CollectionUtils.isEmpty(list)) {
                // 上传文件路径
                String filePath = SkzhConfig.getUploadPath();
                String prefixUrl = serverConfig.getUrl();
                List<Point2D.Double> rangeList = new ArrayList<>();
                for (int i = 0; i < satellite.getPoints().size(); i ++) {
                    String jwPoint = satellite.getPoints().get(i);
                    String[] pointArr = jwPoint.split(",");
                    Point2D.Double item = new Point2D.Double();
                    item.setLocation(Double.parseDouble(pointArr[1]), Double.parseDouble(pointArr[0]));
                    rangeList.add(item);
                }
                for (Satellite sl:list) {
                    if (sl.getImage().indexOf(filePath) >= 0) {
                        String image = sl.getImage();
                        image = image.replace(filePath, prefixUrl+"/profile/upload");
                        sl.setImage(image);
                    }
                    Point2D.Double cpoint = new Point2D.Double();
                    if (sl.getCenterlatitude() != null && sl.getCenterlongitude() != null) {
                        cpoint.setLocation(sl.getCenterlatitude().doubleValue(), sl.getCenterlongitude().doubleValue());
                        boolean isRange = GraphMapUtils.isPtInPoly(cpoint, rangeList);
                        System.out.println(isRange);
                        if (isRange) {
                            resList.add(sl);
                        }
                    } else {
                        List<MapPoint> pList = new ArrayList<>();
                        MapPoint ltPoint = new MapPoint();
                        ltPoint.setLat(sl.getLeftupLatitude().doubleValue());
                        ltPoint.setLng(sl.getLeftupLongitude().doubleValue());
                        pList.add(ltPoint);
                        MapPoint ldPoint = new MapPoint();
                        ldPoint.setLat(sl.getLeftdownLatitude().doubleValue());
                        ldPoint.setLng(sl.getLeftdownLongitude().doubleValue());
                        pList.add(ldPoint);
                        MapPoint rtPoint = new MapPoint();
                        rtPoint.setLat(sl.getRightupLatitude().doubleValue());
                        rtPoint.setLng(sl.getRightupLongitude().doubleValue());
                        pList.add(rtPoint);
                        MapPoint rdPoint = new MapPoint();
                        rdPoint.setLat(sl.getRightdownLatitude().doubleValue());
                        rdPoint.setLng(sl.getRightdownLongitude().doubleValue());
                        pList.add(rdPoint);
                        MapPoint point = GraphMapUtils.getCenterPointFromList(pList);
                        satelliteService.updateCenterPoint(sl.getId(), point.getLat(), point.getLng());
                        cpoint.setLocation(point.getLat(), point.getLng());
                        boolean isRange = GraphMapUtils.isPtInPoly(cpoint, rangeList);
                        if (isRange) {
                            resList.add(sl);
                        }
                    }
                }
            }
            return formateTableList(resList);
        }
    }

    /**
     * 导出卫星影像管理列表
     */
    @PreAuthorize("@ss.hasPermi('map:satellite:export')" )
    @Log(title = "卫星影像管理" , businessType = BusinessType.EXPORT)
    @PostMapping("/export" )
    public void export(HttpServletResponse response, Satellite satellite) {
        List<Satellite> list = satelliteService.selectSatelliteList(satellite);
        ExcelUtil<Satellite> util = new ExcelUtil<Satellite>(Satellite. class);
        util.exportExcel(response, list, "卫星影像管理数据" );
    }

    /**
     * 获取卫星影像管理详细信息
     */
//    @PreAuthorize("@ss.hasPermi('map:satellite:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        Satellite satellite = satelliteService.selectSatelliteById(id);
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();
//        String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
        if (StringUtils.isNotEmpty(satellite.getImage())) {
            if (satellite.getImage().indexOf(filePath) >= 0) {
                String image = satellite.getImage();
                image = image.replace(filePath, prefixUrl+"/profile/upload");
                satellite.setImage(image);
            }
        }
        return success(satellite);
    }

    /**
     * 新增卫星影像管理
     */
    @PreAuthorize("@ss.hasPermi('map:satellite:add')" )
    @Log(title = "卫星影像管理" , businessType = BusinessType.INSERT)
    @PostMapping
    @CacheEvict(value = "satellite", key = "'satelliteList:' + #satellite.id") // 清除缓存
    public AjaxResult add(@RequestBody Satellite satellite) {
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();
//        String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
        // png处理
        String image = satellite.getImage();
        if (StringUtils.isNotEmpty(image)) {
            image = image.replace(prefixUrl+"/profile/upload", filePath);
            satellite.setImage(image);
        }
        // tiff处理
        String sourcefilepath = satellite.getSourcefilepath();
        if (StringUtils.isNotEmpty(sourcefilepath)) {
            sourcefilepath = sourcefilepath.replace(prefixUrl+"/profile/upload", filePath);
            satellite.setSourcefilepath(sourcefilepath);
        }
        return toAjax(satelliteService.insertSatellite(satellite));
    }

    /**
     * 修改卫星影像管理
     */
    @PreAuthorize("@ss.hasPermi('map:satellite:edit')" )
    @Log(title = "卫星影像管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    @CacheEvict(value = "satellite", key = "'satelliteList:' + #satellite.id") // 清除缓存
    public AjaxResult edit(@RequestBody Satellite satellite) {
        String image = satellite.getImage();
        String sourcefilepath = satellite.getSourcefilepath();
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();
//        String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
        // png处理
        if (StringUtils.isNotEmpty(image) && image.indexOf("http:") >= 0) {
            image = image.replace(prefixUrl+"/profile/upload", filePath);
            satellite.setImage(image);
        }
        // tiff处理
        if (StringUtils.isNotEmpty(sourcefilepath) && sourcefilepath.indexOf("http:") >= 0) {
            sourcefilepath = sourcefilepath.replace(prefixUrl+"/profile/upload", filePath);
            satellite.setSourcefilepath(sourcefilepath);
        }
        return toAjax(satelliteService.updateSatellite(satellite));
    }

    /**
     * 删除卫星影像管理
     */
    @PreAuthorize("@ss.hasPermi('map:satellite:remove')" )
    @Log(title = "卫星影像管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    @CacheEvict(value = "satellite",allEntries = true) // 清除缓存
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(satelliteService.deleteSatelliteByIds(ids));
    }
}
