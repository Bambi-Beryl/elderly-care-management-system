package com.neusoft.care.customer.controller;

import com.neusoft.care.common.Result;
import com.neusoft.care.customer.dto.CheckInRegisterDTO;
import com.neusoft.care.customer.entity.*;
import com.neusoft.care.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    public Result<List<Customer>> list(@RequestParam(required = false) String name) {
        return Result.ok(customerService.listCustomers(name));
    }

    @GetMapping("/{id}")
    public Result<Customer> get(@PathVariable Long id) {
        return Result.ok(customerService.getCustomer(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.ok();
    }

    @GetMapping("/check-in/list")
    public Result<List<CheckInRecord>> checkInList() {
        return Result.ok(customerService.listCheckIn());
    }

    @PostMapping("/check-in")
    public Result<Void> checkIn(@RequestBody CheckInRecord record) {
        customerService.checkIn(record);
        return Result.ok();
    }

    @PostMapping("/check-in/register")
    public Result<Void> registerCheckIn(@RequestBody CheckInRegisterDTO dto) {
        customerService.registerCheckIn(dto);
        return Result.ok();
    }

    @DeleteMapping("/check-in/{id}")
    public Result<Void> deleteCheckIn(@PathVariable Long id) {
        customerService.deleteCheckIn(id);
        return Result.ok();
    }

    @GetMapping("/check-out/list")
    public Result<List<CheckOutRequest>> checkOutList() {
        return Result.ok(customerService.listCheckOut());
    }

    @PostMapping("/check-out/submit")
    public Result<Void> submitCheckOut(@RequestBody CheckOutRequest request) {
        customerService.submitCheckOut(request);
        return Result.ok();
    }

    @PostMapping("/check-out/audit")
    public Result<Void> auditCheckOut(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer status = Integer.valueOf(body.get("status").toString());
        String auditor = body.getOrDefault("auditor", "管理员").toString();
        customerService.auditCheckOut(id, status, auditor);
        return Result.ok();
    }

    @DeleteMapping("/check-out/{id}")
    public Result<Void> deleteCheckOut(@PathVariable Long id) {
        customerService.deleteCheckOut(id);
        return Result.ok();
    }

    @GetMapping("/outing/list")
    public Result<List<OutingRequest>> outingList() {
        return Result.ok(customerService.listOuting());
    }

    @PostMapping("/outing/submit")
    public Result<Void> submitOuting(@RequestBody OutingRequest request) {
        customerService.submitOuting(request);
        return Result.ok();
    }

    @PostMapping("/outing/audit")
    public Result<Void> auditOuting(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer status = Integer.valueOf(body.get("status").toString());
        String auditor = body.getOrDefault("auditor", "管理员").toString();
        customerService.auditOuting(id, status, auditor);
        return Result.ok();
    }

    @DeleteMapping("/outing/{id}")
    public Result<Void> deleteOuting(@PathVariable Long id) {
        customerService.deleteOuting(id);
        return Result.ok();
    }
}
