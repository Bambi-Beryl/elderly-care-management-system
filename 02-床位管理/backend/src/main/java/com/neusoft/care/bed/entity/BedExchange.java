package com.neusoft.care.bed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("bed_exchange")
public class BedExchange {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long fromBedId;
    private Long toBedId;
    private String reason;
    private String operator;
    private LocalDateTime exchangeTime;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String fromBedNo;
    @TableField(exist = false)
    private String toBedNo;
}
