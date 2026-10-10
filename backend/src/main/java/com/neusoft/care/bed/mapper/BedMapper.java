package com.neusoft.care.bed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.bed.entity.Bed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface BedMapper extends BaseMapper<Bed> {
    @Select("SELECT b.*, r.room_no, r.room_type, r.floor, c.name AS customer_name FROM bed b " +
            "LEFT JOIN room r ON b.room_id = r.id LEFT JOIN customer c ON b.customer_id = c.id ORDER BY r.floor, r.room_no, b.bed_no")
    List<Bed> selectWithDetail();

    @Select("SELECT r.floor, COUNT(*) AS total, SUM(CASE WHEN b.status=1 THEN 1 ELSE 0 END) AS occupied, " +
            "SUM(CASE WHEN b.status=0 THEN 1 ELSE 0 END) AS free FROM bed b " +
            "LEFT JOIN room r ON b.room_id = r.id GROUP BY r.floor ORDER BY r.floor")
    List<Map<String, Object>> selectFloorStats();
}
