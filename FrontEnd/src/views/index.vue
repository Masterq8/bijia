<template>
  <div class="app-container">
    <div class="content">
      <!-- 顶部导航栏 -->
      <div class="topBar">
        <img src="@/assets/images/logo-col.png" alt="BiJiaNet Logo" class="images" @click="enterPlat()" />
        <div class="header">
          <el-button v-if="state" @click="profile()" class="nav-button">
            <el-icon style="margin-right: 6px;"><User /></el-icon>
            {{ userName }}
          </el-button>
          <el-button v-else @click="logOn()" class="nav-button">
            <el-icon style="margin-right: 6px;"><User /></el-icon>
            登录/注册
          </el-button>
          <!-- 关于我们 已移除 -->
          <el-button @click="productShow()" class="nav-button">产品展示</el-button>
        </div>
      </div>

      <!-- 主内容区域 -->
      <div class="button-group">
        <h1 class="title not-selectable">海洋海岸带遥感影像智能解译云平台</h1>
        <p class="subtitle not-selectable">基于深度学习的海洋遥感影像智能解译系统</p>
        <div class="button-row">
          <el-button @click="enterPlat()" class="main-button" type="primary">
            <el-icon style="margin-right: 8px;"><Monitor /></el-icon>
            进入平台
          </el-button>
          <el-button @click="quickStart()" class="main-button">
            <el-icon style="margin-right: 8px;"><QuestionFilled /></el-icon>
            快速上手
          </el-button>
        </div>
      </div>

      <!-- 底部信息 -->
      <div class="footer">
        <div class="footer-container">
          <div class="footer-text">
            青岛山科人工智能有限公司©
            <img src="@/assets/images/beian.png" alt="备案" class="image1" />
            <a href="https://beian.miit.gov.cn" class="footer-link" target="_blank">鲁ICP备2024052761号</a>
            鲁公网安备37021002001371号
          </div>
          <div>
            技术驱动：<!-- 关于我们 链接已移除 -->
            <a href="https://www.qdhhc.edu.cn/" class="footer-link" target="_blank">青岛黄海学院</a>
            ；平台支持：
            <a href="https://www.huaweicloud.com/" class="footer-link" target="_blank">华为云</a>
            | AICC
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="Index">


import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getUserProfile } from "@/api/system/user";
import { getToken } from '@/utils/auth'

const router = useRouter();

// 使用ref来创建响应式状态
const state = ref(false);
const userName = ref('');

// 在组件挂载时调用API获取用户信息
onMounted(() => {
  // const token = localStorage.getItem('token');
  if (getToken()) {
    getUserInfo();
    state.value = true;
  } else {
    state.value = false;
  }
});

// API调用函数
const getUserInfo = async () => {
  try {
    const response = await getUserProfile();
    userName.value = response.data.userName; // 假设后端返回的用户名字段为userName
  } catch (error) {
    console.error('Error fetching user profile:', error);
    state.value = false; // 如果获取用户信息失败，设置state为false
  }
};

/* 个人中心 */
const profile = () => {
  router.push('/personal/information');
};
/* 登录注册 */
const logOn = () => {
  router.push('/login');
};
/* 关于我们 已移除 */
/* 产品展示 */
const productShow = () => {
  router.push('/productionShow');
};
/* 进入平台 */
const enterPlat = () => {
  router.push('/home');
};
/* 快速上手 */
const quickStart = () => {
  router.push('/quickStart');
};
</script>

<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  text-align: center;
  background-image: url("@/assets/images/sea1.jpg");
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  height: 100%;
  color: white;
  margin: 0;
  padding: 0;
  animation: change 20s infinite alternate;
}

@keyframes change {
  0% {
    background-image: url("@/assets/images/sea1.jpg");
    background-size: cover;
  }
  20% {
    background-image: url("@/assets/images/red2.jpg");
    background-size: cover;
  }
  40% {
    background-image: url("@/assets/images/green2.png");
    background-size: cover;
  }
  80% {
    background-image: url("@/assets/images/red6.png");
    background-size: cover;
  }
  100% {
    background-image: url("@/assets/images/green3.png");
    background-size: cover;
  }
}

.not-selectable {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 100vh;
  position: relative;
}

.topBar {
  background-color: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  height: 60px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
}

.header {
  display: flex;
  align-items: center;
  gap: 24px;
}

.nav-button {
  background-color: transparent;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.15);
    border-color: rgba(255, 255, 255, 0.6);
    transform: translateY(-2px);
  }
}

.button-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  padding-top: 60px;
  
  .title {
    font-size: clamp(32px, 5vw, 72px);
    font-weight: bold;
    margin: 20px;
    text-shadow: 2px 4px 8px rgba(0, 0, 0, 0.6);
    list-style: none;
    line-height: 1.3;
    letter-spacing: 2px;
  }
  
  .subtitle {
    font-size: clamp(14px, 2vw, 20px);
    color: rgba(255, 255, 255, 0.85);
    margin-bottom: 30px;
    text-shadow: 1px 2px 4px rgba(0, 0, 0, 0.5);
    letter-spacing: 1px;
  }
  
  .button-row {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
    justify-content: center;
  }
}

.main-button {
  background-color: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  padding: 14px 36px;
  font-size: 18px;
  height: 54px;
  margin: 12px;
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
  min-width: 160px;
  
  &:hover {
    transform: translateY(-3px);
    background-color: rgba(255, 255, 255, 0.15);
    border-color: white;
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
  }
  
  &:active {
    transform: translateY(-1px);
  }
}

.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.9);
  background-color: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  padding: 12px 0;
  font-size: 13px;
}

.footer-link1 {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  margin-right: 10px;
  transition: color 0.3s ease;
  
  &:hover {
    color: #409EFF;
  }
}

.footer-link {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  transition: color 0.3s ease;
  
  &:hover {
    color: #409EFF;
  }
}

.footer-container {
  padding: 0 20px;
}

.footer-text {
  margin-bottom: 6px;
}

.images {
  height: 45px;
  cursor: pointer;
  transition: transform 0.3s ease;
  
  &:hover {
    transform: scale(1.05);
  }
}

.image1 {
  width: 16px;
  height: 16px;
  padding-left: 5px;
  vertical-align: middle;
}
</style>
