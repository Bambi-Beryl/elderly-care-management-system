package com.neusoft.care.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("check_in_record")
public class CheckInRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long bedId;
    private LocalDate checkInDate;
    private LocalDate contractExpireDate;
    private String contractNo;
    private java.math.BigDecimal deposit;
    private String remark;
    private String operator;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String bedNo;
}
