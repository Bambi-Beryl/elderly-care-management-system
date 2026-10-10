<template>
  <div class="dashboard">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div :class="['stat-card', 'click-card', item.bg]" @click="go(item.path)">
          <div class="label">{{ item.label }}</div>
          <div class="num">{{ stats[item.key] ?? '-' }}</div>
          <div class="hint">点击查看详情 →</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:20px">
      <el-col :span="12">
        <PageCard title="快捷入口">
          <div class="quick-grid">
            <div v-for="q in quickLinks" :key="q.path" class="quick-item click-card" @click="$router.push(q.path)">
              <el-icon :size="28"><component :is="q.icon" /></el-icon>
              <span>{{ q.name }}</span>
            </div>
          </div>
        </PageCard>
      </el-col>
      <el-col :span="12">
        <PageCard title="待办提醒">
          <el-empty v-if="!todos.length" description="暂无待办" />
          <div v-else class="todo-list">
            <div v-for="t in todos" :key="t.text" class="todo-item click-card" @click="$router.push(t.path)">
              <el-tag :type="t.type" size="small">{{ t.tag }}</el-tag>
              <span>{{ t.text }}</span>
            </div>
          </div>
        </PageCard>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import PageCard from '../components/PageCard.vue'
import { dashboardApi } from '../api'

const router = useRouter()
const stats = ref({})

const statCards = [
  { label: '在院客户', key: 'inHospital', bg: 'bg-blue', path: '/customer/list' },
  { label: '总床位数', key: 'bedTotal', bg: 'bg-green', path: '/bed/diagram' },
  { label: '空闲床位', key: 'bedFree', bg: 'bg-orange', path: '/bed/manage' },
  { label: '护理记录', key: 'nursingRecords', bg: 'bg-purple', path: '/nursing/records' }
]

const quickLinks = [
  { name: '入住登记', path: '/customer/check-in', icon: 'Plus' },
  { name: '退住审核', path: '/customer/check-out', icon: 'Document' },
  { name: '床位示意图', path: '/bed/diagram', icon: 'House' },
  { name: '日常护理', path: '/health/daily', icon: 'Monitor' },
  { name: '护理项目', path: '/nursing/items', icon: 'FirstAidKit' },
  { name: '服务关注', path: '/health/attention', icon: 'Star' }
]

const todos = computed(() => {
  const list = []
  if (stats.value.pendingCheckOut > 0) list.push({ tag: '退住', type: 'warning', text: `${stats.value.pendingCheckOut} 条退住待审核`, path: '/customer/check-out' })
  if (stats.value.pendingOuting > 0) list.push({ tag: '外出', type: 'info', text: `${stats.value.pendingOuting} 条外出待审核`, path: '/customer/outing' })
  return list
})

const go = (path) => router.push(path)
onMounted(async () => { stats.value = await dashboardApi.stats() })
</script>

<style scoped>
.stat-row { margin-bottom: 4px; }
.hint { font-size: 12px; opacity: 0.8; }
.quick-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; }
.quick-item {
  display: flex; flex-direction: column; align-items: center; gap: 8px;
  padding: 20px; border-radius: 10px; background: #f5f7fa; color: #606266;
}
.quick-item:hover { background: #ecf5ff; color: #409EFF; }
.todo-list { display: flex; flex-direction: column; gap: 10px; }
.todo-item {
  display: flex; align-items: center; gap: 12px;
  padding: 12px 16px; border-radius: 8px; background: #fdf6ec;
}
</style>
