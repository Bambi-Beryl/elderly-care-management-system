package com.neusoft.care.nursing.service;

import com.neusoft.care.nursing.entity.*;
import com.neusoft.care.nursing.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NursingService {
    private final NursingItemMapper itemMapper;
    private final NursingLevelMapper levelMapper;
    private final CustomerNursingMapper customerNursingMapper;
    private final NursingRecordMapper recordMapper;
    private final LevelItemMapper levelItemMapper;

    public List<NursingItem> listItems() {
        return itemMapper.selectList(null);
    }

    public void saveItem(NursingItem item) {
        if (item.getId() == null) itemMapper.insert(item);
        else itemMapper.updateById(item);
    }

    public void deleteItem(Long id) {
        itemMapper.deleteById(id);
    }

    public void saveLevel(NursingLevel level) {
        if (level.getId() == null) levelMapper.insert(level);
        else levelMapper.updateById(level);
    }

    public void deleteLevel(Long id) {
        levelMapper.deleteById(id);
    }

    public List<NursingLevel> listLevels() {
        List<NursingLevel> levels = levelMapper.selectList(null);
        for (NursingLevel level : levels) {
            List<Long> itemIds = levelItemMapper.selectItemIdsByLevel(level.getId());
            if (!itemIds.isEmpty()) {
                level.setItems(itemMapper.selectBatchIds(itemIds));
            }
        }
        return levels;
    }

    public List<CustomerNursing> listCustomerNursing() {
        return customerNursingMapper.selectWithDetail();
    }

    public void saveCustomerNursing(CustomerNursing cn) {
        if (cn.getId() == null) customerNursingMapper.insert(cn);
        else customerNursingMapper.updateById(cn);
    }

    public void deleteCustomerNursing(Long id) {
        customerNursingMapper.deleteById(id);
    }

    public List<NursingRecord> listRecords() {
        return recordMapper.selectWithDetail();
    }

    public void saveRecord(NursingRecord record) {
        if (record.getId() == null) recordMapper.insert(record);
        else recordMapper.updateById(record);
    }

    public void deleteRecord(Long id) {
        recordMapper.deleteById(id);
    }
}
