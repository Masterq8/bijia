<template>
  <div class="navbar">
    <img class="hamburger-container" :src="logo" @click="toSy" />
    <top-nav id="topmenu-container" class="topmenu-container" />
    <div class="right-menu">
      <!-- 暗色/亮色模式切换按钮 -->
      <div class="theme-toggle" @click="toggleDarkMode">
        <el-icon v-if="settingsStore.darkMode" style="font-size: 18px;"><Sunny /></el-icon>
        <el-icon v-else style="font-size: 18px;"><Moon /></el-icon>
      </div>
      <div class="avatar-container">
        <el-dropdown v-if="state" @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <el-icon><caret-bottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>
                  <el-icon style="margin-right: 8px;"><User /></el-icon>
                  个人中心
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                <el-icon style="margin-right: 8px;"><Setting /></el-icon>
                <span>布局设置</span>
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon style="margin-right: 8px;"><SwitchButton /></el-icon>
                <span>注销</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <div v-else class="right-menu-item hover-effect login-register-buttons">
          <div class="button-container">
            <el-button @click="logOn" class="login-button nav-button">
              <el-icon style="margin-right: 6px;"><User /></el-icon>
              登录
            </el-button>
          </div>
          <div class="button-container">
            <el-button @click="register" class="register-button nav-button">
              <el-icon style="margin-right: 6px;"><Edit /></el-icon>
              注册
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import { Sunny, Moon } from '@element-plus/icons-vue'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import HeaderSearch from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import logo from '@/assets/logo/logo.png'
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserProfile } from "@/api/system/user";
import { getToken } from '@/utils/auth'

// 使用ref来创建响应式状态
const state = ref(false);

// 在组件挂载时调用API获取用户信息
onMounted(() => {
  if (getToken()) {
    state.value = true;
  } else {
    state.value = false;
  }
});

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const router = useRouter();

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout();
      break;
    case "logout":
      logout();
      break;
    default:
      break;
  }
}

function logout() {
  ElMessageBox.confirm('确定退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      location.href = '/index';
    })
  }).catch(() => { });
}

const emits = defineEmits(['setLayout'])
function setLayout() {
  emits('setLayout');
}

function toSy() {
  router.push('/index')
}

const logOn = () => {
  router.push('/login');
};
const register = () => {
  router.push('/register');
};

// 暗色/亮色模式切换
function toggleDarkMode() {
  settingsStore.toggleDarkMode(!settingsStore.darkMode);
}
</script>

<style lang='scss' scoped>
.login-register-buttons {
  display: flex;
  align-items: center;

  .button-container {
    margin-right: 10px;
    flex-shrink: 0;

    &:last-child {
      margin-right: 0;
    }

    .login-button,
    .register-button {
      padding: 8px 16px;
      transition: all 0.3s ease;
      border-radius: 6px;
    }

    .login-button:hover,
    .register-button:hover {
      background-color: var(--bg-hover, rgba(255, 255, 255, 0.15));
      border-color: var(--text-secondary, rgba(255, 255, 255, 0.6));
    }
  }
}

.nav-button {
  background-color: transparent;
  color: var(--text-primary, white);
  border: 1px solid var(--border-color, rgba(255, 255, 255, 0.3));
  margin: 0px;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 6px;
  transition: all 0.3s ease;

  &:hover {
    background-color: var(--bg-hover, rgba(255, 255, 255, 0.15));
    border-color: var(--text-secondary, rgba(255, 255, 255, 0.6));
  }
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: var(--navbar-bg, linear-gradient(90deg, #0f172a 0%, #111827 52%, #172033 100%));
  border-bottom: 1px solid var(--border-color, rgba(148, 163, 184, 0.16));
  box-shadow: var(--shadow-md, 0 8px 24px rgba(2, 6, 23, 0.22));

  .hamburger-container {
    line-height: 46px;
    width: 52px;
    float: left;
    margin: 3px 18px;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .main-button {
    background-color: transparent;
    color: var(--text-primary, white);
    border: 1px solid var(--border-color, white);
    border-radius: 10px;
    padding: 10px 20px;
    font-size: 18px;
    height: 50px;
    border-radius: 7px;
    margin: 5px;
    transition: transform 0.2s linear;
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 79px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;
    align-items: center;

    &:focus {
      outline: none;
    }

    .theme-toggle {
      cursor: pointer;
      padding: 0 12px;
      display: flex;
      align-items: center;
      color: var(--text-primary, white);
      transition: all 0.3s ease;

      &:hover {
        color: var(--text-secondary, rgba(255, 255, 255, 0.8));
        transform: scale(1.1);
      }
    }

    .avatar-container {
      margin-right: 28px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        display: flex;
        align-items: center;
        gap: 8px;

        .user-avatar {
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 8px;
          transition: transform 0.3s ease;

          &:hover {
            transform: scale(1.1);
          }
        }

        i {
          cursor: pointer;
          font-size: 12px;
          color: var(--text-secondary, rgba(255, 255, 255, 0.7));
        }
      }
    }
  }
}
</style>
