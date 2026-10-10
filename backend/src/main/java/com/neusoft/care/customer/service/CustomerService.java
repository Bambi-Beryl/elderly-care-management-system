package com.neusoft.care.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.bed.entity.Bed;
import com.neusoft.care.bed.entity.Room;
import com.neusoft.care.bed.mapper.BedMapper;
import com.neusoft.care.bed.mapper.RoomMapper;
import com.neusoft.care.customer.dto.CheckInRegisterDTO;
import com.neusoft.care.customer.entity.*;
import com.neusoft.care.customer.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
/**
 * 客户业务服务层
 * 核心职责：处理客户的入住、外出、退房、状态管理等全流程业务逻辑
 * 包含事务控制、数据校验、关联表操作
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;
    private final CheckInRecordMapper checkInRecordMapper;
    private final CheckOutRequestMapper checkOutRequestMapper;
    private final OutingRequestMapper outingRequestMapper;
    private final BedMapper bedMapper;
    private final RoomMapper roomMapper;
    /**
     * 查询客户列表（带床位/房间信息）
     * 支持按客户姓名模糊查询，无姓名时返回全部
     * @param name 客户姓名（可为空）
     * @return 客户列表（含床位、房间信息）
     */
    public List<Customer> listCustomers(String name) {
        if (name != null && !name.isBlank()) {
            return customerMapper.selectList(new LambdaQueryWrapper<Customer>().like(Customer::getName, name));
        }
        return customerMapper.selectWithBed();
    }

    public Customer getCustomer(Long id) {
        return customerMapper.selectById(id);
    }
    /**
     * 新增或更新客户信息
     * 若客户ID为空则新增，否则更新；带事务保证数据一致性
     * @param customer 客户对象
     */
    @Transactional
    public void saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customerMapper.insert(customer);
        } else {
            customerMapper.updateById(customer);
        }
    }
    /**
     * 根据客户ID删除客户
     * @param id 客户ID
     */
    public void deleteCustomer(Long id) {
        customerMapper.deleteById(id);
    }

    public List<CheckInRecord> listCheckIn() {
        return checkInRecordMapper.selectWithDetail();
    }

    @Transactional
    public void checkIn(CheckInRecord record) {
        validateContractDates(record.getCheckInDate(), record.getContractExpireDate());
        checkInRecordMapper.insert(record);
        Customer customer = customerMapper.selectById(record.getCustomerId());
        if (customer != null) {
            customer.setBedId(record.getBedId());
            customer.setCheckInDate(record.getCheckInDate());
            customer.setStatus(1);
            customerMapper.updateById(customer);
        }
        occupyBed(record.getBedId(), record.getCustomerId());
    }
    /**
     * 新客户入住登记（新增客户 + 办理入住）
     * 流程：参数校验 → 合同日期校验 → 构建客户对象并入库 → 后续可扩展入住记录生成
     * @param dto 入住登记DTO，包含客户信息、床位ID、入住日期、合同信息等
     * @throws IllegalArgumentException 当入住日期或床位ID为空时抛出
     */
    @Transactional
    public void registerCheckIn(CheckInRegisterDTO dto) {
        if (dto.getCheckInDate() == null || dto.getBedId() == null) {
            throw new IllegalArgumentException("入住日期和床位不能为空");
        }
        validateContractDates(dto.getCheckInDate(), dto.getContractExpireDate());

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setGender(dto.getGender());
        customer.setBirthDate(dto.getBirthDate());
        customer.setAge(calcAge(dto.getBirthDate(), dto.getAge()));
        customer.setIdCard(dto.getIdCard());
        customer.setBloodType(dto.getBloodType());
        customer.setFamilyMember(dto.getFamilyMember());
        customer.setPhone(dto.getPhone());
        customer.setBuilding(dto.getBuilding());
        customer.setEmergencyContact(dto.getFamilyMember());
        customer.setEmergencyPhone(dto.getPhone());
        customer.setCheckInDate(dto.getCheckInDate());
        customer.setBedId(dto.getBedId());
        customer.setStatus(1);
        if (customer.getBuilding() == null && dto.getRoomId() != null) {
            Room room = roomMapper.selectById(dto.getRoomId());
            if (room != null) {
                customer.setBuilding(room.getFloor() + "号楼");
            }
        }
        customerMapper.insert(customer);

        CheckInRecord record = new CheckInRecord();
        record.setCustomerId(customer.getId());
        record.setBedId(dto.getBedId());
        record.setCheckInDate(dto.getCheckInDate());
        record.setContractExpireDate(dto.getContractExpireDate());
        record.setContractNo(dto.getContractNo());
        record.setDeposit(dto.getDeposit());
        record.setRemark(dto.getRemark());
        record.setOperator(dto.getOperator());
        checkInRecordMapper.insert(record);
        occupyBed(dto.getBedId(), customer.getId());
    }
    /**
     * 床位占用校验与绑定（内部工具方法）
     * 用于办理入住时，校验床位状态并更新为已占用，绑定客户
     * @param bedId 要占用的床位ID
     * @param customerId 要绑定的客户ID
     * @throws IllegalArgumentException 床位不存在或已被占用时抛出
     */
    private void occupyBed(Long bedId, Long customerId) {
        Bed bed = bedMapper.selectById(bedId);
        if (bed == null) {
            throw new IllegalArgumentException("床位不存在");
        }
        if (bed.getStatus() != null && bed.getStatus() == 1) {
            throw new IllegalArgumentException("该床位已被占用");
        }
        bed.setStatus(1);
        bed.setCustomerId(customerId);
        bedMapper.updateById(bed);
    }

    private void validateContractDates(LocalDate checkIn, LocalDate expire) {
        if (checkIn != null && expire != null && expire.isBefore(checkIn)) {
            throw new IllegalArgumentException("合同到期时间不能早于入住时间");
        }
    }

    private int calcAge(LocalDate birthDate, Integer fallback) {
        if (birthDate != null) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return fallback != null ? fallback : 0;
    }

    public void deleteCheckIn(Long id) {
        checkInRecordMapper.deleteById(id);
    }

    public List<CheckOutRequest> listCheckOut() {
        return checkOutRequestMapper.selectWithCustomer();
    }

    public void submitCheckOut(CheckOutRequest request) {
        request.setStatus(0);
        request.setApplyTime(LocalDateTime.now());
        checkOutRequestMapper.insert(request);
    }
    /**
     * 退房申请审核
     * 用于管理员审核客户提交的退房申请，更新申请状态并处理客户和床位信息
     * @param id 退房申请ID
     * @param status 审核状态（1=通过，其他=拒绝）
     * @param auditor 审核人姓名/账号
     */
    @Transactional
    public void auditCheckOut(Long id, Integer status, String auditor) {
        CheckOutRequest request = checkOutRequestMapper.selectById(id);
        if (request == null) return;
        request.setStatus(status);
        request.setAuditor(auditor);
        request.setAuditTime(LocalDateTime.now());
        checkOutRequestMapper.updateById(request);
        if (status == 1) {
            Customer customer = customerMapper.selectById(request.getCustomerId());
            if (customer != null) {
                customer.setStatus(3);
                customer.setBedId(null);
                customerMapper.updateById(customer);
            }
        }
    }

    public void deleteCheckOut(Long id) {
        checkOutRequestMapper.deleteById(id);
    }

    public List<OutingRequest> listOuting() {
        return outingRequestMapper.selectWithCustomer();
    }

    public void submitOuting(OutingRequest request) {
        request.setStatus(0);
        request.setApplyTime(LocalDateTime.now());
        outingRequestMapper.insert(request);
    }
    /**
     * 提交外出申请
     * 客户发起外出申请，状态默认设为待审核
     */
    @Transactional
    public void auditOuting(Long id, Integer status, String auditor) {
        OutingRequest request = outingRequestMapper.selectById(id);
        if (request == null) return;
        request.setStatus(status);
        request.setAuditor(auditor);
        request.setAuditTime(LocalDateTime.now());
        outingRequestMapper.updateById(request);
        if (status == 1) {
            Customer customer = customerMapper.selectById(request.getCustomerId());
            if (customer != null) {
                customer.setStatus(2);
                customerMapper.updateById(customer);
            }
        } else if (status == 3) {
            Customer customer = customerMapper.selectById(request.getCustomerId());
            if (customer != null) {
                customer.setStatus(1);
                customerMapper.updateById(customer);
            }
        }
    }

    public void deleteOuting(Long id) {
        outingRequestMapper.deleteById(id);
    }
}
