package com.neusoft.care.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.customer.entity.OutingRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OutingRequestMapper extends BaseMapper<OutingRequest> {
    @Select("SELECT r.*, c.name AS customer_name FROM outing_request r " +
            "LEFT JOIN customer c ON r.customer_id = c.id ORDER BY r.id DESC")
    List<OutingRequest> selectWithCustomer();
}
