package com.neusoft.care.nursing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("nursing_record")
public class NursingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long itemId;
    private LocalDateTime nursingTime;
    private String staffName;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String itemName;
}
