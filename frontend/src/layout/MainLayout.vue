<template>
  <el-container class="layout">
    <el-aside width="230px" class="aside">
      <div class="logo click-card" @click="$router.push('/dashboard')">
        <el-icon><HomeFilled /></el-icon> 东软颐养中心
      </div>
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <el-menu-item index="/dashboard"><el-icon><Odometer /></el-icon><span>系统首页</span></el-menu-item>
        <el-sub-menu index="customer">
          <template #title><el-icon><User /></el-icon><span>客户管理</span></template>
          <el-menu-item index="/customer/list">客户列表</el-menu-item>
          <el-menu-item index="/customer/check-in">入住登记</el-menu-item>
          <el-menu-item index="/customer/check-out">退住登记</el-menu-item>
          <el-menu-item index="/customer/outing">外出登记</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="bed">
          <template #title><el-icon><House /></el-icon><span>床位管理</span></template>
          <el-menu-item index="/bed/diagram">床位示意图</el-menu-item>
          <el-menu-item index="/bed/manage">床位管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="nursing">
          <template #title><el-icon><FirstAidKit /></el-icon><span>护理模块</span></template>
          <el-menu-item index="/nursing/items">护理项目</el-menu-item>
          <el-menu-item index="/nursing/levels">护理级别</el-menu-item>
          <el-menu-item index="/nursing/settings">客户护理设置</el-menu-item>
          <el-menu-item index="/nursing/records">护理记录</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="health">
          <template #title><el-icon><Monitor /></el-icon><span>健康管家</span></template>
          <el-menu-item index="/health/stewards">设置服务对象</el-menu-item>
          <el-menu-item index="/health/attention">服务关注</el-menu-item>
          <el-menu-item index="/health/daily">日常护理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user">
          <template #title><el-icon><Setting /></el-icon><span>用户管理</span></template>
          <el-menu-item index="/user/manage">基础数据维护</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="$route.meta.module">{{ $route.meta.module }}</el-breadcrumb-item>
          <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="header-right">
          <el-avatar :size="32" style="background:#409EFF">{{ avatarText }}</el-avatar>
          <span class="user">{{ displayName }}</span>
          <el-button type="danger" link @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <transition name="page-fade" mode="out-in">
            <!-- 必须单层包裹，否则多根节点页面在 transition 下会空白 -->
            <div v-if="Component" :key="$route.fullPath" class="page-view">
              <component :is="Component" />
            </div>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { getUser, clearAuth } from '../utils/auth'
import { authApi } from '../api'

const router = useRouter()
const loginUser = computed(() => getUser())
const displayName = computed(() => loginUser.value?.realName || loginUser.value?.username || '用户')
const avatarText = computed(() => (displayName.value || '用').charAt(0))

const logout = async () => {
  await ElMessageBox.confirm('确定退出登录？', '提示')
  try { await authApi.logout() } catch { /* ignore */ }
  clearAuth()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #304156; overflow-y: auto; }
.logo {
  height: 60px; display: flex; align-items: center; justify-content: center; gap: 8px;
  color: #fff; font-size: 15px; font-weight: bold; background: #263445; cursor: pointer;
}
.header {
  display: flex; align-items: center; justify-content: space-between;
  background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.08); height: 56px;
}
.header-right { display: flex; align-items: center; gap: 10px; }
.main { padding: 20px; background: #f0f2f5; overflow-y: auto; }
.page-view { width: 100%; min-height: 200px; }
.user { color: #666; font-size: 14px; }
</style>
