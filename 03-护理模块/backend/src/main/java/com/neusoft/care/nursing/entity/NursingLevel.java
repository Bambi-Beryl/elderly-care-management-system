package com.neusoft.care.nursing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("nursing_level")
public class NursingLevel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String levelCode;
    private String levelName;
    private String description;
    private String frequency;
    private BigDecimal price;
    @TableField(exist = false)
    private List<NursingItem> items;
}
