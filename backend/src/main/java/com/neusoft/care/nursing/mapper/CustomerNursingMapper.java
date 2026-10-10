package com.neusoft.care.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.nursing.entity.CustomerNursing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CustomerNursingMapper extends BaseMapper<CustomerNursing> {
    @Select("SELECT cn.*, c.name AS customer_name, nl.level_name FROM customer_nursing cn " +
            "LEFT JOIN customer c ON cn.customer_id=c.id LEFT JOIN nursing_level nl ON cn.level_id=nl.id ORDER BY cn.id DESC")
    List<CustomerNursing> selectWithDetail();
}
