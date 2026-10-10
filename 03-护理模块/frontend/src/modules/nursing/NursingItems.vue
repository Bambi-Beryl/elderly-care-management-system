<template>
  <PageCard title="护理项目库">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增项目</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-table :data="items" stripe border highlight-current-row>
      <el-table-column prop="itemCode" label="编码" width="100" />
      <el-table-column prop="itemName" label="项目名称" />
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column prop="unit" label="单位" width="60" />
      <el-table-column prop="price" label="价格(元)" width="90" />
      <el-table-column prop="cycleDesc" label="执行周期" />
      <el-table-column prop="description" label="说明" show-overflow-tooltip />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑项目' : '新增项目'" width="520px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="编码"><el-input v-model="form.itemCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.itemName" /></el-form-item>
        <el-form-item label="类别">
          <el-select v-model="form.category" style="width:100%">
            <el-option value="生活照料" /><el-option value="医疗护理" /><el-option value="康复心理" />
          </el-select>
        </el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="周期"><el-input v-model="form.cycleDesc" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" type="textarea" /></el-form-item>
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

const items = ref([])
const visible = ref(false)
const form = ref({ unit: '次', category: '生活照料' })

const load = async () => { items.value = await nursingApi.items() }
const openDialog = (row) => { form.value = row ? { ...row } : { unit: '次', category: '生活照料', price: 10 }; visible.value = true }
const save = async () => { await nursingApi.saveItem(form.value); ElMessage.success('保存成功'); visible.value = false; load() }
const remove = async (id) => { await ElMessageBox.confirm('确定删除？'); await nursingApi.deleteItem(id); ElMessage.success('删除成功'); load() }
onMounted(load)
</script>
