package com.neusoft.care.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("customer")
public class Customer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer gender;
    private Integer age;
    private LocalDate birthDate;
    private String bloodType;
    private String idCard;
    private String phone;
    private String familyMember;
    private String emergencyContact;
    private String building;
    private String emergencyPhone;
    private String address;
    private LocalDate checkInDate;
    private Long bedId;
    private Integer status;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private String bedNo;
    @TableField(exist = false)
    private String roomNo;
}
