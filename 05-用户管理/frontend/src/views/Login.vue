<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="brand">
        <el-icon :size="36"><HomeFilled /></el-icon>
        <h1>东软颐养中心</h1>
        <p>智慧养老管理系统</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="submit">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password prefix-icon="Lock" />
        </el-form-item>
        <el-button type="primary" size="large" class="submit-btn" :loading="loading" @click="submit">登 录</el-button>
        <div class="links">
          <router-link to="/register">没有账号？立即注册</router-link>
        </div>
        <p class="hint">演示账号：admin / 000001（密码为手机号后6位）</p>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../api'
import { setAuth } from '../utils/auth'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = ref({ username: 'admin', password: '000001' })
const rules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }]
}

const submit = async () => {
  await formRef.value?.validate()
  loading.value = true
  try {
    const data = await authApi.login(form.value)
    setAuth(data.token, data)
    ElMessage.success(`欢迎，${data.realName}`)
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1d3557 0%, #457b9d 50%, #a8dadc 100%);
}
.auth-card {
  width: 400px;
  padding: 40px 36px;
  background: rgba(255, 255, 255, 0.96);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}
.brand { text-align: center; margin-bottom: 28px; color: #1d3557; }
.brand h1 { font-size: 22px; margin: 8px 0 4px; }
.brand p { color: #666; font-size: 13px; }
.submit-btn { width: 100%; margin-top: 8px; }
.links { text-align: center; margin-top: 16px; }
.links a { color: #409eff; font-size: 14px; text-decoration: none; }
.hint { margin-top: 16px; font-size: 12px; color: #909399; text-align: center; }
</style>
