package com.neusoft.care.health.service;

import com.neusoft.care.health.entity.*;
import com.neusoft.care.health.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 健康服务业务类
 * 负责健康管家、客户管家关联、服务关注、日常照料等模块业务处理
 *
 * @author xxx
 * @since 2026-06-01
 */
@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthStewardMapper stewardMapper;
    private final CustomerStewardMapper customerStewardMapper;
    private final ServiceAttentionMapper serviceAttentionMapper;
    private final DailyCareMapper dailyCareMapper;
    /**
     * 查询所有健康管家信息
     */
    public List<HealthSteward> listStewards() {
        return stewardMapper.selectList(null);
    }
    /**
     * 新增/编辑健康管家
     * @param steward 健康管家信息
     */
    public void saveSteward(HealthSteward steward) {
        if (steward.getId() == null) stewardMapper.insert(steward);
        else stewardMapper.updateById(steward);
    }
    /**
     * 删除健康管家
     * @param id 管家ID
     */
    public void deleteSteward(Long id) {
        stewardMapper.deleteById(id);
    }
    /**
     * 查询客户与健康管家关联数据（含详情）
     */
    public List<CustomerSteward> listCustomerStewards() {
        return customerStewardMapper.selectWithDetail();
    }

    public void saveCustomerSteward(CustomerSteward cs) {
        if (cs.getId() == null) customerStewardMapper.insert(cs);
        else customerStewardMapper.updateById(cs);
    }

    public void deleteCustomerSteward(Long id) {
        customerStewardMapper.deleteById(id);
    }

    public List<ServiceAttention> listServiceAttention() {
        return serviceAttentionMapper.selectWithDetail();
    }
    /**
     * 新增/编辑服务关注信息
     * @param sa 服务关注对象
     */
    public void saveServiceAttention(ServiceAttention sa) {
        if (sa.getId() == null) serviceAttentionMapper.insert(sa);
        else serviceAttentionMapper.updateById(sa);
    }
    /**
     * 删除服务关注记录
     * @param id 关注记录ID
     */
    public void deleteServiceAttention(Long id) {
        serviceAttentionMapper.deleteById(id);
    }
    /**
     * 查询日常照料记录（含详情）
     */
    public List<DailyCare> listDailyCare() {
        return dailyCareMapper.selectWithDetail();
    }
    /**
     * 新增/编辑日常照料记录
     * @param dc 日常照料对象
     */
    public void saveDailyCare(DailyCare dc) {
        if (dc.getId() == null) dailyCareMapper.insert(dc);
        else dailyCareMapper.updateById(dc);
    }

    public void deleteDailyCare(Long id) {
        dailyCareMapper.deleteById(id);
    }
}
