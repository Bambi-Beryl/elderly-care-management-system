<template>
  <PageCard title="客户护理设置">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增设置</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="list" stripe border highlight-current-row>
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="levelName" label="护理级别" />
      <el-table-column prop="startDate" label="开始日期" />
      <el-table-column prop="endDate" label="结束日期" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status ? 'success' : 'info'">{{ row.status ? '生效' : '失效' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑设置' : '新增设置'" width="480px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="客户">
          <el-select v-model="form.customerId" filterable style="width:100%">
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理级别">
          <el-select v-model="form.levelId" style="width:100%">
            <el-option v-for="l in levelList" :key="l.id" :label="l.levelName" :value="l.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
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
import { nursingApi, customerApi } from '../../api'

const list = ref([])
const customers = ref([])
const levelList = ref([])
const visible = ref(false)
const form = ref({ status: 1 })

const load = async () => { list.value = await nursingApi.customerSettings() }
const openDialog = (row) => { form.value = row ? { ...row } : { status: 1 }; visible.value = true }
const save = async () => { await nursingApi.saveCustomerSetting(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await nursingApi.deleteCustomerSetting(id); ElMessage.success('删除成功'); load() }
onMounted(async () => {
  customers.value = await customerApi.list()
  levelList.value = await nursingApi.levels()
  load()
})
</script>
