<template>
  <div class="app-container">
    <div class="content">
      <div class="topBar">
        <img src="@/assets/images/logo-col.png" alt="" class="images" />
        <div class="header">
          <!-- <el-button @click="logOn()" class="nav-button">登录/注册</el-button> -->


    <el-button v-if="state" @click="profile()" class="nav-button">{{userName}}</el-button>


    <el-button v-else @click="logOn()" class="nav-button">登录/注册</el-button>


          <el-button @Click="aboutUs()" class="nav-button">关于我们</el-button>
          <el-button @Click="productShow()" class="nav-button"
            >产品展示</el-button
          >
        </div>
      </div>

      <div class="button-group">
        <li class="title not-selectable">海洋海岸带遥感影像智能解译云平台</li>
        <div>
          <el-button
            @Click="enterPlat()"
            class="main-button"
            style="font-weight: bold"
            >进入平台</el-button
          >
        </div>
        <div>
          <el-button
            @Click="quickStart()"
            class="main-button"
            style="font-size: 18px; color: #ffffff; width: 100px; height: 45px"
            >快速上手</el-button
          >
        </div>
      </div>

      <div class="footer">
        <div class="footer-container">
          <div class="footer-text">
            青岛山科人工智能有限公司©
            <img src="@/assets/images/beian.png" alt="" class="image1" />
            <a href="https://beian.miit.gov.cn" class="footer-link"
              >鲁ICP备2024052761号</a
            >
            鲁公网安备37021002001371号
          </div>
          <div>
            技术驱动：<a href="/aboutUs" class="footer-link1">山东科技大学</a>
            <a href="https://www.qdhhc.edu.cn/" class="footer-link"
              >青岛黄海学院</a
            >；平台支持：
            <a href="https://www.huaweicloud.com/" class="footer-link">
              华为云
            </a>
            |AICC
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
/* 关于我们 */
const aboutUs = () => {
  router.push('/aboutUs');
};
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
}

.app-container {
  text-align: center;
  background-image: url("@/assets/images/sea1.jpg"); /* 替换为你的背景图片路径 */
  background-position: center;
  background-repeat: no-repeat;
  height: 100%;
  color: white; /* 字体颜色 */
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
  -webkit-touch-callout: none; /* iOS Safari */
  -webkit-user-select: none; /* Chrome/ */
  -khtml-user-select: none; /* Konqueror */
  -moz-user-select: none; /* Firefox */
  -ms-user-select: none; /* IE/Edge */
  user-select: none; /* Generic */
}

.content {
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh; /* 使内容垂直居中 */
}

.topBar {
  background-color: rgba(187, 185, 193, 0.171);
  height: 50px;
}

.header {
  display: flex;
  justify-content: center;
  gap: 20px; /* 按钮之间的间距 */
  margin-bottom: 20px;
  margin-left: 1000px;
  padding-top: 10px;
}

.nav-button {
  background-color: transparent; /* 背景透明 */
  color: white;
  border: none;
  margin: 0px;
  padding: 0px;
  font-size: 16px;
}

.button-group .title {
  font-size: 70px; /* 字体大小 */
  font-weight: bold; /* 加粗 */
  margin: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7); /* 文字阴影 */
  list-style: none;
}

.button-group {
  margin: 20px;
  margin-top: 265px;
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

.main-button:hover {
  transform: scale(1.1);
}

.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  text-align: center;
  color: black; /* 底部文字颜色 */
  background-color: rgba(255, 255, 255, 0.5);
  height: 83px;
}
.footer-link1 {
  color: blue; /* 链接颜色 */
  text-decoration: none; /* 去掉下划线 */
  margin-right: 10px;
}

.footer-link {
  color: blue; /* 链接颜色 */
  text-decoration: none; /* 去掉下划线 */
}
.footer-container {
  padding: 10px;
}

.footer-text {
  margin-bottom: 10px;
}

.images {
  height: 50px;
  float: left;
  margin-left: 200px;
}

.image1 {
  width: 16px;
  height: 16px;
  padding-left: 5px;
}
</style>
