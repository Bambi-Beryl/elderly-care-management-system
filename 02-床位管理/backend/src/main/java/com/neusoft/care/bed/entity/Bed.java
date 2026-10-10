package com.neusoft.care.bed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bed")
public class Bed {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long roomId;
    private String bedNo;
    private Integer status;
    private Long customerId;
    @TableField(exist = false)
    private String roomNo;
    @TableField(exist = false)
    private String roomType;
    @TableField(exist = false)
    private Integer floor;
    @TableField(exist = false)
    private String customerName;
}
