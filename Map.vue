<template>
    <div>
        <div>
            <div id="offLine"></div>
        </div>
        <!-- display:none 这个属性设置了元素的显示模式， none表示该元素在页面上不显示，也就是说他被完全隐藏，只有display被改变，这个元素可见 -->
        <!-- position: fixed；fixed 定位使元素相对于浏览器窗口固定定位，即无论用户如何滚动页面，元素的位置都不会改变。它总是保持在指定的位置上 -->
        <!-- bottom: 10px 这个属性指定了元素底部距离其包含块（在 fixed 定位的情况下是浏览器窗口）底边的距离。在这个例子中，bottom: 10px; 表示弹窗距离浏览器窗口底部有 10 像素的间距。    -->
        <!-- right: 10px 含义: 这个属性指定了元素右边缘距离其包含块（在 fixed 定位的情况下是浏览器窗口）右边的距离。right: 10px; 表示弹窗距离浏览器窗口右边缘有 10 像素的间距  -->
        <!-- background: white;  这个属性设置了元素的背景颜色。在这里，white 表示弹窗的背景颜色为白色    -->
        <!-- border: 1px solid black;  这个属性定  义了元素的边框样式。在这个例子中，1px solid black 表示弹窗有一个 1 像素宽的实线黑色边框。 -->
        <!-- padding: 10px; 这个属性设置了元素内容区域的内边距。10px 表示弹窗内部的内容距离弹窗边缘有 10 像素的空间。 -->
        <!-- z-index: 1000; 这个属性定义了元素的堆叠顺序。具有较高 z-index 值的元素会覆盖 z-index 值较低的元素。在这个例子中，z-index: 1000; 确保弹窗在页面上的其他元素之上显示。    -->
        <div id="floating-popup" style="display: none; position: fixed; bottom: 70px; right: 50px; width: 600px; height : 450px;
        background: white; border-radius: 10px; border: 1px solid black; padding: 10px; z-index: 1000;" >
            <button id="close-floating-popup" style="position: absolute; top: 10px; right: 10px; background: none; border: none; font-size: 20px; cursor: pointer;">&times;</button>
               <div class="popup-title" style="text-align: center; margin: 0; padding: 10px 0; font-size: 18px; font-weight: bold;">智能问答系统</div>
               <div style="width: 550px; height : 320px; text-align: center; align-items: center; justify-content: center" >
                       <!-- 这里是聊天记录 -->
                       <div class="chat-container">
                           <div
                                   v-for="(message, index) in messages"
                                   :key="index"
                                   :class="message.align === 'left' ? 'message-left' : 'message-right'"
                           >
                              <div  :class="message.align === 'left' ? 'avatar-content-left' : 'avatar-content-right'">
                               <!-- 头像容器 -->
                               <div class="avatar" :class="message.align === 'left' ? 'avatar-left' : 'avatar-right'">
                                   <img :src="message.align === 'left' ? robot : satellite " alt="头像" />
                               </div>
                                  </div>

                               <!-- 消息内容 -->
                               <div :class="message.align === 'left' ? 'message-content-left' : 'message-content-right'">
                               <!-- 消息内容 -->
                               <div class="name">
                                   <span>{{ message.name }} </span>
                               </div>

                               <div class="chat_message"  v-html="formatText(message.text)">
                               </div>

<!--                                   <div class="chat_message"></div>-->
                               </div>
                           </div>
                       </div>
               </div>
            <div style="display: flex; flex-direction: column; flex: 1; justify-content: flex-end;  margin-top: 10px;">
              <div style="display: flex; align-items: center;">
                <input type="text" placeholder="请输入内容" v-model="inputValue" style="flex: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"/>
                <button style="margin-left: 10px; padding: 8px 16px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;" @click="sendRequest">发送</button>
              </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, nextTick, ref } from "vue";
