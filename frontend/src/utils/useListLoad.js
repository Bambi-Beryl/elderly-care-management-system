import { ref } from 'vue'

/** 带 loading 的列表加载 */
export function useListLoad(fetcher) {
  const list = ref([])
  const loading = ref(false)

  const load = async () => {
    loading.value = true
    try {
      const data = await fetcher()
      list.value = Array.isArray(data) ? data : (data ? [data] : [])
    } catch {
      list.value = []
    } finally {
      loading.value = false
    }
  }

  return { list, loading, load }
}
