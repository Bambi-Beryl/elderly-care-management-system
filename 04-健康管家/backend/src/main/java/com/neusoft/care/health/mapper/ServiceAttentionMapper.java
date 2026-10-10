package com.neusoft.care.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.health.entity.ServiceAttention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ServiceAttentionMapper extends BaseMapper<ServiceAttention> {
    @Select("SELECT sa.*, c.name AS customer_name FROM service_attention sa " +
            "LEFT JOIN customer c ON sa.customer_id=c.id ORDER BY sa.id DESC")
    List<ServiceAttention> selectWithDetail();
}
