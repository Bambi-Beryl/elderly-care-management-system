package com.neusoft.care.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.customer.entity.CheckInRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CheckInRecordMapper extends BaseMapper<CheckInRecord> {
    @Select("SELECT r.*, c.name AS customer_name, CONCAT(rm.room_no, '-', b.bed_no) AS bed_no " +
            "FROM check_in_record r LEFT JOIN customer c ON r.customer_id = c.id " +
            "LEFT JOIN bed b ON r.bed_id = b.id LEFT JOIN room rm ON b.room_id = rm.id ORDER BY r.id DESC")
    List<CheckInRecord> selectWithDetail();
}
