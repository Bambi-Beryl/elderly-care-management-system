<template>
  <PageCard title="服务关注">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增服务</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table v-loading="loading" :data="list" stripe border highlight-current-row empty-text="暂无数据，请确认后端已启动且数据库已导入">
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="serviceName" label="服务名称" />
      <el-table-column prop="serviceType" label="类型" />
      <el-table-column prop="price" label="价格" width="90" />
      <el-table-column prop="startDate" label="开始日期" width="110" />
      <el-table-column prop="endDate" label="结束日期" width="110" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status ? 'success' : 'info'">{{ row.status ? '进行中' : '已结束' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑服务' : '新增服务'" width="520px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="客户">
          <el-select v-model="form.customerId" filterable style="width:100%">
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务名称"><el-input v-model="form.serviceName" /></el-form-item>
        <el-form-item label="服务类型"><el-input v-model="form.serviceType" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
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
const visible = ref(false)
const loading = ref(false)
const form = ref({ status: 1 })

const load = async () => {
  loading.value = true
  try {
    list.value = await healthApi.serviceAttention() || []
  } finally {
    loading.value = false
  }
}
const openDialog = (row) => { form.value = row ? { ...row } : { status: 1 }; visible.value = true }
const save = async () => { await healthApi.saveServiceAttention(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await healthApi.deleteServiceAttention(id); ElMessage.success('删除成功'); load() }
onMounted(async () => {
  try { customers.value = await customerApi.list() || [] } catch { customers.value = [] }
  load()
})
</script>
