<template>
  <PageCard title="入住登记">
    <el-alert type="info" show-icon :closable="false" class="tip">
      登记客户入住信息：根据出生日期自动计算年龄；合同到期时间不能早于入住时间；房间按楼层分组选择；选择房间后仅显示该房间空闲床位。
    </el-alert>
    <el-form :model="form" label-width="110px" style="max-width:720px" :rules="rules" ref="formRef">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="客户姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="form.gender"><el-radio :value="1">男</el-radio><el-radio :value="2">女</el-radio></el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" style="width:100%" @change="calcAge" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄">
            <el-input-number v-model="form.age" :min="0" :max="120" disabled style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard"><el-input v-model="form.idCard" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="血型" prop="bloodType">
            <el-select v-model="form.bloodType" placeholder="选择血型" style="width:100%">
              <el-option v-for="t in ['A','B','AB','O']" :key="t" :label="t" :value="t" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="家属" prop="familyMember"><el-input v-model="form.familyMember" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="楼栋"><el-input v-model="form.building" placeholder="如：1号楼" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="房间号" prop="roomId">
            <el-select v-model="form.roomId" placeholder="按楼层选择房间" style="width:100%" @change="onRoomChange">
              <el-option-group v-for="g in roomGroups" :key="g.floor" :label="g.label">
                <el-option v-for="r in g.rooms" :key="r.id" :label="`${r.roomNo}（${r.roomType}）`" :value="r.id" />
              </el-option-group>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="床位号" prop="bedId">
            <el-select v-model="form.bedId" placeholder="请先选择房间" style="width:100%" :disabled="!form.roomId">
              <el-option v-for="b in vacantBeds" :key="b.id" :label="b.bedNo" :value="b.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入住时间" prop="checkInDate">
            <el-date-picker v-model="form.checkInDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同到期" prop="contractExpireDate">
            <el-date-picker v-model="form.contractExpireDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号"><el-input v-model="form.contractNo" /></el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="押金"><el-input-number v-model="form.deposit" :min="0" :precision="2" style="width:100%" /></el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="success" @click="submit"><el-icon><Check /></el-icon> 提交入住登记</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <div class="toolbar">
      <span class="sub-title">入住记录</span>
      <el-button size="small" @click="loadRecords"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="records" stripe border>
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="bedNo" label="床位" />
      <el-table-column prop="checkInDate" label="入住日期" />
      <el-table-column prop="contractExpireDate" label="合同到期" />
      <el-table-column prop="contractNo" label="合同编号" />
      <el-table-column prop="operator" label="操作员" />
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" type="danger" link @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { customerApi, bedApi } from '../../api'
import { getUser } from '../../utils/auth'

const formRef = ref()
const form = ref({ gender: 1, deposit: 5000, operator: '管理员' })
const records = ref([])
const roomGroups = ref([])
const vacantBeds = ref([])

const validateExpire = (_, value, callback) => {
  if (value && form.value.checkInDate && value < form.value.checkInDate) {
    callback(new Error('合同到期时间不能早于入住时间'))
  } else {
    callback()
  }
}

const rules = {
  name: [{ required: true, message: '请输入客户姓名' }],
  gender: [{ required: true }],
  birthDate: [{ required: true, message: '请选择出生日期' }],
  bloodType: [{ required: true, message: '请选择血型' }],
  idCard: [{ required: true, message: '请输入身份证号' }],
  familyMember: [{ required: true, message: '请输入家属' }],
  phone: [{ required: true, message: '请输入联系电话' }],
  roomId: [{ required: true, message: '请选择房间' }],
  bedId: [{ required: true, message: '请选择床位' }],
  checkInDate: [{ required: true, message: '请选择入住时间' }],
  contractExpireDate: [{ required: true, message: '请选择合同到期时间' }, { validator: validateExpire, trigger: 'change' }]
}

const calcAge = () => {
  if (!form.value.birthDate) return
  const birth = new Date(form.value.birthDate)
  const today = new Date()
  let age = today.getFullYear() - birth.getFullYear()
  const m = today.getMonth() - birth.getMonth()
  if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) age--
  form.value.age = age
}

const onRoomChange = async (roomId) => {
  form.value.bedId = null
  vacantBeds.value = roomId ? await bedApi.vacant(roomId) : []
  const group = roomGroups.value.find((g) => g.rooms?.some((r) => r.id === roomId))
  if (group && !form.value.building) {
    form.value.building = `${group.floor}号楼`
  }
}

const loadRooms = async () => { roomGroups.value = await bedApi.roomsGrouped() }
const loadRecords = async () => { records.value = await customerApi.checkInList() }

const resetForm = () => {
  const user = getUser()
  form.value = {
    gender: 1,
    deposit: 5000,
    operator: user?.realName || '管理员'
  }
  vacantBeds.value = []
}

const submit = async () => {
  await formRef.value?.validate()
  await customerApi.registerCheckIn(form.value)
  ElMessage.success('入住登记成功')
  resetForm()
  loadRecords()
  loadRooms()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确定删除该记录？', '提示', { type: 'warning' })
  await customerApi.deleteCheckIn(id)
  ElMessage.success('删除成功')
  loadRecords()
}

onMounted(() => {
  const user = getUser()
  form.value.operator = user?.realName || '管理员'
  loadRooms()
  loadRecords()
})
</script>

<style scoped>
.tip { margin-bottom: 16px; }
.sub-title { font-weight: 600; color: #303133; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
</style>
