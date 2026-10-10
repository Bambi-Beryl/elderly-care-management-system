<template>
  <el-card class="page-card" shadow="hover">
    <template v-if="title" #header>
      <div class="card-header">
        <span class="title">{{ title }}</span>
        <slot name="extra" />
      </div>
    </template>
    <slot />
  </el-card>
</template>

<script setup>
defineProps({ title: String })
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.title { font-size: 16px; font-weight: 600; color: #303133; }
</style>
