package com.skzh.thematic.service.impl;

import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.utils.DateUtils;
import com.skzh.thematic.domain.Thematic;
import com.skzh.thematic.mapper.ThematicMapper;
import com.skzh.thematic.service.ThematicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhl
 */
@Service
public class ThematicServiceImpl implements ThematicService {

    @Autowired(required = false)
    private ThematicMapper thematicMapper;

    @Override
    public List<Thematic> getThematic(Thematic thematic) {
        return thematicMapper.getThematic(thematic);
    }
    @Override
    public List<Thematic> imageTypeGet() {
        return thematicMapper.imageTypeGet();
    }

    @Override
    public void updateThematicMap(Thematic thematic) {
        thematicMapper.updateThematicMap(thematic);
    }

    @Override
    public Thematic selectInfoById(Long id) {
        return thematicMapper.selectInfoById(id);
    }

    @Override
    public Thematic selectInfoByCode(String code) {
        return thematicMapper.selectInfoByCode(code);
    }

    @Override
    public AjaxResult DeleteArticle (String id) {
        Integer result = thematicMapper.DeleteArticle(id);
        if (result == null || result == 0) {
            return AjaxResult.success("专题图删除成功");
        } else {
            return AjaxResult.success("专题图删除失败，请联系管理人员");
        }
    }

    /**
     * 新增专题图
     * @return 结果
     */
    @Override
    public int addThematicMap(Thematic thematic) {
        thematic.setCreateTime(DateUtils.getNowDate());
        return thematicMapper.addThematicMap(thematic);
    }

}
