<template>
  <div id="app" class="full-height">
    <main class="main">
      <div class="main-left">
        <div class="left-table">
          <div style="display: flex;justify-content: center;color: white;font-size: 16px;padding: 10px 10px;background-color: #69b4ea;border-radius: 2px;">产品类型</div>
          <div v-for="item in sidebarItems" :key="item.header || item.label"  class="left-item" @click="onItemClick(item)">
            <template v-if="item.header">
            <h2>{{ item.header }}</h2>
          </template>
          <template v-else>
            {{ item.label }}
          </template>
          </div>
        </div>
      </div>
      <div class="main-right">
        <div class="right-content">
          <div class="right-content-c">
            <div class="right-table">
              <div class="right-list">
                <!-- 使用v-for遍历products数组 -->
<div class="list-row"  >
  <div class="listNode"
                       v-for="product in visibleProducts"
                       :key="product.id"
                       style="width: 25%; max-width:25%">
    <div class="list-item">
      <div class="item-content">
        <div class="content-img-one">
          <div class="content-img-tow">
            <div class="content-img-three">
              <img :src="product.imageUrl" alt="" />
            </div>
          </div>
        </div>
        <div class="info-title">{{ product.title }}</div>
        <div class="info-info">
          <div class="info-info-item">
            <span class="info-info-title">原始影像</span>
            <span class="info-info-content">{{ product.details.find(detail => detail.label === '原始影像')?.value }}</span>
          </div>
          <div class="info-info-item">
            <span class="info-info-title">处理时间</span>
            <span class="info-info-content">{{ product.details.find(detail => detail.label === '处理时间')?.value }}</span>
          </div>
        </div>
        <div class="model-buttons">
          <el-button class="iconfont-zyy icon-zyyyanjing-kai_1" color="black" @click="viewImage(product)"></el-button>
          <el-button class="iconfont-zyy icon-zyyxiazai" color="black" @click="downloadImage(product)"></el-button>
        </div>
      </div>
    </div>
  </div>
</div>


              </div>
            </div>
          </div>
          <button class="load-more-btn" @click="onLoadMore">加载更多……</button>
        </div>
      </div>
      <div class="model" v-if="showModal" @click.self="closeModal">
        <img :src="selectedProduct.imageUrl" @load="handleImageLoad" @error="handleImageError" class="model-image" alt="Product Image" />
      </div>
    </main>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, reactive } from 'vue';
import {getOrderInfoList} from '@/api/order/orderInfo'
import {ElMessageBox} from "element-plus";
import { getToken } from '@/utils/auth'
import { blobValidate } from '@/utils/ruoyi'
import { saveAs } from 'file-saver'
const baseURL = import.meta.env.VITE_APP_BASE_API


const sidebarItems = [
  { label: '海岸线', class: '海岸线' },         //shoreline
  { label: '海水养殖', class: '海水养殖区' },   //mariculture
  // { label: '土地覆盖', class: '土地覆盖' },       //landCover
  { label: '绿潮浒苔', class: '绿潮浒苔' },           //green
  { label: '滨海湿地', class: '滨海湿地' },      //coastalWet
  { label: '马尾藻金潮', class: '马尾藻金潮' },     //scagassum
  { label: '赤潮检测', class: '赤潮检测' },       //landCover
];

const allProducts = reactive([]);
const visibleProducts = reactive([]);

// 对产品列表进行排序
allProducts.sort((a, b) => a.id - b.id);


const showModal = ref(false);
const selectedProduct = reactive({});

const onItemClick = (item) => {
  console.log(`Clicked on ${item.label}`);
  const type = item.class;
  fetchProductsByCategory(type);
};

const loadMore = () => {
  const start = visibleProducts.length;
  const end = Math.min(start + 8, allProducts.length); // 每次加载8条或剩余所有
  const moreProducts = allProducts.slice(start, end);

  visibleProducts.push(...moreProducts);

  // 如果没有更多产品，禁用按钮或显示提示信息
  if (moreProducts.length <= 0) {
    ElMessageBox.alert('没有更多数据了', '提示', {
      confirmButtonText: '确定',
      customClass: 'my-custom-message-box'
    });
  }
};


const onLoadMore = () => {
  loadMore();
};

