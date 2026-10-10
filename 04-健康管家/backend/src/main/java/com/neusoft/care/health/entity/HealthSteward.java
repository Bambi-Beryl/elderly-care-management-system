package com.neusoft.care.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("health_steward")
public class HealthSteward {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer gender;
    private String phone;
    private String department;
    private Integer status;
    private LocalDateTime createTime;
}
