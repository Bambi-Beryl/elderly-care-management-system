<template>
  <PageCard title="护理级别体系">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增级别</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="levels" stripe border row-key="id">
      <el-table-column type="expand">
        <template #default="{ row }">
          <el-tag v-for="item in row.items" :key="item.id" style="margin:4px" type="info">{{ item.itemName }}</el-tag>
          <span v-if="!row.items?.length" style="color:#999">暂无关联项目</span>
        </template>
      </el-table-column>
      <el-table-column prop="levelCode" label="编码" width="80" />
      <el-table-column prop="levelName" label="级别名称" />
      <el-table-column prop="description" label="适用对象" show-overflow-tooltip />
      <el-table-column prop="frequency" label="护理频次" />
      <el-table-column prop="price" label="费用(元/月)" width="110" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑级别' : '新增级别'" width="520px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="编码"><el-input v-model="form.levelCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.levelName" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="频次"><el-input v-model="form.frequency" /></el-form-item>
        <el-form-item label="费用"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
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
import { nursingApi } from '../../api'

const levels = ref([])
const visible = ref(false)
const form = ref({})

const load = async () => { levels.value = await nursingApi.levels() }
const openDialog = (row) => { form.value = row ? { ...row, items: undefined } : { price: 1000 }; visible.value = true }
const save = async () => { await nursingApi.saveLevel(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await nursingApi.deleteLevel(id); ElMessage.success('删除成功'); load() }
onMounted(load)
</script>
