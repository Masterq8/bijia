package com.skzh.web.controller.ai;

import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.common.core.page.TableDataInfo;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.framework.config.ServerConfig;
import com.skzh.map.domain.Satellite;
import com.skzh.map.service.ISatelliteService;
import com.skzh.record.domain.RecordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.skzh.common.core.domain.AjaxResult.success;

@RestController
@RequestMapping("/ai/info" )
public class AiController  extends BaseController {

  
    @Autowired
    private ISatelliteService satelliteService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 处理分析
     */
    @PreAuthorize("@ss.hasPermi('record:info:list')" )
    @GetMapping("/list" )
    public TableDataInfo list(RecordInfo recordInfo) {

        List<Satellite> sateList = satelliteService.getListInIds(recordInfo.getSatelliteIds());
        if (!CollectionUtils.isEmpty(sateList)) {
            // 上传文件路径
            String filePath = SkzhConfig.getUploadPath();
            String prefixUrl = serverConfig.getUrl();
//            String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
            for (Satellite sl:sateList) {
                if (sl.getImage().indexOf(filePath) >= 0) {
                    String image = sl.getImage();
                    image = image.replace(filePath, prefixUrl+"/profile/upload");
                    sl.setImage(image);
                }
            }
        }
        return getDataTable(sateList);
    }

}
