package com.neusoft.care.health.controller;

import com.neusoft.care.common.Result;
import com.neusoft.care.health.entity.*;
import com.neusoft.care.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    private final HealthService healthService;

    @GetMapping("/stewards")
    public Result<List<HealthSteward>> stewards() {
        return Result.ok(healthService.listStewards());
    }

    @PostMapping("/stewards")
    public Result<Void> saveSteward(@RequestBody HealthSteward steward) {
        healthService.saveSteward(steward);
        return Result.ok();
    }

    @DeleteMapping("/stewards/{id}")
    public Result<Void> deleteSteward(@PathVariable Long id) {
        healthService.deleteSteward(id);
        return Result.ok();
    }

    @GetMapping("/customer-stewards")
    public Result<List<CustomerSteward>> customerStewards() {
        return Result.ok(healthService.listCustomerStewards());
    }

    @PostMapping("/customer-stewards")
    public Result<Void> saveCustomerSteward(@RequestBody CustomerSteward cs) {
        healthService.saveCustomerSteward(cs);
        return Result.ok();
    }

    @DeleteMapping("/customer-stewards/{id}")
    public Result<Void> deleteCustomerSteward(@PathVariable Long id) {
        healthService.deleteCustomerSteward(id);
        return Result.ok();
    }

    @GetMapping("/service-attention")
    public Result<List<ServiceAttention>> serviceAttention() {
        return Result.ok(healthService.listServiceAttention());
    }

    @PostMapping("/service-attention")
    public Result<Void> saveServiceAttention(@RequestBody ServiceAttention sa) {
        healthService.saveServiceAttention(sa);
        return Result.ok();
    }

    @DeleteMapping("/service-attention/{id}")
    public Result<Void> deleteServiceAttention(@PathVariable Long id) {
        healthService.deleteServiceAttention(id);
        return Result.ok();
    }

    @GetMapping("/daily-care")
    public Result<List<DailyCare>> dailyCare() {
        return Result.ok(healthService.listDailyCare());
    }

    @PostMapping("/daily-care")
    public Result<Void> saveDailyCare(@RequestBody DailyCare dc) {
        healthService.saveDailyCare(dc);
        return Result.ok();
    }

    @DeleteMapping("/daily-care/{id}")
    public Result<Void> deleteDailyCare(@PathVariable Long id) {
        healthService.deleteDailyCare(id);
        return Result.ok();
    }
}
