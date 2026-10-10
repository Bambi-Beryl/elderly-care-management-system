package com.neusoft.care.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.health.entity.DailyCare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DailyCareMapper extends BaseMapper<DailyCare> {
    @Select("SELECT dc.*, c.name AS customer_name, hs.name AS steward_name FROM daily_care dc " +
            "LEFT JOIN customer c ON dc.customer_id=c.id LEFT JOIN health_steward hs ON dc.steward_id=hs.id ORDER BY dc.care_date DESC")
    List<DailyCare> selectWithDetail();
}
