<template>
  <el-row :gutter="16" style="margin-bottom:20px">
    <el-col :span="6" v-for="s in stats" :key="s.floor">
      <div class="stat-card bg-blue click-card" @click="scrollToFloor(s.floor)">
        <div class="label">{{ s.floor }}楼</div>
        <div class="num">{{ s.occupied }}/{{ s.total }}</div>
        <div class="hint">占用/总数 · 空闲 {{ s.free }}</div>
      </div>
    </el-col>
  </el-row>
  <PageCard title="床位示意图">
    <div v-for="floor in floors" :key="floor" class="floor-block" :ref="el => floorRefs[floor] = el">
      <h4>{{ floor }}楼</h4>
      <div class="bed-grid">
        <div v-for="bed in bedsByFloor(floor)" :key="bed.id"
             :class="['bed-item', 'click-card', statusClass(bed.status)]"
             @click="showBed(bed)">
          <div class="bed-no">{{ bed.roomNo }}-{{ bed.bedNo }}</div>
          <div class="bed-type">{{ bed.roomType }}</div>
          <div class="bed-user">{{ bed.customerName || '空闲' }}</div>
        </div>
      </div>
    </div>
  </PageCard>
  <el-dialog v-model="dialog" :title="selected ? `${selected.roomNo}-${selected.bedNo} 详情` : ''" width="400px">
    <el-descriptions v-if="selected" :column="1" border>
      <el-descriptions-item label="房间">{{ selected.roomNo }}</el-descriptions-item>
      <el-descriptions-item label="类型">{{ selected.roomType }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ statusText(selected.status) }}</el-descriptions-item>
      <el-descriptions-item label="入住客户">{{ selected.customerName || '无' }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button @click="dialog = false">关闭</el-button>
      <el-button type="primary" @click="$router.push('/bed/manage')">去管理</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import PageCard from '../../components/PageCard.vue'
import { bedApi } from '../../api'

const beds = ref([])
const stats = ref([])
const dialog = ref(false)
const selected = ref(null)
const floorRefs = ref({})

const floors = computed(() => [...new Set(beds.value.map(b => b.floor))].sort())
const bedsByFloor = (floor) => beds.value.filter(b => b.floor === floor)
const statusClass = (s) => ({ 0: 'free', 1: 'occupied', 2: 'maintain' }[s] || 'free')
const statusText = (s) => ({ 0: '空闲', 1: '占用', 2: '维护' }[s] || '')
const showBed = (bed) => { selected.value = bed; dialog.value = true }
const scrollToFloor = (floor) => floorRefs.value[floor]?.scrollIntoView({ behavior: 'smooth' })

onMounted(async () => {
  beds.value = await bedApi.list()
  stats.value = await bedApi.stats()
})
</script>

<style scoped>
.floor-block { margin-bottom: 24px; }
.bed-grid { display: flex; flex-wrap: wrap; gap: 12px; }
.bed-item { width: 120px; padding: 12px; border-radius: 10px; text-align: center; border: 2px solid; }
.bed-item.free { background: #f0f9eb; border-color: #67c23a; }
.bed-item.occupied { background: #fdf6ec; border-color: #e6a23c; }
.bed-item.maintain { background: #fef0f0; border-color: #f56c6c; }
.bed-no { font-weight: bold; }
.bed-type, .bed-user { font-size: 12px; color: #666; margin-top: 4px; }
.hint { font-size: 12px; opacity: 0.85; }
</style>
