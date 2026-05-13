import defaultSettings from '@/settings'
import { useDynamicTitle } from '@/utils/dynamicTitle'

const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''

const useSettingsStore = defineStore(
  'settings',
  {
    state: () => ({
      title: '',
      theme: storageSetting.theme || '#409EFF',
      sideTheme: storageSetting.sideTheme || sideTheme,
      darkMode: storageSetting.darkMode !== undefined ? storageSetting.darkMode : true, // 默认暗色模式
      showSettings: showSettings,
      topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
      tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
      fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
      sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
      dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
    }),
    actions: {
      // 修改布局设置
      changeSetting(data) {
        const { key, value } = data
        if (this.hasOwnProperty(key)) {
          this[key] = value
        }
      },
      // 设置网页标题
      setTitle(title) {
        this.title = title
        useDynamicTitle();
      },
      // 切换暗色/亮色模式
      toggleDarkMode(isDark) {
        this.darkMode = isDark
        this.sideTheme = isDark ? 'theme-dark' : 'theme-light'
        // 保存到本地存储
        this.saveSettings()
        // 应用主题
        this.applyTheme()
      },
      // 应用主题
      applyTheme() {
        const theme = this.darkMode ? 'dark' : 'light'
        document.documentElement.setAttribute('data-theme', theme)
        // 同步更新侧边栏主题
        this.sideTheme = this.darkMode ? 'theme-dark' : 'theme-light'
      },
      // 保存设置
      saveSettings() {
        const layoutSetting = {
          "topNav": this.topNav,
          "tagsView": this.tagsView,
          "fixedHeader": this.fixedHeader,
          "sidebarLogo": this.sidebarLogo,
          "dynamicTitle": this.dynamicTitle,
          "sideTheme": this.sideTheme,
          "theme": this.theme,
          "darkMode": this.darkMode
        }
        localStorage.setItem("layout-setting", JSON.stringify(layoutSetting))
      }
    }
  })

export default useSettingsStore
