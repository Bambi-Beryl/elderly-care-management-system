package com.neusoft.care.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("outing_request")
public class OutingRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String reason;
    private LocalDate outingDate;
    private LocalDate returnDate;
    private Integer status;
    private LocalDateTime applyTime;
    private LocalDateTime auditTime;
    private String auditor;
    @TableField(exist = false)
    private String customerName;
}