const onShowProduct = (product) => {
  selectedProduct.id = product.id;
  selectedProduct.title = product.title;
  selectedProduct.imageUrl = product.imageUrl;
  selectedProduct.filepath = product.filepath;
  selectedProduct.details = product.details;
  selectedProduct.category = product.category;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

// 查看按钮
const viewImage = (product) => {
  onShowProduct(product);
  console.log('View image:', product.imageUrl);
};

// 下载按钮
//function downloadImage(product) {
//   window.open(product.tifUrl)
//};

const downloadImage = (product) => { // 图片 URL 
const imageUrl = product.tifUrl; // 创建一个<a>标签 
const link = document.createElement('a'); link.href = imageUrl; // 设置下载属性 
link.download = 'image.jpg'; // 将<a>标签加入文档 
document.body.appendChild(link); // 打开<a>标签链接 
window.open(link.href); // 移除<a>标签 
document.body.removeChild(link); // 显示下载成功消息 
ElMessage.success('图片已下载'); 
};

//专题图分类表列表查询方法
const fetchProductsByCategory = (type) => {
   allProducts.splice(0, allProducts.length);
  visibleProducts.splice(0, visibleProducts.length);
  getOrderInfoList({ type }).then(res => {
    console.log('API Response:', res);
    if (res && res.hasOwnProperty('rows') && Array.isArray(res.rows)) {
      const formattedProducts = res.rows.map(orderInfo => ({
        id: orderInfo.id,
        title: orderInfo.title,
        imageUrl: orderInfo.imageUrl,
        tifUrl: orderInfo.tifUrl,
        type: orderInfo.type,
        filepath: orderInfo.filepath,
        details: [
          { label: '处理时间', value: orderInfo.orderTime || '' },
        ],
      }));
      allProducts.splice(0, allProducts.length); // 清空当前数据
      allProducts.push(...formattedProducts);
      visibleProducts.splice(0, visibleProducts.length); // 清空当前可见数据
      visibleProducts.push(...formattedProducts.slice(0, 12)); // 初始加载12条
      console.log('Fetched products:', allProducts);
    } else {
      console.error('The API response is not in the expected format.');
    }
  }).catch(error => {
    console.error('Error fetching products by category:', error);
  });
};

const getList = () => {
  fetchProductsByCategory(null); // 初始化时无分类
};

 function handleImageLoad(event) {
    console.log('Image loaded successfully:', event.target.src);
  };

  function handleImageError(event) {
    console.error('Image failed to load:', event.target.src);
  };

// ... 初始加载时调用 ...
getList();
</script>


<style scoped>
#app.full-height {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  background-color: #1c1e21;
  display: flex;
  flex-grow: 1;
  overflow: hidden;
  width: 100%;
}

.main-left {
  background-color: #1c1e21;
  border-right: 1px solid rgba(232, 234, 237, .2);
  box-sizing: border-box;
  flex-shrink: 0;
  height: 100%;
  padding: 28px 12px;
  width: 15%;
}

.left-item {
  align-items: center;
  color: #ffffff; /* 设置字体颜色为白色 */
  cursor: pointer;
  display: flex;
  height: 40px;
  justify-content: center;
  position: relative;
  width: 100%;
  margin: 15px 0;
}

.left-item:hover {
  background-color: rgba(60, 128, 255, 0.77);
  border-radius: 5px;
}

.item-content {
    background-color: #202326;
    border-radius: 2px;
    box-sizing: border-box;
    margin: 0 auto 24px;
    padding: 12px 12px 0;
    position: relative;
    transition: all .3s;
    width: 100%;
}


.content-img-one {
  background: #17181a;
  height: 0;
  padding-bottom: 56%;
  position: relative;
  width: 100%;
}

.content-img-tow {
    align-items: center;
    bottom: 0;
    cursor: pointer;
    display: flex;
    justify-content: center;
    left: 0;
    position: absolute;
    right: 0;
    top: 0;
}

.content-img-three {
    align-items: center;
    background-color: #0c0e12;
    display: flex;
    height: 100%;
    justify-content: center;
    position: relative;
    width: 100%;
}

.content-img-three > img {
  height: 100%;
  object-fit: contain;
  width: 100%;
}

.info-title {
    color: rgba(232, 234, 237, .9);
    font-size: 14px;
    margin: 8px 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
}

.info-info {
    align-items: center;
    border-bottom: 1px solid rgba(232, 234, 237, .2);
    display: flex;
    flex-wrap: wrap;
    font-size: 12px;
    padding-bottom: 8px;
    width: 100%;
}

.info-info-item {
    align-items: center;
    display: flex;
    height: 20px;
    width: 100%;
}

.info-info-title {
    color: rgba(232, 234, 237, .5);
    flex-shrink: 0;
    margin-right: 4px;
    width: 48px;
}

.info-info-content {
    color: rgba(232, 234, 237, .7);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.main-right {
  background-color: #17181a;
  flex: 1;
  height: 100%;
  overflow: hidden;
  position: relative;
}

.right-content {
    height: 100%;
    overflow-x: hidden;
    overflow-y: auto;
}

.right-content-c {
    padding: 12px 28px;
    width: 100%;
}

.right-table {
    height: auto;
    overflow-x: hidden;
    overflow-y: auto;
    padding-right: 5px;
    padding-top: 5px;
    width: 100%;
}

.right-list {
    font-feature-settings: 'tnum';
    box-sizing: border-box;
    color: hsla(0, 0%, 100%, .85);
    font-size: 12px;
    font-variant: tabular-nums;
    line-height: 1.5715;
    list-style: none;
    margin: 0;
    padding: 0;
    position: relative;
}

.list-row {
  margin-left: -12px;
  margin-right: -12px;
  display: flex;
  flex-wrap: wrap;
}

.list-item {
  flex: 0 0 calc(25% - 24px); /* 每个项目占据25%，减去左右各12px的边距 */
  margin: 12px; /* 左右上下各12px的边距 */
}


.load-more-btn {
  background: #1c1e21;
  border: solid 1px whitesmoke;
  border-radius: 4px;
  color: #fff;
  padding: 5px 25px 5px 25px;
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}

.model {
  display: block;
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
}

.model-image {
  display: block;
  max-width: 90%;
  max-height: 90%;
  margin: auto;
  margin-top: 2%;
  margin-bottom: 2%;
}

.model-buttons {
  display: flex;
  justify-content: center;
  margin: 10px 0;
  width: 100%;
  padding-bottom: 10px; /* 增加底部padding */
}

.view-button, .download-button {
  background-color: #1c1e21;
  border: solid 1px whitesmoke;
  border-radius: 4px;
  color: #fff;
  padding: 5px 15px;
  margin: 0 5px;
  display: flex;
  align-items: center;
}

.view-button i, .download-button i {
  margin-right: 5px;
}



</style>
