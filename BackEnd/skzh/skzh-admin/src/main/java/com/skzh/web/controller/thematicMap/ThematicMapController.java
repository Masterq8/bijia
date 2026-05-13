package com.skzh.web.controller.thematicMap;

import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.page.TableDataInfo;
import com.skzh.thematic.domain.Thematic;
import com.skzh.thematic.service.ThematicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理Controller
 *
 * @author skzh
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/thematicMap/thematicMap" )
public class ThematicMapController extends BaseController {
    @Autowired
    private ThematicService thematicService;

    @GetMapping("/getThematic")
    public TableDataInfo List(Thematic thematic) {
        startPage();
        List<Thematic> list = thematicService.getThematic(thematic);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return success(thematicService.selectInfoById(id));
    }

    /**
     * 查询专题图下拉框
     * * @return
     */
    @GetMapping("/imageTypeGet")
    public List<Thematic> imageTypeGet() {
        List<Thematic> list = thematicService.imageTypeGet();
        return list;
    }

    /**
     * 删除专题图
     */
    @DeleteMapping("/{id}" )
    public AjaxResult remove(@PathVariable("id") String id) {
        AjaxResult result = thematicService.DeleteArticle(id);
        return result;
    }

    /**
     * 新增专题图
     */
    @PostMapping("/addThematicMap")
    public AjaxResult add(@RequestBody Thematic thematic) {
        return toAjax(thematicService.addThematicMap(thematic));
    }

    /**
     * 修改专题图
     */
    @PutMapping
    public AjaxResult update(@RequestBody Thematic thematic) {
        try {
            thematicService.updateThematicMap(thematic);
        } catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("修改失败");
        }
        return AjaxResult.success("修改成功");
    }

    @GetMapping(value = "/getByCode/{code}" )
    public AjaxResult getInfoByCode(@PathVariable("code" ) String code) {
        return success(thematicService.selectInfoByCode(code));
    }
}

