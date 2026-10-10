<template>
  <PageCard title="基础数据维护">
    <div class="toolbar">
      <el-button type="success" @click="openDialog()"><el-icon><Plus /></el-icon> 新增员工</el-button>
      <el-button @click="load"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>
    <el-row :gutter="16" class="section">
      <el-col :span="8">
        <el-card shadow="never" header="角色列表">
          <el-table :data="roles" size="small" stripe>
            <el-table-column prop="roleCode" label="编码" />
            <el-table-column prop="roleName" label="名称" />
            <el-table-column prop="description" label="说明" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never" header="系统用户">
          <el-table :data="users" stripe border>
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="realName" label="姓名" />
            <el-table-column prop="roleName" label="角色" width="110" />
            <el-table-column prop="phone" label="手机" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
                <el-button size="small" type="warning" link @click="resetPwd(row.id)">重置密码</el-button>
                <el-button size="small" type="danger" link @click="remove(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="visible" :title="form.id ? '编辑员工' : '新增员工'" width="480px" destroy-on-close>
      <el-form :model="form" label-width="90px" :rules="formRules" ref="formRef">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="姓名" prop="realName"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width:100%">
            <el-option v-for="r in roles" :key="r.roleCode" :label="r.roleName" :value="r.roleCode" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item v-if="!form.id" label="密码">
          <el-input v-model="form.password" placeholder="留空则默认手机号后6位" show-password />
        </el-form-item>
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
import { userApi } from '../../api'

const users = ref([])
const roles = ref([])
const visible = ref(false)
const formRef = ref()
const form = ref({ status: 1, role: 'nurse' })
const formRules = {
  username: [{ required: true, message: '请输入用户名' }],
  realName: [{ required: true, message: '请输入姓名' }],
  phone: [{ required: true, message: '请输入手机号' }],
  role: [{ required: true, message: '请选择角色' }]
}

const load = async () => {
  users.value = await userApi.list()
  roles.value = await userApi.roles()
}
const openDialog = (row) => {
  form.value = row ? { ...row } : { status: 1, role: 'nurse' }
  visible.value = true
}
const save = async () => {
  await formRef.value?.validate()
  await userApi.save(form.value)
  ElMessage.success('保存成功')
  visible.value = false
  load()
}
const resetPwd = async (id) => {
  await ElMessageBox.confirm('将密码重置为手机号后6位，确定？', '提示')
  await userApi.resetPassword(id)
  ElMessage.success('密码已重置')
}
const remove = async (id) => {
  await ElMessageBox.confirm('确定删除该用户？', '提示', { type: 'warning' })
  await userApi.delete(id)
  ElMessage.success('删除成功')
  load()
}
onMounted(load)
</script>

<style scoped>
.toolbar { margin-bottom: 16px; }
.section { margin-top: 8px; }
</style>
