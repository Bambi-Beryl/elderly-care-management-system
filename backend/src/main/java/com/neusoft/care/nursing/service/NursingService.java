package com.neusoft.care.nursing.service;

import com.neusoft.care.nursing.entity.*;
import com.neusoft.care.nursing.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 护理服务业务类
 * <p>
 * 负责护理项目、护理等级、客户护理关系、护理记录等模块的业务逻辑处理
 *
 * @author （你的名字/团队）
 * @since （创建日期，如2026-06-01）
 */
@Service
@RequiredArgsConstructor
// ------------------------------ 依赖注入 ------------------------------
/**
 * 护理项目数据访问对象
 * 用于操作护理项目相关的数据库表
 */
public class NursingService {
    private final NursingItemMapper itemMapper;
    private final NursingLevelMapper levelMapper;
    private final CustomerNursingMapper customerNursingMapper;
    private final NursingRecordMapper recordMapper;
    private final LevelItemMapper levelItemMapper;
    // ------------------------------ 护理项目相关方法 ------------------------------

    /**
     * 查询所有护理项目列表
     *
     * @return 护理项目集合，包含系统中所有的护理项目信息
     */
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
    // ------------------------------ 护理等级相关方法 ------------------------------

    /**
     * 新增/更新护理等级
     * <p>
     * 根据护理等级ID判断操作类型：
     * <ul>
     *     <li>ID为null时：新增护理等级</li>
     *     <li>ID不为null时：更新已有护理等级</li>
     * </ul>
     *
     * @param level 护理等级对象，包含要保存/更新的护理等级信息
     */
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
    // ------------------------------ 客户护理关系相关方法 ------------------------------

    /**
     * 查询客户护理关联列表（含详情）
     * <p>
     * 通过自定义SQL查询，返回客户与护理等级关联的完整信息
     *
     * @return 客户护理关联集合，包含客户信息、护理等级信息等
     */
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
    // ------------------------------ 护理记录相关方法 ------------------------------

    /**
     * 查询护理执行记录列表（含详情）
     * <p>
     * 通过自定义SQL查询，返回包含完整信息的护理执行记录
     *
     * @return 护理记录集合，包含护理项目、客户、执行人员等信息
     */
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
