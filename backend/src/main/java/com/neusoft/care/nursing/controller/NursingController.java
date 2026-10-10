package com.neusoft.care.nursing.controller;

import com.neusoft.care.common.Result;
import com.neusoft.care.nursing.entity.*;
import com.neusoft.care.nursing.service.NursingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nursing")
@RequiredArgsConstructor
public class NursingController {
    private final NursingService nursingService;

    @GetMapping("/items")
    public Result<List<NursingItem>> items() {
        return Result.ok(nursingService.listItems());
    }

    @PostMapping("/items")
    public Result<Void> saveItem(@RequestBody NursingItem item) {
        nursingService.saveItem(item);
        return Result.ok();
    }

    @DeleteMapping("/items/{id}")
    public Result<Void> deleteItem(@PathVariable Long id) {
        nursingService.deleteItem(id);
        return Result.ok();
    }

    @GetMapping("/levels")
    public Result<List<NursingLevel>> levels() {
        return Result.ok(nursingService.listLevels());
    }

    @PostMapping("/levels")
    public Result<Void> saveLevel(@RequestBody NursingLevel level) {
        nursingService.saveLevel(level);
        return Result.ok();
    }

    @DeleteMapping("/levels/{id}")
    public Result<Void> deleteLevel(@PathVariable Long id) {
        nursingService.deleteLevel(id);
        return Result.ok();
    }

    @GetMapping("/customer-settings")
    public Result<List<CustomerNursing>> customerSettings() {
        return Result.ok(nursingService.listCustomerNursing());
    }

    @PostMapping("/customer-settings")
    public Result<Void> saveCustomerSetting(@RequestBody CustomerNursing cn) {
        nursingService.saveCustomerNursing(cn);
        return Result.ok();
    }

    @DeleteMapping("/customer-settings/{id}")
    public Result<Void> deleteCustomerSetting(@PathVariable Long id) {
        nursingService.deleteCustomerNursing(id);
        return Result.ok();
    }

    @GetMapping("/records")
    public Result<List<NursingRecord>> records() {
        return Result.ok(nursingService.listRecords());
    }

    @PostMapping("/records")
    public Result<Void> saveRecord(@RequestBody NursingRecord record) {
        nursingService.saveRecord(record);
        return Result.ok();
    }

    @DeleteMapping("/records/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        nursingService.deleteRecord(id);
        return Result.ok();
    }
}
