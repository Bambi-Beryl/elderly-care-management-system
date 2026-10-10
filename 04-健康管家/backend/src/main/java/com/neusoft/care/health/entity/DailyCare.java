package com.neusoft.care.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_care")
public class DailyCare {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long stewardId;
    private LocalDate careDate;
    private String bloodPressure;
    private Integer heartRate;
    private BigDecimal bloodSugar;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String mood;
    private String diet;
    private String sleep;
    private String activity;
    private String remark;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String stewardName;
}
