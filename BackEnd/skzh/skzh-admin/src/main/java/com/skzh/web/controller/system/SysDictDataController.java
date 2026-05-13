package com.skzh.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.skzh.common.annotation.Log;
import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.domain.entity.SysDictData;
import com.skzh.common.core.page.TableDataInfo;
import com.skzh.common.enums.BusinessType;
import com.skzh.common.utils.StringUtils;
import com.skzh.common.utils.poi.ExcelUtil;
import com.skzh.system.service.ISysDictDataService;
import com.skzh.system.service.ISysDictTypeService;

/**
 * 数据字典信息
 *
 * @author skzh
 */
@RestController
@RequestMapping("/system/dict/data" )
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')" )
    @GetMapping("/list" )
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')" )
    @PostMapping("/export" )
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据" );
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')" )
    @GetMapping(value = "/{dictCode}" )
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }
    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
//    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/dictionary/{dictType}/{dictValue}")
    public AjaxResult getInfo2(@PathVariable String dictType, @PathVariable String dictValue) {
        return success(dictDataService.selectDictLabel(dictType, dictValue));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}" )
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')" )
    @Log(title = "字典数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')" )
    @Log(title = "字典数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')" )
    @Log(title = "字典类型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}" )
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
