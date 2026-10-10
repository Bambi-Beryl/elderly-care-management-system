package com.neusoft.care.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("check_out_request")
public class CheckOutRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String reason;
    private Integer status;
    private LocalDateTime applyTime;
    private LocalDateTime auditTime;
    private String auditor;
    @TableField(exist = false)
    private String customerName;
}