import { useRouter } from "vue-router";
import L from 'leaflet';
import 'leaflet-imageoverlay-rotated';
import probotImage from '@/assets/images/robot.png';
import  satellite from '@/assets/images/satellite.jpg'
import  robot from '@/assets/images/robot.jpg'
import { getByContent } from '@/api/map/satellite.js'
const showModal = ref(false);
const dialogVisible = ref(false);
const currentTime = ref(new Date().toString());
const inputValue = ref('');
const messages = ref([])

const router = useRouter();
const map = ref(null);
const emit = defineEmits(['gave-lat-lng'], ["getValue"]);
const drawnPolygon = ref(null); // 保存绘制的多边形

// 初始化地图
const initMap = () => {
  map.value = L.map("offLine", {
    center: [35.96592727383686, 110.15522837638856], // 地图中心
    zoom: 5, // 缩放比例
    zoomControl: false, // 禁用 + - 按钮
    doubleClickZoom: false, // 禁用双击放大
    attributionControl: false, // 移除右下角leaflet标识
    minZoom: 2,
    maxZoom: 18,
  });

  const streets = L.tileLayer(
    "https://webrd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x={x}&y={y}&z={z}",
    { attribution: "", maxZoom: 18 }
  ).addTo(map.value);

    // 创建一个自定义控件, position: 'bottomleft' 这个是指定他的位置为左下角
    const robotControl = L.control({ position: 'bottomright' });

    // onAdd 是一个回调函数，当控件会被……添加调用这个函数，这个map参数时地图示例，允许你在控件中访问和操作地图
    robotControl.onAdd = function (map) {
        // 创建按钮元素， 使用Leaflet的DOM工具创建一个<div> 元素，并添加三个CSS类
        // leaflet-bar ：用于在Leaflet创建工具条
        // leaflet-control： 这是Leaflet控件的标准样式
        // leaflet-control-custom:  这是你定义的自定义样式，用于进一步的样式定制
        const div = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
        div.style.backgroundColor = 'white';
        div.style.backgroundImage = `url(${probotImage})`; // 替换为你的机器人图像路径
        div.style.backgroundSize = 'cover';
        div.style.backgroundPosition = 'center'; // 背景图片居中显示

        div.style.width = '50px';
        div.style.height = '50px';
        div.style.cursor = 'pointer';
        div.style.border = '0px';
        div.style.backgroundSize = 'cover'; // 背景图片完全覆盖 div
        div.style.borderRadius = '50%';

        // 点击事件
        div.onclick = function () {
            if (showModal.value === true){
                showModal.value = false
            } else {
                showModal.value = true
            }
            // 这行代码通过 document.getElementById 方法获取具有 id 为 'floating-popup' 的元素，并将其赋值给变量 popup
            const popup = document.getElementById('floating-popup');
            if (popup.style.display === 'none') {
                popup.style.display = 'block';
            } else {
                popup.style.display = 'none';
            }
        };
       // 返回刚创建的div元素，这样这个Leaflet就会将这个控件添加到地图上
        return div;
    };

    // 添加控件到地图
    if (map.value) {
        robotControl.addTo(map.value);
    } else {
        console.error('地图实例未定义，无法添加控件');
    }
    // document.getElementById('close-floating-popup') 用于获取具有 id 为 'close-floating-popup' 的元素，这里假设该元素是一个按钮或其他可以点击的元素。.onclick 是设置点击事件处理程序的属性。它的值是一个函数，这个函数将在按钮被点击时执行。
    document.getElementById('close-floating-popup').onclick = function() {
        document.getElementById('floating-popup').style.display = 'none';
    };

  // 设置绘制多边形样式
  map.value.pm.setPathOptions({
    color: "pink",    //路径颜色
    fillColor: "dodgerblue",  //填充颜色为闪蓝
    fillOpacity: 0.2, //透明度，1为完全不透明
  });


  //设置标注语言（'zh'为中文）
  map.value.pm.setLang('zh');


  // 图形完成的回调
  map.value.on("pm:create", (e) => {
    emit('gave-lat-lng', e.marker._latlngs);
    drawnPolygon.value = e.layer;
    console.log(e.marker._latlngs); // 经纬度
  });


};


