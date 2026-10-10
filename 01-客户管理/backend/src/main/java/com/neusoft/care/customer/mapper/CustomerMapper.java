package com.neusoft.care.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.customer.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    @Select("SELECT c.*, CONCAT(r.room_no, '-', b.bed_no) AS bed_no, r.room_no FROM customer c " +
            "LEFT JOIN bed b ON c.bed_id = b.id LEFT JOIN room r ON b.room_id = r.id ORDER BY c.id")
    List<Customer> selectWithBed();
}
