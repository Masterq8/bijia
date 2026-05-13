<template>
  <div class="topBar">
    <img src="@/assets/images/logo-col.png" alt="" class="images" />
    <div class="header">
      <el-button @click="Index()" class="nav-button">返回主页</el-button>
      <el-button @click="enterPlat()" class="nav-button">进入地图</el-button>
      <el-button @Click="aboutUs()" class="nav-button">关于我们</el-button>
      <el-button @Click="productShow()" class="nav-button">产品展示</el-button>
    </div>
  </div>
  <section class="top" style="background-color: rgb(34,79,226)">
    <div style="justify-content: center !important;">
      <div style="display:flex;justify-content: center !important;">
      <div>
        <img src="/resData/whyus.png" class="img1">
      </div>
      <div class="toptext">
        <h2 style="color: white">
          流程指引 &
          <span style="color: #0dcaf0">常见问题</span>
        </h2>
        <h3 style="color: white">
          BiJiaNet海洋海岸带遥感影像智能解译云平台是基于深度学习算法的遥感影像全场景智能解译平台软件
          <br>
          <br>
          本页面将作为引导页面帮助用户快速上手使用BiJiaNet
        </h3>
      </div>
    </div>
    </div>
  </section>
  <section style="background-color:rgb(185, 217, 243);">
  <div>
  <div class="section-header">
    <h2>使用流程</h2>
  </div>
  <div  style="display:flex;justify-content: center !important;">
    <div>
      <img src="/resData/PG.svg">
    </div>
  </div>
</div>
</section>
  <section style="background-image: url('/resData/backimage1.png');
background-position: top;
background-repeat: no-repeat;
background-size: cover;
width: 100%;
">
  <div>
  <div class="section-header">
    <h2>常见问题</h2>
  </div>
  <div style="display:flex;justify-content: center !important;">
    <div style="width: 75%">
      <img src="/resData/FAQ.png" class="img3" style="justify-content: center !important;">
    </div>
  </div>
  </div>
  </section>

  <div class="footer">
    <div>
      青岛山科人工智能有限公司©
      <img src="@/assets/images/beian.png" style="width: 16px;padding-right: 7px">
      <a href="https://beian.miit.gov.cn" style="padding-right: 7px">鲁ICP备2024052761号</a>
      鲁公网安备37021002001371号
    </div>
    <div style="margin-top: 10px">
      技术驱动
      <a href="/aboutUs">山东科技大学-ITRSLab；</a>
      <a href="https:www.huaweicloud.com/">华为云</a>
      | AICC
    </div>
  </div>
</template>

<script setup name="quickStart">
import {useRouter} from "vue-router";

const router = useRouter();
/* 关于我们 */
const aboutUs = () => {
  router.push("/aboutUs");
};
/* 产品展示 */
const productShow = () => {
  router.push("/productionShow");
};
/* 进入地图 */
const enterPlat = () => {
  router.push("/home");
};
/* 返回首页 */
const Index = () => {
  router.push("/index");
};
</script>

<style scoped>

.section-header{
  padding-top: 20px;
}
.section-header h2{
  text-align: center;
  padding-bottom: 10px;
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 20px;
  position: relative;
  margin-top: 0;
  justify-content: center !important;
}

img{
  max-width: 100%;
  height: auto;
  border-radius: 1rem;
  margin-bottom: 1.5rem;
}

.toptext{
  width: 700px;
}

.top h2{
  font-size: 57px;
  font-weight: 700;
  margin-bottom: 20px;
}

.top h3{
  font-size: 1.75rem;
  line-height: 1.2;
}

.img1{
  max-width: 100%;
  height: auto;
}

.footer{
  padding: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: rgb(184,215,241);
}

.footer img{
  margin-bottom: 0;
}

.topBar {
  position: fixed;
  background-color: rgba(187, 185, 193, 0.171);
  height: 50px;
  width: 100%;
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

.images {
  height: 50px;
  float: left;
  margin-left: 200px;
}
</style>
