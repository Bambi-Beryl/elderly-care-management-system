<template>
  <PageCard title="提交退住申请" style="margin-bottom:20px">
    <el-form :model="form" inline :rules="rules" ref="formRef">
      <el-form-item label="客户" prop="customerId">
        <el-select v-model="form.customerId" filterable placeholder="选择客户" style="width:180px">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="退住原因" prop="reason">
        <el-input v-model="form.reason" placeholder="请输入退住原因" style="width:280px" />
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="submit"><el-icon><Upload /></el-icon> 提交申请</el-button>
      </el-form-item>
    </el-form>
  </PageCard>
  <PageCard title="退住申请列表">
    <el-table :data="list" stripe border highlight-current-row>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="reason" label="原因" show-overflow-tooltip />
      <el-table-column prop="applyTime" label="申请时间" width="170" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'success' : 'danger'">
            {{ { 0: '待审核', 1: '已通过', 2: '已拒绝' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button type="success" size="small" @click="audit(row.id, 1)">通过</el-button>
            <el-button type="danger" size="small" @click="audit(row.id, 2)">拒绝</el-button>
          </template>
          <span v-else style="color:#999;margin-right:8px">已处理</span>
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
import { customerApi } from '../../api'

const formRef = ref()
const form = ref({})
const list = ref([])
const customers = ref([])
const rules = {
  customerId: [{ required: true, message: '请选择客户' }],
  reason: [{ required: true, message: '请输入原因' }]
}

const load = async () => { list.value = await customerApi.checkOutList() }
const submit = async () => {
  await formRef.value?.validate()
  await customerApi.submitCheckOut(form.value)
  ElMessage.success('提交成功')
  form.value = {}
  load()
}
const audit = async (id, status) => {
  await customerApi.auditCheckOut({ id, status })
  ElMessage.success('审核完成')
  load()
}
const remove = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await customerApi.deleteCheckOut(id)
  ElMessage.success('删除成功')
  load()
}
onMounted(async () => {
  customers.value = await customerApi.list()
  load()
})
</script>
