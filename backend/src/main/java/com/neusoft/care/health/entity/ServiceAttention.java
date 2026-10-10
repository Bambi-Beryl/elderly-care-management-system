package com.neusoft.care.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("service_attention")
public class ServiceAttention {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String serviceName;
    private String serviceType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Integer status;
    private String remark;
    @TableField(exist = false)
    private String customerName;
}
