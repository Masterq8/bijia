package com.skzh.web.controller.map;

import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.page.TableDataInfo;
import com.skzh.map.domain.DataInfo;

import com.skzh.map.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data/info")
public class DataInfoController extends BaseController {

    @Autowired
    private DataInfoService dataInfoService;


    /*
    * 查询数据类型列表
    * */
    @GetMapping("/list")
    public AjaxResult list(DataInfo dataInfo) {
        List<DataInfo> list = dataInfoService.selectDataInfoListByType("sys_data_series");
        for(DataInfo info : list) {
            List<DataInfo> sublist = dataInfoService.selectDataInfoListByType(info.getDictValue());
            info.setChildren(sublist);
        }
        return success(list);
    }
}
