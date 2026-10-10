package com.neusoft.care.bed.controller;

import com.neusoft.care.bed.entity.*;
import com.neusoft.care.bed.service.BedService;
import com.neusoft.care.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bed")
@RequiredArgsConstructor
public class BedController {
    private final BedService bedService;

    @GetMapping("/list")
    public Result<List<Bed>> list() {
        return Result.ok(bedService.listBeds());
    }

    @GetMapping("/rooms")
    public Result<List<Room>> rooms() {
        return Result.ok(bedService.listRooms());
    }

    @GetMapping("/rooms/grouped")
    public Result<List<Map<String, Object>>> roomsGrouped() {
        return Result.ok(bedService.listRoomsGrouped());
    }

    @GetMapping("/vacant")
    public Result<List<Bed>> vacant(@RequestParam Long roomId) {
        return Result.ok(bedService.listVacantByRoom(roomId));
    }

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> stats() {
        return Result.ok(bedService.floorStats());
    }

    @GetMapping("/exchange/list")
    public Result<List<BedExchange>> exchangeList() {
        return Result.ok(bedService.listExchange());
    }

    @PostMapping("/exchange")
    public Result<Void> exchange(@RequestBody BedExchange exchange) {
        bedService.exchangeBed(exchange);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        bedService.updateBedStatus(id, status);
        return Result.ok();
    }

    @PostMapping("/rooms")
    public Result<Void> saveRoom(@RequestBody Room room) {
        bedService.saveRoom(room);
        return Result.ok();
    }

    @DeleteMapping("/rooms/{id}")
    public Result<Void> deleteRoom(@PathVariable Long id) {
        bedService.deleteRoom(id);
        return Result.ok();
    }

    @PostMapping("/save")
    public Result<Void> saveBed(@RequestBody Bed bed) {
        bedService.saveBed(bed);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBed(@PathVariable Long id) {
        bedService.deleteBed(id);
        return Result.ok();
    }

    @DeleteMapping("/exchange/{id}")
    public Result<Void> deleteExchange(@PathVariable Long id) {
        bedService.deleteExchange(id);
        return Result.ok();
    }
}
