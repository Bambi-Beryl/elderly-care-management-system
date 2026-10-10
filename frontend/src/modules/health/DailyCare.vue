<template>
  <PageCard title="日常护理">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增记录</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="list" stripe border highlight-current-row @row-click="onRowClick">
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="stewardName" label="管家" />
      <el-table-column prop="careDate" label="日期" width="110" />
      <el-table-column prop="bloodPressure" label="血压" width="90" />
      <el-table-column prop="heartRate" label="心率" width="60" />
      <el-table-column prop="bloodSugar" label="血糖" width="60" />
      <el-table-column prop="weight" label="体重" width="60" />
      <el-table-column prop="mood" label="情绪" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click.stop="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click.stop="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer v-model="drawer" title="护理详情" size="420px">
      <el-descriptions v-if="current" :column="1" border>
        <el-descriptions-item label="客户">{{ current.customerName }}</el-descriptions-item>
        <el-descriptions-item label="管家">{{ current.stewardName }}</el-descriptions-item>
        <el-descriptions-item label="血压">{{ current.bloodPressure }}</el-descriptions-item>
        <el-descriptions-item label="心率">{{ current.heartRate }}</el-descriptions-item>
        <el-descriptions-item label="血糖">{{ current.bloodSugar }}</el-descriptions-item>
        <el-descriptions-item label="体重">{{ current.weight }}</el-descriptions-item>
        <el-descriptions-item label="体温">{{ current.temperature }}</el-descriptions-item>
        <el-descriptions-item label="饮食">{{ current.diet }}</el-descriptions-item>
        <el-descriptions-item label="睡眠">{{ current.sleep }}</el-descriptions-item>
        <el-descriptions-item label="活动">{{ current.activity }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ current.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>

    <el-dialog v-model="visible" :title="form.id ? '编辑记录' : '新增记录'" width="640px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="客户">
              <el-select v-model="form.customerId" filterable style="width:100%">
                <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管家">
              <el-select v-model="form.stewardId" filterable style="width:100%">
                <el-option v-for="s in stewards" :key="s.id" :label="s.name" :value="s.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="日期"><el-date-picker v-model="form.careDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="血压"><el-input v-model="form.bloodPressure" placeholder="120/80" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="心率"><el-input-number v-model="form.heartRate" :min="40" :max="200" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="血糖"><el-input-number v-model="form.bloodSugar" :min="0" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="体重"><el-input-number v-model="form.weight" :min="0" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="体温"><el-input-number v-model="form.temperature" :min="35" :max="42" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="情绪"><el-input v-model="form.mood" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="饮食"><el-input v-model="form.diet" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="睡眠"><el-input v-model="form.sleep" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="活动"><el-input v-model="form.activity" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </PageCard>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { healthApi, customerApi } from '../../api'

const list = ref([])
const customers = ref([])
const stewards = ref([])
const visible = ref(false)
const drawer = ref(false)
const current = ref(null)
const form = ref({ heartRate: 72 })

const load = async () => { list.value = await healthApi.dailyCare() }
const openDialog = (row) => { form.value = row ? { ...row } : { heartRate: 72 }; visible.value = true }
const onRowClick = (row) => { current.value = row; drawer.value = true }
const save = async () => { await healthApi.saveDailyCare(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await healthApi.deleteDailyCare(id); ElMessage.success('删除成功'); load() }

onMounted(async () => {
  customers.value = await customerApi.list()
  stewards.value = await healthApi.stewards()
  load()
})
</script>
