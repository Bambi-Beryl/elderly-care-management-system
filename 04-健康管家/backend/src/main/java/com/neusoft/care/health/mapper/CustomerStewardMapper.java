package com.neusoft.care.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.health.entity.CustomerSteward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CustomerStewardMapper extends BaseMapper<CustomerSteward> {
    @Select("SELECT cs.*, c.name AS customer_name, hs.name AS steward_name FROM customer_steward cs " +
            "LEFT JOIN customer c ON cs.customer_id=c.id LEFT JOIN health_steward hs ON cs.steward_id=hs.id ORDER BY cs.id DESC")
    List<CustomerSteward> selectWithDetail();
}
