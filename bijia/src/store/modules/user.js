// 用户 store（简化版）
const useUserStore = () => {
  return {
    logOut: () => {
      localStorage.removeItem('Authorization')
      return Promise.resolve()
    }
  }
}

export default useUserStore
