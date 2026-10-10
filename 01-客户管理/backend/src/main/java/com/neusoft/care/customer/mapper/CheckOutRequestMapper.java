package com.neusoft.care.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.customer.entity.CheckOutRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CheckOutRequestMapper extends BaseMapper<CheckOutRequest> {
    @Select("SELECT r.*, c.name AS customer_name FROM check_out_request r " +
            "LEFT JOIN customer c ON r.customer_id = c.id ORDER BY r.id DESC")
    List<CheckOutRequest> selectWithCustomer();
}
