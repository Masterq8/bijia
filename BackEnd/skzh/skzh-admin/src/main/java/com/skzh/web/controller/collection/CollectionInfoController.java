package com.skzh.web.controller.collection;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.domain.model.LoginUser;
import com.skzh.framework.config.ServerConfig;
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
import com.skzh.collection.domain.CollectionInfo;
import com.skzh.collection.service.ICollectionInfoService;
import com.skzh.common.utils.poi.ExcelUtil;
import com.skzh.common.core.page.TableDataInfo;

/**
 * 收藏Controller
 *
 * @author 朱继龙
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/collection/info" )
public class CollectionInfoController extends BaseController {

    @Autowired
    private ICollectionInfoService collectionInfoService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询收藏列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:info:list')" )*/
    @GetMapping("/list" )
    public TableDataInfo list(CollectionInfo collectionInfo) {
    LoginUser getLoginUser = getLoginUser();
    collectionInfo.setUserId(getLoginUser.getUserId());
        startPage();
        List<CollectionInfo> list = collectionInfoService.selectCollectionInfoList(collectionInfo);
        return getDataTable(list);
    }

    /**
     * 导出收藏列表
     */
/*    @PreAuthorize("@ss.hasPermi('system:info:export')" )*/
    @Log(title = "收藏" , businessType = BusinessType.EXPORT)
    @PostMapping("/export" )
    public void export(HttpServletResponse response, CollectionInfo collectionInfo) {
        List<CollectionInfo> list = collectionInfoService.selectCollectionInfoList(collectionInfo);
        ExcelUtil<CollectionInfo> util = new ExcelUtil<CollectionInfo>(CollectionInfo. class);
        util.exportExcel(response, list, "收藏数据" );
    }

    /**
     * 获取收藏详细信息
     */
/*    @PreAuthorize("@ss.hasPermi('system:info:query')" )*/
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return success(collectionInfoService.selectCollectionInfoById(id));
    }


    /**
     * 添加收藏
     */
/*    @PreAuthorize("@ss.hasPermi('system:info:add')" )*/
    @Log(title = "收藏" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CollectionInfo collectionInfo) {
        LoginUser getLoginUser = getLoginUser();
        collectionInfo.setUserId(getLoginUser.getUserId());
        if (collectionInfo.getId() != null) {
            collectionInfo.setSatelliteId(collectionInfo.getId());
            collectionInfo.setId(null);
        }
        // 上传文件路径
        String filePath = SkzhConfig.getUploadPath();
        String prefixUrl = serverConfig.getUrl();
//        String prefixUrl = "http://111.15.191.198:8066";// todo 演示时使用
        if (collectionInfo.getImage().indexOf(filePath) >= 0) {
            String image = collectionInfo.getImage();
            image = image.replace(filePath, prefixUrl+"/profile/upload");
            collectionInfo.setImage(image);
        }
        return toAjax(collectionInfoService.insertCollectionInfo(collectionInfo));
    }

    /**
     * 修改收藏
     */
/*    @PreAuthorize("@ss.hasPermi('system:info:edit')" )*/
    @Log(title = "收藏" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CollectionInfo collectionInfo) {
        return toAjax(collectionInfoService.updateCollectionInfo(collectionInfo));
    }

    /**
     * 删除收藏
     */
/*    @PreAuthorize("@ss.hasPermi('system:info:remove')" )*/
    @Log(title = "收藏" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(collectionInfoService.deleteCollectionInfoByIds(ids));
    }
}
