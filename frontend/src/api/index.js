import request from './request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data),
  logout: () => request.post('/auth/logout'),
  me: () => request.get('/auth/me')
}

export const userApi = {
  list: () => request.get('/user/list'),
  roles: () => request.get('/user/roles'),
  save: (data) => request.post('/user/save', data),
  delete: (id) => request.delete(`/user/${id}`),
  resetPassword: (id) => request.post(`/user/${id}/reset-password`)
}

export const dashboardApi = {
  stats: () => request.get('/dashboard/stats')
}

export const customerApi = {
  list: (name) => request.get('/customer/list', { params: { name } }),
  get: (id) => request.get(`/customer/${id}`),
  save: (data) => request.post('/customer/save', data),
  delete: (id) => request.delete(`/customer/${id}`),
  checkInList: () => request.get('/customer/check-in/list'),
  checkIn: (data) => request.post('/customer/check-in', data),
  registerCheckIn: (data) => request.post('/customer/check-in/register', data),
  deleteCheckIn: (id) => request.delete(`/customer/check-in/${id}`),
  checkOutList: () => request.get('/customer/check-out/list'),
  submitCheckOut: (data) => request.post('/customer/check-out/submit', data),
  auditCheckOut: (data) => request.post('/customer/check-out/audit', data),
  deleteCheckOut: (id) => request.delete(`/customer/check-out/${id}`),
  outingList: () => request.get('/customer/outing/list'),
  submitOuting: (data) => request.post('/customer/outing/submit', data),
  auditOuting: (data) => request.post('/customer/outing/audit', data),
  deleteOuting: (id) => request.delete(`/customer/outing/${id}`)
}

export const bedApi = {
  list: () => request.get('/bed/list'),
  rooms: () => request.get('/bed/rooms'),
  roomsGrouped: () => request.get('/bed/rooms/grouped'),
  vacant: (roomId) => request.get('/bed/vacant', { params: { roomId } }),
  stats: () => request.get('/bed/stats'),
  save: (data) => request.post('/bed/save', data),
  delete: (id) => request.delete(`/bed/${id}`),
  saveRoom: (data) => request.post('/bed/rooms', data),
  deleteRoom: (id) => request.delete(`/bed/rooms/${id}`),
  updateStatus: (id, status) => request.put(`/bed/${id}/status`, null, { params: { status } }),
  exchangeList: () => request.get('/bed/exchange/list'),
  exchange: (data) => request.post('/bed/exchange', data),
  deleteExchange: (id) => request.delete(`/bed/exchange/${id}`)
}

export const nursingApi = {
  items: () => request.get('/nursing/items'),
  saveItem: (data) => request.post('/nursing/items', data),
  deleteItem: (id) => request.delete(`/nursing/items/${id}`),
  levels: () => request.get('/nursing/levels'),
  saveLevel: (data) => request.post('/nursing/levels', data),
  deleteLevel: (id) => request.delete(`/nursing/levels/${id}`),
  customerSettings: () => request.get('/nursing/customer-settings'),
  saveCustomerSetting: (data) => request.post('/nursing/customer-settings', data),
  deleteCustomerSetting: (id) => request.delete(`/nursing/customer-settings/${id}`),
  records: () => request.get('/nursing/records'),
  saveRecord: (data) => request.post('/nursing/records', data),
  deleteRecord: (id) => request.delete(`/nursing/records/${id}`)
}

export const healthApi = {
  stewards: () => request.get('/health/stewards'),
  saveSteward: (data) => request.post('/health/stewards', data),
  deleteSteward: (id) => request.delete(`/health/stewards/${id}`),
  customerStewards: () => request.get('/health/customer-stewards'),
  saveCustomerSteward: (data) => request.post('/health/customer-stewards', data),
  deleteCustomerSteward: (id) => request.delete(`/health/customer-stewards/${id}`),
  serviceAttention: () => request.get('/health/service-attention'),
  saveServiceAttention: (data) => request.post('/health/service-attention', data),
  deleteServiceAttention: (id) => request.delete(`/health/service-attention/${id}`),
  dailyCare: () => request.get('/health/daily-care'),
  saveDailyCare: (data) => request.post('/health/daily-care', data),
  deleteDailyCare: (id) => request.delete(`/health/daily-care/${id}`)
}