const formatText  = (text) => {
    const maxLength = 20; // 设置每行的最大字符数
    let result = ''
    for (let i = 0; i < text.length; i += maxLength) {
        result += text.slice(i, i + maxLength) + '<br>'; // 插入换行符
    }
    return result;
}

function sendRequest() {
    console.log('发送了一次');
    messages.value.push({ text: inputValue.value, align: "right", name: "用户", time: currentTime.value})
    backRequest(inputValue.value)
}

function backRequest(content) {
       const query = { content }
       getByContent(query).then(response => {

           console.log('response', response)
       })
}

// 绘制多边形方法
const drawPolygon = () =>{
  map.value.pm.enableDraw("Polygon", {
    snappable: true,  //开启捕捉
    snapDistance: 10  //捕捉精确度，越低越精确
  });
}

// 获取绘制的多边形
const getDrawnPolygon = () => {
  if (drawnPolygon.value) {
    return drawnPolygon.value;
  }
  return null;
};

//地图跳转到指定坐标点
const goToLatLng = (lat, lng) => {
  map.value.flyTo([lat, lng], 8);
};


const imageArr = ref([])

//添加多边形
const addPolygon = (points, imageUrl, id,data) => {

  /* const latlngs = [
    [points.TopLeftLatitude, points.TopLeftLongitude],
    [points.TopRightLatitude, points.TopRightLongitude],
    [points.BottomRightLatitude, points.BottomRightLongitude],
    [points.BottomLeftLatitude, points.BottomLeftLongitude]
  ]; */

  const ke1 = Math.max(points.TopLeftLatitude, points.TopRightLatitude) // 纬度最大值
  const ke2 = Math.min(points.BottomLeftLongitude, points.TopLeftLongitude) // 经度最小值
  const ke3 =  Math.min(points.BottomLeftLatitude, points.BottomRightLatitude) // 纬度最小值
  const ke4 = Math.max(points.BottomRightLongitude, points.TopRightLongitude) // 经度最大值

  const latlngs = [
    [ke1,ke2],
    [ke1,ke4],
    [ke3,ke4],
    [ke3,ke2],
  ]

  // 创建多边形
  const polygon = L.polygon(latlngs).addTo(map.value);
  //.setIndex(1);
  polygon.id = id
  polygon.data = data;  //保存详细数据

  polygon.on('contextmenu', function (e) {
    emit("getSonValue1", this.data);  //传递多边形对应的详细数据
  });

  polygon.on('click', function (e) {
      emit("getSonValue2", e.target.id); // 传递多边形ID
  });

  polygon.on('dblclick', function (e) {
  const id = e.target.id;
  emit("getValue", id)


  // 查找当前多边形ID对应的图片图层
  const index = imageArr.value.findIndex((item) => item.id === id);
  if (index !== -1) {
    // 如果图片已存在，则移除图片
    map.value.removeLayer(imageArr.value[index]);
    imageArr.value.splice(index, 1);
  } else {
    // 如果图片不存在，则添加图片
    /* const TL = [
      Math.max(points.TopLeftLatitude, points.TopRightLatitude), // 纬度最大值
      Math.min(points.BottomLeftLongitude, points.TopLeftLongitude) // 经度最小值
    ];
    const BR = [
      Math.min(points.BottomLeftLatitude, points.BottomRightLatitude), // 纬度最小值
      Math.max(points.BottomRightLongitude, points.TopRightLongitude) // 经度最大值
    ];
    let imgPoint = [TL, BR];

    const imageLayer = L.imageOverlay(imageUrl, imgPoint, { opacity: 1 }); */

    var topleft = L.latLng(points.TopLeftLatitude, points.TopLeftLongitude),
    topright = L.latLng(points.TopRightLatitude, points.TopRightLongitude),
    bottomleft = L.latLng(points.BottomLeftLatitude, points.BottomLeftLongitude);

    var imageLayer = L.imageOverlay.rotated(imageUrl, topleft, topright, bottomleft, {opacity: 1,interactive: false}).addTo(map.value);

    imageLayer.id = id;
    map.value.addLayer(imageLayer);
    imageArr.value.push(imageLayer);
  }
});


};

