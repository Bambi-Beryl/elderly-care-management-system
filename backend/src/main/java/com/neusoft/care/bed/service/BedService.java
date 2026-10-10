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
/**
 * 床位服务类
 * 负责床位、房间、床位调换的业务逻辑
 *
 * @author xxx
 * @since 2026-06-01
 */
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
    /**
     * 查询指定房间的空闲床位（status=0）
     *
     * @param roomId 房间ID
     */
    public List<Bed> listVacantByRoom(Long roomId) {
        return bedMapper.selectList(new LambdaQueryWrapper<Bed>()
                .eq(Bed::getRoomId, roomId)
                .eq(Bed::getStatus, 0)
                .orderByAsc(Bed::getBedNo));
    }

    public List<Map<String, Object>> floorStats() {
        return bedMapper.selectFloorStats();
    }
    /**
     * 床位调换（事务）
     * 记录调换记录，并更新两张床位状态
     *
     * @param exchange 调换信息
     */
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
    /**
     * 修改床位状态
     *
     * @param id     床位ID
     * @param status 状态
     */
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
    /**
     * 删除房间
     *
     * @param id 房间ID
     */
    public void deleteRoom(Long id) {
        roomMapper.deleteById(id);
    }
    /**
     * 保存/更新床位
     *
     * @param bed 床位信息
     */
    public void saveBed(Bed bed) {
        if (bed.getId() == null) bedMapper.insert(bed);
        else bedMapper.updateById(bed);
    }
    /**
     * 删除床位
     *
     * @param id 床位ID
     */
    public void deleteBed(Long id) {
        bedMapper.deleteById(id);
    }

    public void deleteExchange(Long id) {
        bedExchangeMapper.deleteById(id);
    }
}
