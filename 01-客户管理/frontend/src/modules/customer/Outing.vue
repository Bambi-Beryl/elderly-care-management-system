<template>
  <PageCard title="提交外出申请" style="margin-bottom:20px">
    <el-form :model="form" inline :rules="rules" ref="formRef">
      <el-form-item label="客户" prop="customerId">
        <el-select v-model="form.customerId" filterable style="width:160px">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="原因" prop="reason"><el-input v-model="form.reason" style="width:160px" /></el-form-item>
      <el-form-item label="外出日期" prop="outingDate">
        <el-date-picker v-model="form.outingDate" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item label="返回日期">
        <el-date-picker v-model="form.returnDate" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item><el-button type="success" @click="submit">提交申请</el-button></el-form-item>
    </el-form>
  </PageCard>
  <PageCard title="外出申请列表">
    <el-table :data="list" stripe border highlight-current-row>
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="reason" label="原因" />
      <el-table-column prop="outingDate" label="外出日期" width="110" />
      <el-table-column prop="returnDate" label="返回日期" width="110" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ { 0: '待审核', 1: '已通过', 2: '已拒绝', 3: '已返回' }[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button type="success" size="small" @click="audit(row.id, 1)">通过</el-button>
            <el-button type="danger" size="small" @click="audit(row.id, 2)">拒绝</el-button>
          </template>
          <el-button v-if="row.status === 1" type="primary" size="small" @click="audit(row.id, 3)">确认返回</el-button>
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
  reason: [{ required: true, message: '请输入原因' }],
  outingDate: [{ required: true, message: '请选择日期' }]
}

const load = async () => { list.value = await customerApi.outingList() }
const submit = async () => {
  await formRef.value?.validate()
  await customerApi.submitOuting(form.value)
  ElMessage.success('提交成功')
  form.value = {}
  load()
}
const audit = async (id, status) => {
  await customerApi.auditOuting({ id, status })
  ElMessage.success('操作成功')
  load()
}
const remove = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await customerApi.deleteOuting(id)
  ElMessage.success('删除成功')
  load()
}
onMounted(async () => {
  customers.value = await customerApi.list()
  load()
})
</script>
