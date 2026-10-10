import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'
import { getToken } from '../utils/auth'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue'), meta: { public: true, title: '登录' } },
  { path: '/register', component: () => import('../views/Register.vue'), meta: { public: true, title: '注册' } },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      { path: '/dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '系统首页', module: '首页' } },
      { path: '/customer/list', component: () => import('../modules/customer/CustomerList.vue'), meta: { title: '客户列表', module: '客户管理' } },
      { path: '/customer/check-in', component: () => import('../modules/customer/CheckIn.vue'), meta: { title: '入住登记', module: '客户管理' } },
      { path: '/customer/check-out', component: () => import('../modules/customer/CheckOut.vue'), meta: { title: '退住登记', module: '客户管理' } },
      { path: '/customer/outing', component: () => import('../modules/customer/Outing.vue'), meta: { title: '外出登记', module: '客户管理' } },
      { path: '/bed/diagram', component: () => import('../modules/bed/BedDiagram.vue'), meta: { title: '床位示意图', module: '床位管理' } },
      { path: '/bed/manage', component: () => import('../modules/bed/BedManage.vue'), meta: { title: '床位管理', module: '床位管理' } },
      { path: '/nursing/items', component: () => import('../modules/nursing/NursingItems.vue'), meta: { title: '护理项目', module: '护理模块' } },
      { path: '/nursing/levels', component: () => import('../modules/nursing/NursingLevels.vue'), meta: { title: '护理级别', module: '护理模块' } },
      { path: '/nursing/settings', component: () => import('../modules/nursing/CustomerNursing.vue'), meta: { title: '客户护理设置', module: '护理模块' } },
      { path: '/nursing/records', component: () => import('../modules/nursing/NursingRecords.vue'), meta: { title: '护理记录', module: '护理模块' } },
      { path: '/health/stewards', component: () => import('../modules/health/CustomerSteward.vue'), meta: { title: '设置服务对象', module: '健康管家' } },
      { path: '/health/attention', component: () => import('../modules/health/ServiceAttention.vue'), meta: { title: '服务关注', module: '健康管家' } },
      { path: '/health/daily', component: () => import('../modules/health/DailyCare.vue'), meta: { title: '日常护理', module: '健康管家' } },
      { path: '/user/manage', component: () => import('../modules/user/UserManage.vue'), meta: { title: '基础数据维护', module: '用户管理' } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, _from, next) => {
  const token = getToken()
  if (to.meta.public) {
    if (token && (to.path === '/login' || to.path === '/register')) {
      next('/dashboard')
    } else {
      next()
    }
    return
  }
  if (!token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
