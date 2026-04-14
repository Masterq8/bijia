import { defineStore } from 'pinia'

export const useHomeStore = defineStore('home', {
  state: () => ({
    selectedDataTypes: [],
    selectedProvince: '',
    selectedCity: '',
    keepResults: true,
    activeRange: 'halfYear',
  }),
  actions: {
    setDataTypes(val) {
      this.selectedDataTypes = val
    },
    setProvince(val) {
      this.selectedProvince = val
    },
    setCity(val) {
      this.selectedCity = val
    },
    setKeepResults(val) {
      this.keepResults = val
    },
    setActiveRange(val) {
      this.activeRange = val
    },
  },
})
