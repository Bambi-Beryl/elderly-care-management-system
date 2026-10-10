package com.neusoft.care.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("customer_steward")
public class CustomerSteward {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long stewardId;
    private LocalDate assignDate;
    private Integer status;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String stewardName;
}
