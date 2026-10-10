<template>
  <PageCard title="护理记录">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增记录</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="records" stripe border highlight-current-row>
      <el-table-column prop="customerName" label="客户" />
      <el-table-column prop="itemName" label="护理项目" />
      <el-table-column prop="nursingTime" label="护理时间" width="170" />
      <el-table-column prop="staffName" label="执行人" />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }"><el-tag :type="row.status ? 'success' : 'warning'">{{ row.status ? '已完成' : '未完成' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑记录' : '新增记录'" width="520px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="客户">
          <el-select v-model="form.customerId" filterable style="width:100%">
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理项目">
          <el-select v-model="form.itemId" filterable style="width:100%">
            <el-option v-for="i in items" :key="i.id" :label="i.itemName" :value="i.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理时间"><el-date-picker v-model="form.nursingTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="执行人"><el-input v-model="form.staffName" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="已完成" inactive-text="未完成" /></el-form-item>
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
import { nursingApi, customerApi } from '../../api'

const records = ref([])
const customers = ref([])
const items = ref([])
const visible = ref(false)
const form = ref({ staffName: '刘护士', status: 1 })

const load = async () => { records.value = await nursingApi.records() }
const openDialog = (row) => { form.value = row ? { ...row } : { staffName: '刘护士', status: 1 }; visible.value = true }
const save = async () => { await nursingApi.saveRecord(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await nursingApi.deleteRecord(id); ElMessage.success('删除成功'); load() }
onMounted(async () => {
  customers.value = await customerApi.list()
  items.value = await nursingApi.items()
  load()
})
</script>
