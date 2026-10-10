package com.neusoft.care.health.service;

import com.neusoft.care.health.entity.*;
import com.neusoft.care.health.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthStewardMapper stewardMapper;
    private final CustomerStewardMapper customerStewardMapper;
    private final ServiceAttentionMapper serviceAttentionMapper;
    private final DailyCareMapper dailyCareMapper;

    public List<HealthSteward> listStewards() {
        return stewardMapper.selectList(null);
    }

    public void saveSteward(HealthSteward steward) {
        if (steward.getId() == null) stewardMapper.insert(steward);
        else stewardMapper.updateById(steward);
    }

    public void deleteSteward(Long id) {
        stewardMapper.deleteById(id);
    }

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

    public void saveServiceAttention(ServiceAttention sa) {
        if (sa.getId() == null) serviceAttentionMapper.insert(sa);
        else serviceAttentionMapper.updateById(sa);
    }

    public void deleteServiceAttention(Long id) {
        serviceAttentionMapper.deleteById(id);
    }

    public List<DailyCare> listDailyCare() {
        return dailyCareMapper.selectWithDetail();
    }

    public void saveDailyCare(DailyCare dc) {
        if (dc.getId() == null) dailyCareMapper.insert(dc);
        else dailyCareMapper.updateById(dc);
    }

    public void deleteDailyCare(Long id) {
        dailyCareMapper.deleteById(id);
    }
}
