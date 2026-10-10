<template>
  <PageCard title="床位管理">
    <div class="toolbar">
      <el-button type="success" @click="openBedDialog()"><el-icon><Plus /></el-icon> 新增床位</el-button>
      <el-button type="primary" @click="openRoomDialog()"><el-icon><Plus /></el-icon> 新增房间</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="beds" stripe border highlight-current-row>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="roomNo" label="房间" />
      <el-table-column prop="bedNo" label="床位号" width="80" />
      <el-table-column prop="floor" label="楼层" width="60" />
      <el-table-column prop="roomType" label="类型" />
      <el-table-column prop="customerName" label="入住客户" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : row.status === 1 ? 'warning' : 'danger'">
            {{ { 0: '空闲', 1: '占用', 2: '维护' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openBedDialog(row)">编辑</el-button>
          <el-dropdown trigger="click" @command="cmd => updateStatus(row.id, cmd)">
            <el-button size="small" link type="warning">改状态</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :command="0">空闲</el-dropdown-item>
                <el-dropdown-item :command="1">占用</el-dropdown-item>
                <el-dropdown-item :command="2">维护</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button size="small" link type="danger" @click="removeBed(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>

  <PageCard title="床位调换" style="margin-top:20px">
    <el-form :model="exForm" inline :rules="exRules" ref="exFormRef">
      <el-form-item label="客户" prop="customerId">
        <el-select v-model="exForm.customerId" filterable style="width:140px">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="原床位" prop="fromBedId">
        <el-select v-model="exForm.fromBedId" filterable style="width:120px">
          <el-option v-for="b in occupiedBeds" :key="b.id" :label="`${b.roomNo}-${b.bedNo}`" :value="b.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="目标床位" prop="toBedId">
        <el-select v-model="exForm.toBedId" filterable style="width:120px">
          <el-option v-for="b in freeBeds" :key="b.id" :label="`${b.roomNo}-${b.bedNo}`" :value="b.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="原因"><el-input v-model="exForm.reason" style="width:140px" /></el-form-item>
      <el-form-item><el-button type="primary" @click="exchange">调换</el-button></el-form-item>
    </el-form>
    <el-table :data="exchanges" stripe border size="small">
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="fromBedNo" label="原床位" />
      <el-table-column prop="toBedNo" label="新床位" />
      <el-table-column prop="reason" label="原因" />
      <el-table-column prop="exchangeTime" label="时间" width="170" />
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" type="danger" link @click="removeEx(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>

  <el-dialog v-model="bedVisible" :title="bedForm.id ? '编辑床位' : '新增床位'" width="480px" destroy-on-close>
    <el-form :model="bedForm" label-width="80px">
      <el-form-item label="房间">
        <el-select v-model="bedForm.roomId" style="width:100%">
          <el-option v-for="r in rooms" :key="r.id" :label="`${r.roomNo} (${r.roomType})`" :value="r.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="床位号"><el-input v-model="bedForm.bedNo" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="bedForm.status"><el-option :value="0" label="空闲" /><el-option :value="1" label="占用" /><el-option :value="2" label="维护" /></el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="bedVisible = false">取消</el-button>
      <el-button type="primary" @click="saveBed">保存</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="roomVisible" title="新增房间" width="480px" destroy-on-close>
    <el-form :model="roomForm" label-width="80px">
      <el-form-item label="房间号"><el-input v-model="roomForm.roomNo" /></el-form-item>
      <el-form-item label="楼层"><el-input-number v-model="roomForm.floor" :min="1" /></el-form-item>
      <el-form-item label="类型"><el-input v-model="roomForm.roomType" placeholder="单人间/双人间/VIP" /></el-form-item>
      <el-form-item label="床位数"><el-input-number v-model="roomForm.bedCount" :min="1" /></el-form-item>
      <el-form-item label="价格"><el-input-number v-model="roomForm.price" :min="0" :precision="2" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="roomVisible = false">取消</el-button>
      <el-button type="primary" @click="saveRoom">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { bedApi, customerApi } from '../../api'

const beds = ref([])
const rooms = ref([])
const exchanges = ref([])
const customers = ref([])
const bedVisible = ref(false)
const roomVisible = ref(false)
const bedForm = ref({ status: 0 })
const roomForm = ref({ floor: 1, bedCount: 1, price: 3000 })
const exFormRef = ref()
const exForm = ref({ operator: '管理员' })
const exRules = { customerId: [{ required: true }], fromBedId: [{ required: true }], toBedId: [{ required: true }] }

const freeBeds = computed(() => beds.value.filter(b => b.status === 0))
const occupiedBeds = computed(() => beds.value.filter(b => b.status === 1))

const load = async () => {
  beds.value = await bedApi.list()
  rooms.value = await bedApi.rooms()
  exchanges.value = await bedApi.exchangeList()
}
const openBedDialog = (row) => { bedForm.value = row ? { ...row } : { status: 0 }; bedVisible.value = true }
const openRoomDialog = () => { roomForm.value = { floor: 1, bedCount: 1, price: 3000 }; roomVisible.value = true }
const saveBed = async () => { await bedApi.save(bedForm.value); ElMessage.success('保存成功'); bedVisible.value = false; load() }
const saveRoom = async () => { await bedApi.saveRoom(roomForm.value); ElMessage.success('保存成功'); roomVisible.value = false; load() }
const removeBed = async (id) => { await ElMessageBox.confirm('确定删除？'); await bedApi.delete(id); ElMessage.success('删除成功'); load() }
const updateStatus = async (id, status) => { await bedApi.updateStatus(id, status); ElMessage.success('状态已更新'); load() }
const exchange = async () => {
  await exFormRef.value?.validate()
  await bedApi.exchange(exForm.value)
  ElMessage.success('调换成功')
  exForm.value = { operator: '管理员' }
  load()
}
const removeEx = async (id) => { await ElMessageBox.confirm('确定删除？'); await bedApi.deleteExchange(id); ElMessage.success('删除成功'); load() }

onMounted(async () => {
  customers.value = await customerApi.list()
  load()
})
</script>
