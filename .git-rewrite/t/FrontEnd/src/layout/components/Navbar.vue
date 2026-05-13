<template>
  <div class="navbar">
    <img class="hamburger-container" :src="logo" @click ="toSy"/>
    <top-nav id="topmenu-container" class="topmenu-container"/> 
    <div class="right-menu">
      <div class="avatar-container">
        <el-dropdown v-if="state" @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <el-icon><caret-bottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <!-- <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                <span>布局设置</span>
              </el-dropdown-item> -->
            <el-dropdown-item command="logout">
                <span>注销</span>
              </el-dropdown-item>
            </el-dropdown-menu> 
              
      



          </template>   
      
  
        </el-dropdown>   
           
      <!-- ... previous code ... -->

<div v-else class="right-menu-item hover-effect login-register-buttons">
  <div class="button-container">
    <el-button @click="logOn" class="login-button nav-button">登录</el-button>
  </div>
  <div class="button-container">
    <el-button @click="register" class="register-button nav-button">注册</el-button>
  </div>
</div>

<!-- ... previous code ... -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
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
  // const token = localStorage.getItem('token');
  if (getToken()) {
    // getUserInfo();
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

function toSy(){
  router.push('/index')
}

const logOn = () => {
  router.push('/login');
};
const register = () => {
  router.push('/register');
};
</script>

<style lang='scss' scoped>

.login-register-buttons {
  display: flex;

  .button-container {
    margin-right: 10px; /* Add some spacing between the buttons */
    flex-shrink: 0; /* Prevent buttons from shrinking */

    &:last-child {
      margin-right: 0; /* Remove margin for the last button */
    }

    .login-button,
    .register-button {
      padding: 8px 16px; /* Adjust padding to make the buttons more visible */
      transition: background-color 0.3s, color 0.3s; /* Smooth transition for hover effect */
    }

    .login-button:hover,
    .register-button:hover {
      background-color: #007BFF; /* 鼠标悬停时背景颜色为蓝色 */
      color: #fff; /* 鼠标悬停时文字颜色为白色 */
    }
  }
}

.nav-button {
  background-color: transparent; /* 背景透明 */
  color: white;
  border: none;
  margin: 0px;
  padding: 0px;
  font-size: 16px;
}
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: linear-gradient(90deg, #0f172a 0%, #111827 52%, #172033 100%);
  border-bottom: 1px solid rgba(148, 163, 184, 0.16);
  box-shadow: 0 8px 24px rgba(2, 6, 23, 0.22);

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
  color: white; /* 按钮字体颜色 */
  border: 1px solid white;
  border-radius: 10px;
  padding: 10px 20px;
  font-size: 18px;
  height: 50px;
  border-radius: 7px;
  margin: 5px; /* 按钮之间的间距 */
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

    &:focus {
      outline: none;
    }


    .avatar-container {
      margin-right: 28px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
