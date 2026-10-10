<template>
  <PageCard title="客户列表">
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索客户姓名" style="width:200px" clearable @keyup.enter="load" @clear="load">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="load"><el-icon><Search /></el-icon> 搜索</el-button>
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增客户</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="pagedList" stripe border highlight-current-row @row-click="onRowClick">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" width="60">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="60" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="bedNo" label="床位" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click.stop="openDialog(row)">编辑</el-button>
          <el-button size="small" type="info" link @click.stop="viewDetail(row)">详情</el-button>
          <el-button size="small" type="danger" link @click.stop="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="pager" background layout="total, prev, pager, next" :total="list.length" :page-size="pageSize" v-model:current-page="page" />

    <el-dialog v-model="visible" :title="form.id ? '编辑客户' : '新增客户'" width="560px" destroy-on-close>
      <el-form :model="form" label-width="90px" :rules="rules" ref="formRef">
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别"><el-radio-group v-model="form.gender"><el-radio :value="1">男</el-radio><el-radio :value="2">女</el-radio></el-radio-group></el-form-item>
        <el-form-item label="年龄" prop="age"><el-input-number v-model="form.age" :min="1" :max="120" /></el-form-item>
        <el-form-item label="身份证"><el-input v-model="form.idCard" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="紧急联系人"><el-input v-model="form.emergencyContact" /></el-form-item>
        <el-form-item label="紧急电话"><el-input v-model="form.emergencyPhone" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" type="textarea" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status"><el-option :value="1" label="在院" /><el-option :value="2" label="外出" /><el-option :value="3" label="退住" /></el-select></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="drawer" title="客户详情" size="400px">
      <el-descriptions :column="1" border v-if="current">
        <el-descriptions-item label="姓名">{{ current.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ current.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ current.age }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ current.phone }}</el-descriptions-item>
        <el-descriptions-item label="床位">{{ current.bedNo || '未分配' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ current.address }}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </PageCard>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { customerApi } from '../../api'

const list = ref([])
const keyword = ref('')
const visible = ref(false)
const drawer = ref(false)
const current = ref(null)
const page = ref(1)
const pageSize = 10
const formRef = ref()
const form = ref({ gender: 1, age: 70, status: 1 })
const rules = { name: [{ required: true, message: '请输入姓名' }], age: [{ required: true, message: '请输入年龄' }] }

const pagedList = computed(() => list.value.slice((page.value - 1) * pageSize, page.value * pageSize))
const statusText = (s) => ({ 1: '在院', 2: '外出', 3: '退住' }[s] || '未知')
const statusType = (s) => ({ 1: 'success', 2: 'warning', 3: 'info' }[s] || '')

const load = async () => { list.value = await customerApi.list(keyword.value || undefined); page.value = 1 }
const openDialog = (row) => { form.value = row ? { ...row } : { gender: 1, age: 70, status: 1 }; visible.value = true }
const viewDetail = (row) => { current.value = row; drawer.value = true }
const onRowClick = (row) => { current.value = row }
const save = async () => {
  await formRef.value?.validate()
  await customerApi.save(form.value)
  ElMessage.success('保存成功')
  visible.value = false
  load()
}
const remove = async (id) => {
  await ElMessageBox.confirm('确定删除该客户？', '提示', { type: 'warning' })
  await customerApi.delete(id)
  ElMessage.success('删除成功')
  load()
}
onMounted(load)
</script>

<style scoped>
.pager { margin-top: 16px; justify-content: flex-end; }
</style>
