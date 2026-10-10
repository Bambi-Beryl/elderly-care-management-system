package com.neusoft.care.bed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.bed.entity.BedExchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BedExchangeMapper extends BaseMapper<BedExchange> {
    @Select("SELECT e.*, c.name AS customer_name, " +
            "CONCAT(r1.room_no,'-',b1.bed_no) AS from_bed_no, CONCAT(r2.room_no,'-',b2.bed_no) AS to_bed_no " +
            "FROM bed_exchange e LEFT JOIN customer c ON e.customer_id=c.id " +
            "LEFT JOIN bed b1 ON e.from_bed_id=b1.id LEFT JOIN room r1 ON b1.room_id=r1.id " +
            "LEFT JOIN bed b2 ON e.to_bed_id=b2.id LEFT JOIN room r2 ON b2.room_id=r2.id ORDER BY e.id DESC")
    List<BedExchange> selectWithDetail();
}