// 显示图片
const addRotatedImageWithPolygon = (points, imageUrl) => {

  /* const aaa = {
    TopLeftLatitude: points.leftupLatitude,
    TopLeftLongitude: points.leftupLongitude,
    TopRightLatitude: points.rightupLatitude,
    TopRightLongitude: points.rightupLongitude,
    BottomRightLatitude: points.rightdownLatitude,
    BottomRightLongitude: points.rightdownLongitude,
    BottomLeftLatitude: points.leftdownLatitude,
    BottomLeftLongitude: points.leftdownLongitude
  };

  // 从points对象中解析多边形的顶点
  const TL = [
    Math.max(aaa.TopLeftLatitude, aaa.TopRightLatitude), // 纬度最大值
    Math.min(aaa.BottomLeftLongitude, aaa.TopLeftLongitude) // 经度最小值
  ];
  const BR = [
    Math.min(aaa.BottomLeftLatitude, aaa.BottomRightLatitude), // 纬度最小值
    Math.max(aaa.BottomRightLongitude, aaa.TopRightLongitude) // 经度最大值
  ];

  // 图片边界点
  let imgPoint = [TL, BR]; */

  // 创建并添加图片
  // var imageLayer = L.imageOverlay(imageUrl, imgPoint, { opacity: 1 });//opacity是透明度
  // var imageLayer = L.imageOverlay(imageUrl, [[points.leftupLatitude,points.leftupLongitude],[points.rightdownLatitude,points.rightdownLongitude]], { opacity: 1 });

  var topleft = L.latLng(points.leftupLatitude, points.leftupLongitude),
	topright = L.latLng(points.rightupLatitude, points.rightupLongitude),
	bottomleft = L.latLng(points.leftdownLatitude, points.leftdownLongitude);

  var imageLayer = L.imageOverlay.rotated(imageUrl, topleft, topright, bottomleft, {opacity: 1,interactive: false}).addTo(map.value);

  imageLayer.id = points.id;
  map.value.addLayer(imageLayer);
  imageArr.value.push(imageLayer)
  console.log(imageArr.value)

};


const removeImage = (id) => {
  for (let index = 0; index < imageArr.value.length; index++) {
    const element = imageArr.value[index];
    if (element.id == id) {
      map.value.removeLayer(element);
      imageArr.value.splice(index,1)
    }
  }
}

const removeAllImage = () => {
    for (let index = 0; index < imageArr.value.length; index++) {
        const element = imageArr.value[index];
        map.value.removeLayer(element);
    }
    imageArr.value = []
}

const clearAllLayers = () => {
  if (map.value) {
    map.value.eachLayer((layer) => {
      if (layer instanceof L.Polygon || layer instanceof L.Rectangle) {
        map.value.removeLayer(layer);
      }
    });
  }
};

// 改变多边形框颜色
const changePolygonColor = (id, color, fillColor) => {
  map.value.eachLayer((layer) => {
    if (layer instanceof L.Polygon && layer.id === id) {
      layer.setStyle({ color: color, fillColor: fillColor });
    }
  });
};

// 暴露方法供父组件调用
defineExpose({
  drawPolygon,
  clearAllLayers,
  getDrawnPolygon,
  goToLatLng,
  addRotatedImageWithPolygon, // 修改为新的方法名
  addPolygon,
  removeImage,
  changePolygonColor,
  removeAllImage
});

onMounted(() => {
  initMap();
});
</script>

<style scoped>
#offLine {
  height: calc(100vh - 50px);
  /* width: 100%; */
}

#floating-popup {
    .close-btn:hover {
        background: darkred;
    }
}

