// 简单的缓存模块
const cache = {
  session: {
    getJSON(key) {
      const value = sessionStorage.getItem(key)
      return value ? JSON.parse(value) : null
    },
    setJSON(key, value) {
      sessionStorage.setItem(key, JSON.stringify(value))
    }
  },
  local: {
    getJSON(key) {
      const value = localStorage.getItem(key)
      return value ? JSON.parse(value) : null
    },
    setJSON(key, value) {
      localStorage.setItem(key, JSON.stringify(value))
    }
  }
}

export default cache
