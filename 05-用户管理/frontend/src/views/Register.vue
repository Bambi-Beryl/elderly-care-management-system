<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="brand">
        <h1>员工注册</h1>
        <p>注册后默认密码为手机号后 6 位</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="姓名" prop="realName"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" maxlength="11" /></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width:100%">
            <el-option v-for="r in roles" :key="r.roleCode" :label="r.roleName" :value="r.roleCode" />
          </el-select>
        </el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" placeholder="留空则用手机号后6位" show-password /></el-form-item>
        <el-button type="primary" class="submit-btn" :loading="loading" @click="submit">注 册</el-button>
        <div class="links"><router-link to="/login">已有账号？去登录</router-link></div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi, userApi } from '../api'
import { setAuth } from '../utils/auth'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const roles = ref([])
const form = ref({ role: 'nurse' })
const rules = {
  username: [{ required: true, message: '请输入用户名' }],
  realName: [{ required: true, message: '请输入姓名' }],
  phone: [{ required: true, message: '请输入手机号', pattern: /^1\d{10}$/ }],
  role: [{ required: true, message: '请选择角色' }]
}

const submit = async () => {
  await formRef.value?.validate()
  loading.value = true
  try {
    const data = await authApi.register(form.value)
    setAuth(data.token, data)
    ElMessage.success('注册成功，已自动登录')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}

onMounted(async () => { roles.value = await userApi.roles() })
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2d6a4f 0%, #40916c 50%, #b7e4c7 100%);
}
.auth-card {
  width: 440px;
  padding: 32px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15);
}
.brand { text-align: center; margin-bottom: 20px; }
.brand h1 { font-size: 20px; color: #2d6a4f; }
.brand p { color: #666; font-size: 13px; margin-top: 4px; }
.submit-btn { width: 100%; margin-top: 8px; }
.links { text-align: center; margin-top: 14px; }
.links a { color: #409eff; text-decoration: none; font-size: 14px; }
</style>
