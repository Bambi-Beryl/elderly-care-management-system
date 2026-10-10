package com.neusoft.care.bed.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.bed.entity.*;
import com.neusoft.care.bed.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedService {
    private final BedMapper bedMapper;
    private final RoomMapper roomMapper;
    private final BedExchangeMapper bedExchangeMapper;

    public List<Bed> listBeds() {
        return bedMapper.selectWithDetail();
    }

    public List<Room> listRooms() {
        return roomMapper.selectList(null);
    }

    public List<Map<String, Object>> listRoomsGrouped() {
        List<Room> rooms = roomMapper.selectList(
                new LambdaQueryWrapper<Room>().orderByAsc(Room::getFloor).orderByAsc(Room::getRoomNo));
        return rooms.stream()
                .collect(Collectors.groupingBy(Room::getFloor, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> group = new LinkedHashMap<>();
                    group.put("floor", e.getKey());
                    group.put("label", e.getKey() + "层");
                    group.put("rooms", e.getValue());
                    return group;
                })
                .toList();
    }

    public List<Bed> listVacantByRoom(Long roomId) {
        return bedMapper.selectList(new LambdaQueryWrapper<Bed>()
                .eq(Bed::getRoomId, roomId)
                .eq(Bed::getStatus, 0)
                .orderByAsc(Bed::getBedNo));
    }

    public List<Map<String, Object>> floorStats() {
        return bedMapper.selectFloorStats();
    }

    @Transactional
    public void exchangeBed(BedExchange exchange) {
        exchange.setExchangeTime(LocalDateTime.now());
        bedExchangeMapper.insert(exchange);
        Bed from = bedMapper.selectById(exchange.getFromBedId());
        Bed to = bedMapper.selectById(exchange.getToBedId());
        if (from != null && to != null) {
            Long customerId = from.getCustomerId();
            from.setCustomerId(null);
            from.setStatus(0);
            to.setCustomerId(customerId);
            to.setStatus(1);
            bedMapper.updateById(from);
            bedMapper.updateById(to);
        }
    }

    public List<BedExchange> listExchange() {
        return bedExchangeMapper.selectWithDetail();
    }

    public void updateBedStatus(Long id, Integer status) {
        Bed bed = bedMapper.selectById(id);
        if (bed != null) {
            bed.setStatus(status);
            bedMapper.updateById(bed);
        }
    }

    public void saveRoom(Room room) {
        if (room.getId() == null) roomMapper.insert(room);
        else roomMapper.updateById(room);
    }

    public void deleteRoom(Long id) {
        roomMapper.deleteById(id);
    }

    public void saveBed(Bed bed) {
        if (bed.getId() == null) bedMapper.insert(bed);
        else bedMapper.updateById(bed);
    }

    public void deleteBed(Long id) {
        bedMapper.deleteById(id);
    }

    public void deleteExchange(Long id) {
        bedExchangeMapper.deleteById(id);
    }
}
