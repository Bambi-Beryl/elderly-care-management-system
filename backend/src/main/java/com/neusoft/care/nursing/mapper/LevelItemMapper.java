package com.neusoft.care.nursing.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface LevelItemMapper {
    @Select("SELECT item_id FROM level_item WHERE level_id=#{levelId}")
    List<Long> selectItemIdsByLevel(Long levelId);
}
