<template>
  <PageCard title="健康管家管理" style="margin-bottom:20px">
    <div class="toolbar">
      <el-button type="success" @click="openStewardDialog()"><el-icon><Plus /></el-icon> 新增管家</el-button>
    </div>
    <el-table :data="stewards" stripe border size="small">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" width="60">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="department" label="部门" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openStewardDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="removeSteward(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>

  <PageCard title="服务对象分配">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 分配管家</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="list" stripe border highlight-current-row>
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="stewardName" label="健康管家" />
      <el-table-column prop="assignDate" label="分配日期" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status ? 'success' : 'info'">{{ row.status ? '服务中' : '已结束' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>

  <el-dialog v-model="stewardVisible" :title="stewardForm.id ? '编辑管家' : '新增管家'" width="480px" destroy-on-close>
    <el-form :model="stewardForm" label-width="80px">
      <el-form-item label="姓名"><el-input v-model="stewardForm.name" /></el-form-item>
      <el-form-item label="性别"><el-radio-group v-model="stewardForm.gender"><el-radio :value="1">男</el-radio><el-radio :value="2">女</el-radio></el-radio-group></el-form-item>
      <el-form-item label="电话"><el-input v-model="stewardForm.phone" /></el-form-item>
      <el-form-item label="部门"><el-input v-model="stewardForm.department" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="stewardVisible = false">取消</el-button>
      <el-button type="primary" @click="saveSteward">保存</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="visible" :title="form.id ? '编辑分配' : '分配管家'" width="480px" destroy-on-close>
    <el-form :model="form" label-width="90px">
      <el-form-item label="客户">
        <el-select v-model="form.customerId" filterable style="width:100%">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="管家">
        <el-select v-model="form.stewardId" filterable style="width:100%">
          <el-option v-for="s in stewards" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="分配日期"><el-date-picker v-model="form.assignDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
      <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { healthApi, customerApi } from '../../api'

const list = ref([])
const stewards = ref([])
const customers = ref([])
const visible = ref(false)
const stewardVisible = ref(false)
const form = ref({ status: 1 })
const stewardForm = ref({ gender: 1, department: '健康管家部', status: 1 })

const load = async () => { list.value = await healthApi.customerStewards() }
const loadStewards = async () => { stewards.value = await healthApi.stewards() }
const openDialog = (row) => { form.value = row ? { ...row } : { status: 1 }; visible.value = true }
const openStewardDialog = (row) => { stewardForm.value = row ? { ...row } : { gender: 1, department: '健康管家部', status: 1 }; stewardVisible.value = true }
const save = async () => { await healthApi.saveCustomerSteward(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const saveSteward = async () => { await healthApi.saveSteward(stewardForm.value); ElMessage.success('保存成功'); stewardVisible.value = false; loadStewards() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await healthApi.deleteCustomerSteward(id); ElMessage.success('删除成功'); load() }
const removeSteward = async (id) => { await ElMessageBox.confirm('确定删除？'); await healthApi.deleteSteward(id); ElMessage.success('删除成功'); loadStewards() }

onMounted(async () => {
  customers.value = await customerApi.list()
  loadStewards()
  load()
})
</script>