.popup-title {
    text-align: center; /* 文本居中 */
    margin-top: 0; /* 顶部外边距为0，使其靠近弹窗顶部 */
    padding: 10px 0; /* 可选：添加一些内边距以增加美观性 */
    font-size: 18px; /* 可选：设置标题字体大小 */
    font-weight: bold; /* 可选：设置标题字体加粗 */
}


.chat-container {
    display: flex;
    flex-direction: column;
    width: 550px;
    background: #ffffff;
    border: 1px solid #e4e7ed;
    border-radius: 3px;
    overflow-y: auto;
    text-align: center;
    height: 320px;
    padding: 16px;
    .name {
        font-family: PingFangSC-Regular;
        font-weight: 200;
        font-size: 12px;
        color: #909399;
        margin-bottom: 12px;
        width: 100%; /* 确保名称占据足够空间 */
        overflow: hidden; /* 避免名称超出 */
    }
    .chat_message {
        padding: 15px 15px;
        background: #f8f8f9;
        border-radius: 4px;
        margin-bottom: 15px;
        word-wrap: break-word;
        white-space: nowrap ;
        font-weight: 400;
        font-size: 15px;
        color: #303133;
    }
}

.containerr {
    display: flex;
    align-items: center; /* 垂直居中容器内的所有子元素 */
    justify-content: space-between; /* 使子元素在容器的两端对齐 */
    width: 100%; /* 根据需要设置容器的宽度 */
}

.message-left {
    max-width: 418px;
    align-self: flex-start;
    display: flex;
    flex-direction: column;
}

.message-left .avatar {
    margin-right: 10px; /* 头像和消息内容之间的间距 */
}

.message-content-left {
    display: flex;
    align-items: center;
}

.message-content-right {
    display: flex;
    align-items: center;
    flex-direction: row-reverse;
}

.message-right {
    max-width: 418px;
    align-self: flex-end;
    display: flex;
    flex-direction: column;
}

.message-right .avatar {
    margin-left: 0; /* 头像和消息内容之间的间距 */
}

.message-right .message-content-right {
    text-align: right; /* 右侧消息内容文本对齐方式 */
}

/* 头像容器 */
.avatar {
    width: 50px; /* 设置头像宽度 */
    height: 50px; /* 设置头像高度 */
    border-radius: 50%; /* 圆形头像 */
    overflow: hidden; /* 隐藏超出部分 */
}

.avatar img {
    width: 100%; /* 填满头像容器 */
    height: auto; /* 保持图片比例 */
}

.avatar-left {
    /* 两边头像样式 */
    display: flex;
    flex-direction: column; /* 垂直排列 */
    align-items: center; /* 垂直居中头像 */
    justify-content: center; /* 水平居中头像 */
    width: 30px; /* 设定一个固定宽度 */
    height: 30px; /* 设定一个固定高度 */
    border-radius: 50%; /* 圆形边框 */
    overflow: hidden; /* 隐藏超出部分 */
    margin-right: 10px;
}
.avatar-content-right{
    display: flex;
    align-items: flex-end;
    flex-direction: row-reverse;
}

.avatar-right {
    /* 两边头像样式 */
    display: flex;
    flex-direction: column; /* 垂直排列 */
    align-items: center; /* 垂直居中头像 */
    width: 30px; /* 设定一个固定宽度 */
    height: 30px; /* 设定一个固定高度 */
    border-radius: 50%; /* 圆形边框 */
    overflow: hidden; /* 隐藏超出部分 */
    margin-right: 10px;
}
.avatar-left img, .avatar-right img {
    width: 100%; /* 图片宽度填满容器 */
    height: 100%; /* 图片高度填满容器 */
    object-fit: cover; /* 保持图片比例并覆盖容器 */
}

.draw {
  z-index: 999999;
  display: flex;
  width: 60px;
  height: 50px;
  position: absolute;
  left: 80px;
  justify-content: center;
  align-items: center;
}

.draw1 {
  z-index: 999999;
  display: flex;
  width: 60px;
  height: 50px;
  position: absolute;
  left: 200px;
  justify-content: center;
  align-items: center;
}
</style>
