package com.skzh.thematic.mapper;

import com.skzh.map.domain.Satellite;
import com.skzh.thematic.domain.Thematic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThematicMapper {

    List<Thematic> getThematic(Thematic thematic);

    Integer DeleteArticle(String id);

   public int addThematicMap(Thematic thematic);

    List<Thematic> imageTypeGet();

    Thematic getMake(String imageCategory);

    public void updateThematicMap(Thematic thematic);

    public Thematic selectInfoById(Long id);

    public Thematic selectInfoByCode(@Param("code") String code);

}
