package com.skzh.thematic.service;

import com.skzh.common.core.domain.AjaxResult;
import com.skzh.map.domain.Satellite;
import com.skzh.thematic.domain.Thematic;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dell
 */
@Component
public interface ThematicService {
    List<Thematic> getThematic(Thematic thematic);

    public AjaxResult DeleteArticle(String id);

   public int addThematicMap(Thematic thematic);

    List<Thematic> imageTypeGet();

    public void updateThematicMap(Thematic thematic);

    public Thematic selectInfoById(Long id);

    public Thematic selectInfoByCode(String code);
}
