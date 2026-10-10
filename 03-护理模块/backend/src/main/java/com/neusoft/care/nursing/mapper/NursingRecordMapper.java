package com.neusoft.care.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.care.nursing.entity.NursingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface NursingRecordMapper extends BaseMapper<NursingRecord> {
    @Select("SELECT nr.*, c.name AS customer_name, ni.item_name FROM nursing_record nr " +
            "LEFT JOIN customer c ON nr.customer_id=c.id LEFT JOIN nursing_item ni ON nr.item_id=ni.id ORDER BY nr.nursing_time DESC")
    List<NursingRecord> selectWithDetail();
}
