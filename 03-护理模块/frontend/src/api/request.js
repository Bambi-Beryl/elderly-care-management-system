import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from '../utils/auth'

// 直连后端（已配置 CORS），避免 Vite 代理未生效时 /api 返回空页面
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000
})

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

request.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body && body.code === 200) {
      return body.data
    }
    const msg = body?.message || '请求失败'
    if (msg.includes('请先登录') || msg.includes('登录已过期')) {
      clearAuth()
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login'
      }
    }
    ElMessage.error(msg)
    return Promise.reject(new Error(msg))
  },
  (err) => {
    let msg = '网络错误'
    if (err.code === 'ECONNABORTED') {
      msg = '请求超时，请检查后端是否启动'
    } else if (err.response) {
      msg = err.response.data?.message || `服务器错误(${err.response.status})`
    } else if (err.message?.includes('Network Error')) {
      msg = '无法连接后端，请先启动 Spring Boot（端口 8080）'
    } else {
      msg = err.message || msg
    }
    ElMessage.error(msg)
    return Promise.reject(new Error(msg))
  }
)

export default request
