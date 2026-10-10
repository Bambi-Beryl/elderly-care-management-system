package com.neusoft.care.customer.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CheckInRegisterDTO {
    private String name;
    private Integer gender;
    private LocalDate birthDate;
    private Integer age;
    private String idCard;
    private String bloodType;
    private String familyMember;
    private String phone;
    private String building;
    private Long roomId;
    private Long bedId;
    private LocalDate checkInDate;
    private LocalDate contractExpireDate;
    private String contractNo;
    private BigDecimal deposit;
    private String remark;
    private String operator;
}
