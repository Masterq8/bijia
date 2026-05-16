<template>
  <div class="home">
    <div class="app-content" v-show="showText">

      <div class="panel-top">
        <div class="panel-title">遥感数据</div>
        <div class="panel-subtitle">{{ activePanel === 'search' ? '选择数据类型、检索范围与采集时间，快速定位海岸带遥感影像' : '上传本地影像数据，解压后自动加入我的数据并在地图中预览' }}</div>
        <div class="panel-actions">
          <button class="panel-action" :class="{ active: activePanel === 'search' }" type="button" @click="switchPanel('search')">检索数据</button>
          <button class="panel-action" :class="{ active: activePanel === 'upload' }" type="button" @click="switchPanel('upload')">上传我的数据</button>
        </div>
      </div>

      <template v-if="activePanel === 'search'">
        <el-row :gutter="10" class="mb8">
          <el-col :span="6">
            <div class="app-tabulation">
              <div class="app-type">数据类型</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-tree ref="elTreeRef"
                style="max-width: 600px; width: 200px; margin-top: 20px;" :data="data"
                show-checkbox node-key="dictValue" :props="menuProps" @check-change="handleNodeClick">
              </el-tree>
            </div>
          </el-col>
        </el-row>

        <div class="area-card">
          <div class="area-card__title">检索区域</div>
          <div class="area-card__desc">请选择一种空间范围方式：支持多边形绘制、矩形绘制或上传 GeoJSON 矢量范围。</div>
          <div class="area-actions">
            <el-button :type="drawMode === 'polygon' ? 'primary' : 'default'" class="area-action" @click="startDrawPolygon">多边形绘制</el-button>
            <el-button :type="drawMode === 'rectangle' ? 'primary' : 'default'" class="area-action" @click="startDrawRectangle">矩形绘制</el-button>
            <el-upload
              class="vector-upload"
              accept=".geojson,.json"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleVectorFileChange"
            >
              <el-button :type="drawMode === 'vector' ? 'primary' : 'default'" class="area-action">上传矢量图</el-button>
            </el-upload>
            <el-button class="area-action" @click="cancelDrawing">清除区域</el-button>
          </div>
          <div class="area-card__tip">说明：矢量图当前支持 GeoJSON / JSON 格式，上传后将自动定位并作为检索范围。</div>
        </div>

        <el-row>
          <el-col :span="6">
            <div class="app-type">采集时间</div>
          </el-col>
          <el-col :span="4">
            <div class="app-time">
              <el-date-picker :key="datePickerKey" v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期"
                end-placeholder="结束日期" size="small" :disabled-date="disabledDate"
                style="width: 100%; height: 34px">
              </el-date-picker>
            </div>
          </el-col>
        </el-row>

        <el-row type="flex" justify="center">
          <el-tag
                  v-for="item in dateRanges"
                  :key="item.label"
                  class="quick-date-tag"
                  :class="{ active: selectedQuickRange === item.value }"
                  effect="plain" @click="handleTagClick(item.value)">
              {{ item.label }}
          </el-tag>
        </el-row>
        <div class="search-action-card" v-show="isShowPage">
          <div class="search-action-card__summary">{{ querySummary }}</div>
          <el-button type="primary" class="search-primary" @click="search()">开始检索</el-button>
        </div>
      </template>

      <div v-else class="upload-page-card">
        <div class="upload-page-card__icon">⇧</div>
        <div class="upload-page-card__title">上传我的数据</div>
        <div class="upload-page-card__desc">当前为上传页面，不需要填写检索条件。点击下方按钮打开本地影像上传表单，原上传、解压、地图预览能力保持不变。</div>
        <el-button type="primary" class="upload-page-card__button" @click="handleUpload()">打开上传表单</el-button>
        <div class="upload-page-card__tips">
          <span>支持压缩包上传</span>
          <span>解压后加入我的数据</span>
          <span>上传完成后自动定位预览</span>
        </div>
      </div>
    </div>

    <!-- 打开上传对话框 -->
    <el-dialog title="本地影像上传" v-model="open" width="600px" append-to-body>
      <el-form :model="form" ref="infoRef" :rules="rules" label-width="100px"  class="upload-form">
        <!-- <el-form-item label="文件名称">
          <el-input v-model="form.imageName" />
        </el-form-item>

        <el-form-item label="图像上传">
          <image-upload v-model="form.image" />
        </el-form-item>-->

        <el-form-item label="采集时间" prop="collectTime">
          <el-date-picker clearable v-model="form.collectTime" type="date" value-format="YYYY-MM-DD"
            placeholder="请选择采集时间" style="width:200px;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="所属区域" prop="province">
          <el-select v-model="selectedProvince" @change="handleProvinceChange" placeholder="请选择省份" clearable style="width:200px;">
            <el-option v-for="province in options" :key="province.value" :label="province.label"
              :value="province.value"></el-option>
          </el-select>
          <el-select v-model="selectedCity" placeholder="请选择城市" clearable style="padding-left:10px;width:210px;">
            <el-option v-for="city in selectedProvinceChildren" :key="city.value" :label="city.label"
              :value="city.value"></el-option>
          </el-select>
        </el-form-item>
        <!--
                <el-form-item label="卫星类型" prop="satelliteType">
                  <el-tree-select v-model="form.satelliteType" :data="data" :props="{ value: 'label', children: 'children' }"
                    filterable style="width: 240px" clearable />
                </el-form-item>

                <el-form-item label="传感器类型" prop="sensorType">
                  <el-input v-model="form.sensorType" placeholder="请输入传感器类型" />
                </el-form-item> -->
        <el-col :span="24">
            <el-form-item label="影像文件" prop="sourcefilepath"   class="upload-item">
                <el-upload
                        ref="uploadRef"
                        :limit="1"
                        accept=".zip, .tar.gz, .rar"
                        :headers="upload.headers"
                        :action="upload.url"
                        :disabled="upload.isUploading"
                        :on-change="handleChange"
                        :on-progress="handleFileUploadProgress"
                        :on-success="handleFileSuccess"
                        :on-exceed="handleExceed"
                        :auto-upload="true"
                        :file-list="fileList"
                        :list-type="listType"
                        drag
                        class="upload-dragger"
                        style="min-height: 150px; width: 300px; padding: 20px;"
                >
                    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">请上传zip、tar、rar4类型压缩包文件，<br/>文件大小最大不超过5G</div>
                </el-upload>
                <p class="upload-info" style="color: red;">注：文件压缩较慢，请耐心等待。</p>
                <!--<p class="upload-info">文件压缩成功后，会自动上传到我的数据中。</p>-->
            </el-form-item>
        </el-col>

        <el-button style="margin-left: 200px" type="success" @click="handleAdd()">解压并上传</el-button>
      </el-form>
    </el-dialog>

    <button type="button" class="sidebar-toggle" :class="{ collapsed: !showText }"
      @click="toggleTextVisibility()" :style="`left:${realLeft}; `">
      <span>{{ showText ? '‹' : '›' }}</span>
    </button>
    <!-- <div class="map-status" v-show="showText">
      <strong>当前检索：</strong>{{ querySummary }}
    </div> -->
    <baseMap ref="mapDom" :style="`width:${realWidth};`" @getValue="getSonValue" @getSonValue1="getSonValue1"
      @getSonValue2="getSonValue2" @gave-lat-lng="handleAreaDrawn" />

    <!-- 右侧边栏：搜索结果 -->
    <div class="right-sidebar" v-show="showSearch">
      <div class="right-sidebar-header">
        <div class="right-sidebar-title">检索结果</div>
        <div class="right-sidebar-subtitle">共找到 {{ total }} 条数据</div>
      </div>
      
      <div class="right-sidebar-content">
        <div v-for="item in satelliteData" :key="item.id" class="result-item">
          <div class="result-item-header">
            <input type="checkbox" class="result-checkbox" @click="imageshow(item)" :ref="`checkbox_${item.id}`"
              :value="item.id" v-model="checkboxStatus[item.id]">
            <span class="result-item-title">{{ item.satelliteType }}_{{ item.region }}_{{ formatCollectTime(item.collectTime) }}</span>
          </div>
          <div class="result-item-body" @dblclick="navigateToCenter(item)" @mouseover="onMouseOver(item)" @mouseleave="onMouseLeave(item)">
            <img :src="item.image" class="result-item-img" @click="viewDetails(item)">
            <div class="result-item-info">
              <div class="info-row">
                <span class="info-label">卫星:</span>
                <span class="info-value">{{ item.satelliteType }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">传感器:</span>
                <span class="info-value">{{ item.sensorType }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">采集时间:</span>
                <span class="info-value">{{ item.collectTime }}</span>
              </div>
            </div>
          </div>
          <div class="result-item-actions">
            <el-button size="small" @click="handleViewImage(item)">预览</el-button>
            <el-button size="small" @click="viewDetails(item)">详情</el-button>
            <el-button size="small" @click="openThematicMap(item)">专题图</el-button>
          </div>
        </div>
      </div>

      <div class="right-sidebar-footer">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 40]"
          size="small"
          :disabled="disabled"
          :background="false"
          layout="prev, pager, next,total, sizes"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <div class="footer-actions">
          <el-button :disabled="!isAnalyzeAllowed" @click="handleAddDate()">加入我的数据</el-button>
          <el-button :disabled="!isAnalyzeAllowed" type="primary" @click="ProcessingAnalysis()">处理分析</el-button>
        </div>
      </div>
    </div>

    <!-- 右侧边栏收起按钮 -->
    <button type="button" class="right-sidebar-toggle" :class="{ collapsed: !showSearch }"
      @click="toggleSearchVisibility()" :style="`right:${showSearch ? '320px' : '0px'};`">
      <span>{{ showSearch ? '‹' : '›' }}</span>
    </button>

    <!--卫星数据详情框-->
    <el-dialog :title="title" v-model="satelliteDetails" width="800px" height="600px" append-to-body
      class="satellite-detail-dialog">
      <div class="satellite-detail">
        <!-- 图片区域 -->
        <div class="satellite-detail__image">
          <div class="satellite-detail__title">
            {{ selectedItem ? selectedItem.satelliteType + "_" + selectedItem.region + "_" +
            formatCollectTime(selectedItem.collectTime) : '' }}
          </div>
          <img :src="selectedItem ? selectedItem.image : ''" alt="" class="satellite-detail__img">
        </div>
        <!-- 数据展示区域 -->
        <div class="satellite-detail__info">
          <p v-if="selectedItem"><strong>地址:</strong> {{ selectedItem.province }} {{ selectedItem.city }}</p>
          <p v-if="selectedItem"><strong>卫星类型:</strong> {{ selectedItem.satelliteType }}</p>
          <p v-if="selectedItem"><strong>传感器类型:</strong> {{ selectedItem.sensorType }}</p>
          <p v-if="selectedItem"><strong>采集时间:</strong> {{ selectedItem.collectTime }}</p>
          <p v-if="selectedItem"><strong>景path:</strong> {{ selectedItem.viewPath }}</p>
          <p v-if="selectedItem"><strong>景row:</strong> {{ selectedItem.viewRow }}</p>
          <p v-if="selectedItem"><strong>星下点path:</strong> {{ selectedItem.starPath }}</p>
          <p v-if="selectedItem"><strong>星下点row:</strong> {{ selectedItem.starRow }}</p>
          <p v-if="selectedItem" class="spacing"><strong>左上角经/纬度:</strong> {{ selectedItem.leftupLongitude + "°E" + "," + selectedItem.leftupLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>左下角经/纬度:</strong> {{ selectedItem.leftdownLongitude + "°E" + "," + selectedItem.leftdownLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>右上角经/纬度:</strong> {{ selectedItem.rightupLongitude + "°E" + "," + selectedItem.rightupLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>右下角经/纬度:</strong> {{ selectedItem.rightdownLongitude + "°E" + "," + selectedItem.rightdownLatitude + "°N"}}</p>
        </div>
      </div>
      <!-- 按钮区域 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button :disabled="!isDownloadAllowed" @click="downloadImage">下载本幅影像</el-button>
          <el-button type="primary" :disabled="!isCollectAllowed" @click="collectImage(selectedItem)">收藏本幅影像</el-button>
          <el-button type="success" :disabled="!isAnalyzeAllowed"
            @click="ProcessingAnalysis(selectedItem)">处理分析本幅影像</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 专题图设置弹出框 -->
    <el-dialog v-model="HandleAnalytics" width="800px" append-to-body class="thematic-map-dialog">
      <template #title>
        <span class="dialog-title">专题图</span>
      </template>
      <div class="container">
        <div class="form-container">
          <el-form :model="form" label-width="120px">
            <div class="form-label"><span class="required">*</span> 专题图标题</div>
            <el-input style="width: 50%" v-model="formList.title" placeholder="请输入专题图标题" />
            <div class="form-label" style="width: 50%;padding-top: 10px;"><span class="required">*</span> 专题图类型</div>
            <el-select v-model="formList.type" style="width: 50%">
              <el-option v-for="item in imageTypeData" :key="item.id" :value="item.categoryCode"
                :label="item.imageCategory">
              </el-option>
            </el-select>
            <div class="form-label" style="padding-top: 10px;">专题图备注信息</div>
            <el-input type="textarea" v-model="formList.exInfo" style="resize: none;width:80%;" name="info" rows="5"
              placeholder="请输入备注信息"></el-input>
          </el-form>
        </div>
        <div class="side-container">
          <div class="zhuantitu-img">
            <img :id="formList.type" :src="getImageUrl(formList.type)" class="thematic-preview-img" alt="Type1" />
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="makeButtton">制 作</el-button>
        </div>
      </template>
    </el-dialog>

    <!--地图绘制弹出对话框-->
    <el-dialog v-model="dialogVisible" title="提示" width="300px"
      :style="{ minHeight: '200px', top: '50%', transform: 'translateY(-50%)' }">
      <el-divider class="divider-custom"></el-divider>
      <div class="instruction">
        <span>
          <span class="red-text">单击</span>地图开始绘制查询区域；如需退出或重画，请点击<span class="dark-text">清除区域</span>按钮。
        </span>
      </div>
      <div class="button-container">
        <span class="dialog-footer">
          <el-button type="primary" @click="confirmDrawing">确定</el-button>
        </span>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, nextTick } from "vue";
import axios from "axios";
import baseMap from "@/components/Map/Map.vue";
import { getMenuInfoList } from "@/api/Data/DataInfo.js";
import { addInfo,addDateInfo,addInfo1 } from "@/api/record/info";
import { aiInfo } from "@/api/ai/info";
import { listSatellite, getSatellite, listSatellite1 } from "@/api/map/satellite.js";
import { ElMessageBox, ElMessage } from "element-plus";
import { loadAllParams } from "@/api/page.js";
import { addCollectionInfo,listInfo } from "@/api/collection/collection.js";
import { imageTypeGet, addMake, imageGetByCode } from "@/api/home/home";
import { getToken } from '@/utils/auth'
import { da } from "element-plus/es/locales.mjs";
import {
    generateGTMOrder
} from "@/api/order/orderManagement";

const isDownloadAllowed = ref(false);
const isCollectAllowed = ref(false);
const isAnalyzeAllowed = ref(false);
const { proxy } = getCurrentInstance();
const router = useRouter();
const wode = () => {
  router.push("/login");
};
const tp = () => {};
const value = ref([]);
const total = ref(0);
const showText = ref(true);
const showSearch = ref(false);
const activePanel = ref('search');
const radio1 = ref("2");
const isDisabled = ref(true);
const edDisabled = ref(false);
const data = ref([]);
const elTreeRef = ref(null);
const dialogVisible = ref(false);
const open = ref(false);
const options = ref([]);
const selectedProvince = ref("");
const selectedCity = ref("");
const checked = ref(false);
const keepResultPrompted = ref(false);
const check = ref(false);
const isShowPage = ref(true);
const dataUpLoad = reactive({
  form: {},
});
const imageTypeData = ref([]);
const checkboxStatus = ref({}); // 用于存储复选框的状态
const { form } = toRefs(dataUpLoad);
const formList = reactive({
  form: {}
});
const dateRanges = ref([
    { type: '', label: '近三天', value: '1' },
    { type: 'success', label: '近一周', value: '2' },
    { type: 'info', label: '近一月', value: '3' },
    { type: 'danger', label: '近半年', value: '4' },
    { type: 'warning', label: '近一年', value: '5' }
]);
const selectedQuickRange = ref('');
const datePickerKey = ref(0);
const drawMode = ref('');
const rules = reactive({
  imageName: [
    { required: true, message: '请输入文件名称', trigger: 'blur' }
  ],
  image: [
    { required: true, message: '请上传影像', trigger: 'change' }
  ],
  // collectTime: [
  //   { required: true, message: '请选择采集时间', trigger: 'change' }
  // ],
  // province: [
  //   { required: true, message: '请选择省份', trigger: 'change' }
  // ],
  // city: [
  //   { required: true, message: '请选择城市', trigger: 'change' }
  // ],
  satelliteType: [
    { required: true, message: '请选择卫星类型', trigger: 'change' }
  ],
  sensorType: [
    { required: true, message: '请输入传感器类型', trigger: 'blur' }
  ],
  sourcefilepath: [
    { required: true, message: "卫星tiff源文件不能为空", trigger: "blur" }
],
})


const uploadRef = ref(null);
const upload = reactive({
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: import.meta.env.VITE_APP_BASE_API + "/common/upload",
        percent: 0,
        total: 0
    });
const fileList = ref([]);
const listType = ref("text");

   /**文件上传中处理 */
   const handleFileUploadProgress = (event, file, fileList) => {
        console.log("上传中")
        upload.isUploading = true;
        upload.percent = event.percent
        upload.total = event.total
    };
    /** 文件上传成功处理 */
    const handleFileSuccess = (response, file, fileList) => {
        console.log("上传成功")
        upload.isUploading = false;
        form.value.sourcefilepath = response.url
        form.value.sourcefilename = response.originalFilename
        console.log(form.value);
    };
    // 上传文件列表的改变事件
    const handleChange = (file, fileList1) => {
        console.log("上传文件改变")
        fileList.value = fileList1;
        console.log(fileList.value);
    };
    // 重新上传提醒
    function handleExceed(){
        proxy.$message.warning("已存在卫星tiff源文件，请删除后重新上传");
    }

onMounted(() => {
  // const token = localStorage.getItem('token');
  if (getToken()) {
    isDownloadAllowed.value = true;
    isCollectAllowed.value = true;
    isAnalyzeAllowed.value = true;
  } else {
    isDownloadAllowed.value = false;
    isCollectAllowed.value = false;
    isAnalyzeAllowed.value = false;
  }
});

function disabledDate(time) {
  return time.getTime() > Date.now(); // 禁用当前时间之后的时间
}

onMounted(() => {
  const today = new Date();
  // 设置开始时间为当前月的第一天
  const startDate = new Date(today.getFullYear(), today.getMonth(), 1);
  // 设置结束时间为今天
  const endDate = new Date();

  dateRange.value = [startDate, endDate];
});

// 根据选中的类型返回图片 URL
const getImageUrl = (typeCode) => {
    if (!typeCode) return ""; // 没有选择时返回空字符串
    return imageTypeData.value.find((item) => item.categoryCode === typeCode)?.image || "";
};


//存储从接口获取的卫星数据
const satelliteData = ref();

const toggleTextVisibility = () => {
  showText.value = !showText.value;
  // showSearch.value = true;
  // isShowPage.value = false;
  nextTick(() => {
    if (mapDom.value && mapDom.value.resizeMap) {
      mapDom.value.resizeMap();
    }
  });
};

const toggleSearchVisibility = () => {
  showSearch.value = !showSearch.value;
  nextTick(() => {
    if (mapDom.value && mapDom.value.resizeMap) {
      mapDom.value.resizeMap();
    }
  });
};

const handleRadioChange = (value) => {
  if (value == "1") {
    isDisabled.value = false;
    edDisabled.value = true;
  } else if (value == "2") {
    selectedProvince.value = "";
    selectedCity.value = "";
    isDisabled.value = true;
    edDisabled.value = false;
  }
};

const goBack = () => {
  /* getMenuInfoList().then((res) => {
    if (res.code == 200) {
      data.value = res.data[0];
    }
  }); */
  if (!checked.value) {
      mapDom.value.clearAllLayers();
      // 移除图片
      mapDom.value.removeAllImage();
      satelliteData.value.forEach((item)=>{
          checkboxStatus.value[item.id] = false
      })
      fileList.value = [];
  }
  showSearch.value = false;
  isShowPage.value = true;
}
import useTagsViewstore from '@/store/modules/tagsView'
/* 查询结果弹出框 */
const openSelect = () => {
  if (!checked.value) {
    ElMessageBox.alert(
      "已开启查询结果保留，将保留查询结果直到网页被关闭；点击查询结果栏【左上角后退按钮】可返回查询条件选择",
      "提示",
      {
        confirmButtonText: "确定",
        customClass: "message-box",
        dangerouslyUseHTMLString: true,
      }
    ).then(() => {
      useTagsViewstore().addCache('Home')
      checked.value = true;
    });
  } else if (checked.value) {
    useTagsViewstore().addCache('pro')
    check.value = false;
  }
};

const realWidth = computed(() => {
  let width = "100vw";
  if (showText.value) {
    width = "calc(100vw - 400px)";
  }
  if (showSearch.value) {
    width = showText.value ? "calc(100vw - 720px)" : "calc(100vw - 320px)";
  }
  return width;
});

const realLeft = computed(() => (showText.value ? "400px" : "0vw"));

const querySummary = computed(() => {
  const typeText = selectedSatelliteTypes.value.length ? `${selectedSatelliteTypes.value.length}类数据` : '未选择数据类型';
  const areaTypeMap = {
      polygon: '多边形绘制区域',
      rectangle: '矩形绘制区域',
      vector: '矢量图上传区域'
  };
  const areaText = areaTypeMap[drawMode.value] || '未设置检索区域';
  const timeText = dateRange.value && dateRange.value.length === 2
      ? `${formatDate(dateRange.value[0])} 至 ${formatDate(dateRange.value[1])}`
      : '未设置时间';
  return `${typeText} · ${areaText} · ${timeText}`;
});

const switchPanel = (panel) => {
  activePanel.value = panel;
  if (panel === 'upload') {
    cancelDrawing();
  }
};

const syncSelectedSatelliteTypesFromTree = () => {
  if (!elTreeRef.value) return;
  const checkedNodes = elTreeRef.value.getCheckedNodes(true, false) || [];
  selectedSatelliteTypes.value = Array.from(new Set(
      checkedNodes
          .map(node => node.label)
          .filter(value => value !== undefined && value !== null && value !== '')
  ));
};

const menuProps = {
  children: "children",
  label: "label",
};

/* 重置上传对话框 */
function reset() {
  form.value = {};
  selectedProvince.value = "";
  selectedCity.value = "";
}

// 检索区域省市选择方法
const parseCitiesData = (data) => {
  const provinces = data.map((item) => ({
    value: item.label,
    label: item.label,
    children: item.children.map((child) => ({
      value: child.label,
      label: child.label,
    })),
  }));
  options.value = provinces;
};

// 计算属性，动态生成城市选项
const selectedProvinceChildren = computed(() => {
  if (!selectedProvince.value) return [];
  const province = options.value.find(
    (opt) => opt.value === selectedProvince.value
  );
  return province?.children || [];
});

// 组件挂载后获取省市数据
onMounted(async () => {
  try {
    // const response = await axios.get("/src/assets/json/city.json");
    const response = await axios.get("/resData/city.json");
    const citiesData = response.data;
    parseCitiesData(citiesData);
  } catch (error) {
    console.error("Error fetching city data:", error);
  }
});

// 获取专题图类型下拉框
onMounted(async () => {
  imageTypeData.value = await imageTypeGet();
  if (imageTypeData.value.length > 0) {
    formList.type = imageTypeData.value[0].categoryCode;
  }
});

// 监听省份选择变化
watch(selectedProvince, (newProvince) => {
  form.value.province = newProvince; // 存储省份名称
  form.value.provinceCode = options.value.find(
    (opt) => opt.value === newProvince
  )?.value; // 存储省份编码
  // 清空城市选择
  selectedCity.value = "";
  form.value.city = "";
  form.value.cityCode = "";
});

// 监听城市选择变化
watch(selectedCity, (newCity) => {
  form.value.city = newCity; // 存储城市名称
  form.value.cityCode = selectedProvinceChildren.value.find(
    (city) => city.value === newCity
  )?.value; // 存储城市编码
});

const mapDom = ref(null);

//开始绘制按钮（弹窗）
const confirm = () => {
  dialogVisible.value = true;
};

//弹窗确定按钮（开始绘制检索区域）
const confirmDrawing = () => {
  dialogVisible.value = false;
  // 启用地图绘图功能,调用Map.vue中的initDrawing方法
  drawMode.value = 'polygon';
  mapDom.value.clearAllLayers();
  mapDom.value.drawPolygon();
  console.log("确定绘制");
};

// 开始多边形绘制
const startDrawPolygon = () => {
  if (!mapDom.value) return;
  drawMode.value = 'polygon';
  keepResultPrompted.value = false;
  mapDom.value.clearAllLayers();
  mapDom.value.drawPolygon();
  ElMessage.success('已进入多边形绘制模式，请在地图上单击绘制检索区域');
};

// 开始矩形绘制
const startDrawRectangle = () => {
  if (!mapDom.value) return;
  drawMode.value = 'rectangle';
  keepResultPrompted.value = false;
  mapDom.value.clearAllLayers();
  mapDom.value.drawRectangle();
  ElMessage.success('已进入矩形绘制模式，请在地图上拖拽生成检索区域');
};

const askKeepQueryResult = () => {
  if (keepResultPrompted.value || checked.value) return;
  keepResultPrompted.value = true;
  ElMessageBox.confirm(
      '检索区域已设置，是否保持后续查询结果？开启后返回条件页不会清除当前结果。',
      '保持查询结果',
      {
        confirmButtonText: '保持',
        cancelButtonText: '暂不保持',
        type: 'info',
        customClass: 'my-custom-message-box'
      }
  ).then(() => {
      checked.value = true;
      useTagsViewstore().addCache('Home');
      ElMessage.success('已开启查询结果保留');
  }).catch(() => {});
};

const handleAreaDrawn = () => {
  askKeepQueryResult();
};

// 上传GeoJSON矢量范围
const handleVectorFileChange = (uploadFile) => {
  const rawFile = uploadFile?.raw;
  if (!rawFile) return;
  const reader = new FileReader();
  reader.onload = (event) => {
    try {
      const geojson = JSON.parse(event.target.result);
      mapDom.value.clearAllLayers();
      const loaded = mapDom.value.addGeoJsonLayer(geojson);
      if (loaded) {
        drawMode.value = 'vector';
        ElMessage.success('矢量范围已加载，可直接作为检索区域');
        askKeepQueryResult();
      } else {
        ElMessage.error('未识别到有效的Polygon/MultiPolygon矢量面，请检查文件内容');
      }
    } catch (error) {
      console.error('GeoJSON解析失败:', error);
      ElMessage.error('矢量文件解析失败，请上传标准GeoJSON/JSON文件');
    }
  };
  reader.onerror = () => {
    ElMessage.error('矢量文件读取失败，请重新选择文件');
  };
  reader.readAsText(rawFile, 'UTF-8');
};

//清除所绘制的检索区域
const cancelDrawing = () => {
  // 清除所有绘制的图形
  mapDom.value.clearAllLayers();
  drawMode.value = '';
  keepResultPrompted.value = false;
  console.log("取消绘制");
};

//控制卫星数据详情页开关状态
const satelliteDetails = ref(false);

//控制专题图弹出框开关状态
const HandleAnalytics = ref(false);

//把信息数据传到专题图中
const thematicMapDta = ref(null);

//存储检索结果列表所选中的单条卫星数据
const selectedItem = ref(null);

//查看卫星数据详情页方法
const viewDetails = (item) => {
  // 将点击的item设置给selectedItem
  selectedItem.value = item;
  // 打开对话框
  satelliteDetails.value = true;
};

const openThematicMap = (item) => {
  thematicMapDta.value = item;
  formList.id = item.id
  HandleAnalytics.value = true;
};


// 辅助函数用于统一管理图片的显示与隐藏
const toggleImageDisplay = (item, show) => {
  if (show) {
    // 添加图片
    mapDom.value.addRotatedImageWithPolygon(item, item.image);
    // 飞点
    const centerPoint = calculateCenterPoint(item);
    navigateToCenterPoint(centerPoint);
  } else {
    // 移除图片
    mapDom.value.removeImage(item.id);
  }
  // 更新状态
  item.isShow = show;
  checkboxStatus.value[item.id] = show;
};

// 方法用于切换图像的显示状态
const handleViewImage = (item) => {
  toggleImageDisplay(item, !item.isShow);
};

// 双击数据条方法
const navigateToCenter = (item) => {
  const centerPoint = calculateCenterPoint(item);
  navigateToCenterPoint(centerPoint);

  // 如果当前未显示，则设置为显示并添加图片
  if (!item.isShow) {
    toggleImageDisplay(item, true);
  }
};

// 勾选复选框方法
const imageshow = (item) => {
  toggleImageDisplay(item, !item.isShow);
};


// 记录当前活跃的多边形ID
let activeItemId = null;

// 鼠标悬停让多边形颜色改变
const onMouseOver = (item) => {
  // 如果当前已经有其他多边形处于激活状态，则恢复其颜色
  if (activeItemId !== null && activeItemId !== item.id) {
    mapDom.value.changePolygonColor(activeItemId, "#1E90FF","#1E90FF");
  }
  // 设置当前悬停的多边形为激活状态，并改变颜色
  activeItemId = item.id;
  mapDom.value.changePolygonColor(item.id, "#FF0000","#1E90FF");
};

// 鼠标悬停离开指定位置时
const onMouseLeave = () => {
  // 当前多边形离开悬停状态，直到鼠标悬停到下一个多边形才恢复颜色
  if (activeItemId !== null) {
    mapDom.value.changePolygonColor(activeItemId, "#FF0000","#1E90FF");
  }
};

// 单击图片改变多边形框颜色
const getSonValue2 = (polygonId) => {
  if (activeItemId !== null) {
    // 如果有其他多边形处于高亮状态，先恢复其颜色
    mapDom.value.changePolygonColor(activeItemId, "#1E90FF","#1E90FF");
  }
  // 更新当前高亮的多边形ID
  activeItemId = polygonId;
  // 改变点击的多边形为红色
  mapDom.value.changePolygonColor(polygonId, "#FF0000","#1E90FF");
};


//双击图片事件
const getSonValue = (id) => {
  satelliteData.value.forEach((item)=>{
    if(item.id ==id){
      item.isShow = !item.isShow;
      checkboxStatus.value[item.id] = !checkboxStatus.value[item.id];
    }
  })
}

//右键图片显示详情页
const getSonValue1 = (item) => {
  viewDetails(item);    //直接传递数据项
}

// 当分页大小改变时触发
const handleSizeChange = (newSize) => {
  queryParams.pageSize = newSize;
  search();
};

// 当当前页码改变时触发
const handleCurrentChange = (newPage) => {
  queryParams.currentPage4 = newPage;
  search();
};


    // 构建查询参数对象
  const queryParams = reactive({
    pageNum: 1, // 当前页码
    pageSize: 10, // 每页显示条目数
    province: null, // 用户选择的省份
    city: null, // 用户选择的城市
    startCollectTime: null, // 时间范围的起始日期
    endCollectTime: null , // 时间范围的结束日期
    satelliteTypes: null , // 用户选择的卫星类型
    points: [] // 自定义经纬度范围
  });
  // 点击时间标签
  const handleTagClick = (val) => {
      selectedQuickRange.value = val;
      const tempDate = new Date();
      // 设置结束时间为今天
      const endDate = new Date();
      if (val == '1') {// 3天前日期
          tempDate.setDate(tempDate.getDate() - 2)
      } else if (val == '2') {// 7天前日期
          tempDate.setDate(tempDate.getDate() - 6)
      } else if (val == '3') {// 一月前日期
          tempDate.setDate(tempDate.getDate() - 30)
      } else if (val == '4') {// 半年前日期
          tempDate.setDate(tempDate.getDate() - 183)
      } else if (val == '5') {// 一年前日期
          tempDate.setDate(tempDate.getDate() - 365)
      }
      const startDate = new Date(tempDate.getFullYear(), tempDate.getMonth(), tempDate.getDate());
      const normalizedEndDate = new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
      dateRange.value = [startDate, normalizedEndDate];
      datePickerKey.value += 1;
  }

const resolveFirstLatLngRing = (latlngs) => {
    let ring = latlngs;
    while (Array.isArray(ring) && ring.length > 0 && !('lat' in ring[0]) && !('lng' in ring[0])) {
        ring = ring[0];
    }
    return Array.isArray(ring) ? ring : [];
}

// 检索
const search = () => {
    syncSelectedSatelliteTypesFromTree();
    if (selectedSatelliteTypes.value.length === 0) {
      return ElMessageBox.alert('请选择数据类型！', '提示', {
        confirmButtonText: '确定',
        customClass: 'my-custom-message-box'
      });
    }
    queryParams.satelliteTypes = selectedSatelliteTypes.value // 用户选择的卫星类型

    queryParams.province = null
    queryParams.city = null
    const drawnPolygon = getDrawnPolygon();
    if (drawnPolygon == null) {
        return ElMessageBox.alert('请先绘制矩形/多边形检索区域，或上传GeoJSON矢量范围！', '提示', {
            confirmButtonText: '确定',
            customClass: 'my-custom-message-box'
        });
    }
    queryParams.points = []
    const latlngs = drawnPolygon.getLatLngs();
    const firstRing = resolveFirstLatLngRing(latlngs);
    if (firstRing.length < 3) {
        return ElMessageBox.alert('当前检索区域无效，请重新绘制矩形/多边形或上传面状GeoJSON矢量范围！', '提示', {
            confirmButtonText: '确定',
            customClass: 'my-custom-message-box'
        });
    }
    firstRing.forEach(item => {
        queryParams.points.push(item.lat+','+item.lng)
    })
    if (dateRange.value.length === 0) {
        return ElMessageBox.alert('请输入有效的时间范围！', '提示', {
            confirmButtonText: '确定',
            customClass: 'my-custom-message-box'
        });
    }
    // 将 Date 对象转换为 "yyyy-MM-dd HH:mm:ss" 格式的字符串
    queryParams.startCollectTime = formatDate(dateRange.value[0]) // 时间范围的起始日期
    queryParams.endCollectTime = formatDate(dateRange.value[1]) // 时间范围的结束日期
    console.log(queryParams)
    // 清除历史查询记录
    mapDom.value.clearAllLayers();
    // 移除图片
    mapDom.value.removeAllImage();
    if (satelliteData.value != undefined) {
        satelliteData.value.forEach((item)=>{
            checkboxStatus.value[item.id] = false
        })
    }
    fileList.value = [];
    // 请求
    listSatellite1(queryParams).then(response => {

      total.value = response.total;

      let filteredData = [];
      filteredData = response.rows;
      satelliteData.value = response.rows;

      // 检查是否有数据
      if (filteredData.length > 0) {// 有数据
        // 默认图像不显示
        satelliteData.value.forEach((item) => {
            item.isShow = false
        })
        // 跳转到第一条数据的中心点
        const firstItem = filteredData[0];
        const centerPoint = calculateCenterPoint(firstItem);
        navigateToCenterPoint(centerPoint);
        cancelDrawing();

        // 遍历所有数据，创建多边形框并显示图片
        filteredData.forEach(item => {
          const imageUrl = item.image;
          const points = {
            TopLeftLatitude: item.leftupLatitude,
            TopLeftLongitude: item.leftupLongitude,
            TopRightLatitude: item.rightupLatitude,
            TopRightLongitude: item.rightupLongitude,
            BottomRightLatitude: item.rightdownLatitude,
            BottomRightLongitude: item.rightdownLongitude,
            BottomLeftLatitude: item.leftdownLatitude,
            BottomLeftLongitude: item.leftdownLongitude
          };

          mapDom.value.addPolygon(points, imageUrl, item.id,item);
          // index
          showSearch.value = true;
          isShowPage.value = false;
        });
      } else {
        //检索后没有数据
        return ElMessageBox.alert('未查询到相关数据，请检查检索条件', '提示', {
          confirmButtonText: '确定',
          customClass: 'my-custom-message-box'
        });
      }
    });
};

function formatDate(date) {
    const pad = (num) => (num < 10 ? '0' + num : num);
    const yyyy = date.getFullYear();
    const mm = pad(date.getMonth() + 1); // getMonth() 返回的月份是从 0 开始的
    const dd = pad(date.getDate());
    // const hh = pad(date.getHours());
    // const min = pad(date.getMinutes());
    // const ss = pad(date.getSeconds());
    // return `${yyyy}-${mm}-${dd} ${hh}:${min}:${ss}`;
    return `${yyyy}-${mm}-${dd}`;
};

function formatLastDate(date) {
    let y = date.getFullYear()
    let MM = date.getMonth() + 1
    MM = MM < 10 ? ('0' + MM) : MM
    let d = date.getDate()
    d = d < 10 ? ('0' + d) : d
    let lastDate = y + '-' + MM + '-' + d + ' 23:59:59'
    return lastDate;
}

const setValueNull = () => {
  selectedProvince.value = null;
  selectedCity.value = null;


}


//  `response` 是从 `listSatellite` 接口获得的数据,根据四个顶角经纬度来计算中心点
const calculateCenterPoint = (item)=> {
  // 提取四对经纬度
  const leftdown = [item.leftdownLatitude, item.leftdownLongitude];
  const leftup = [item.leftupLatitude, item.leftupLongitude];
  const rightdown = [item.rightdownLatitude, item.rightdownLongitude];
  const rightup = [item.rightupLatitude, item.rightupLongitude];

  // 计算经纬度的平均值
  const latSum = (leftdown[0] + leftup[0] + rightdown[0] + rightup[0]) / 4;
  const lonSum = (leftdown[1] + leftup[1] + rightdown[1] + rightup[1]) / 4;

  return { lat: latSum, lng: lonSum };
}


// 判断一个点是否在多边形内(算法)：射线交叉法
function isPointInPolygon(point, polygon) {
  var x = point.lng, y = point.lat;
  var n = polygon.length;
  var inside = false;

  var p1x = polygon[0].lng, p1y = polygon[0].lat;
  for (var i = 1; i <= n; i++) {
    var p2x = polygon[i % n].lng, p2y = polygon[i % n].lat;
    if (y > Math.min(p1y, p2y)) {
      if (y <= Math.max(p1y, p2y)) {
        if (x <= Math.max(p1x, p2x)) {
          if (p1y != p2y) {
            var xints = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x;
          }
          if (p1x == p2x || x <= xints) {
            inside = !inside;
          }
        }
      }
    }
    p1x = p2x;
    p1y = p2y;
  }

  return inside;
}

// 获取绘制的多边形
function getDrawnPolygon() {
   // 使用 `mapDom.value` 来访问地图组件的实例
  return mapDom.value.getDrawnPolygon();
}

// 跳转到指定中心点的方法
const navigateToCenterPoint = (centerPoint) =>{
  console.log(`跳转到中心点: ${centerPoint.lat}, ${centerPoint.lng}`);
  // 这里可以添加实际的跳转逻辑
  mapDom.value.goToLatLng(centerPoint.lat, centerPoint.lng);
}


function normalizeSatelliteType(type) {
  if (!type) return type;
  return type.replace(/^GF-0(\d)$/, 'GF$1');
}

function getMenuList() {
  getMenuInfoList().then((res) => {
    if (res.code == 200) {
      const menuData = res.data;
      menuData.forEach(item => {
        if (item.children) {
          item.children.forEach(child => {
            if (child.value) {
              child.value = normalizeSatelliteType(child.value);
            }
            if (child.dictValue) {
              child.dictValue = normalizeSatelliteType(child.dictValue);
            }
            if (child.label) {
              child.label = normalizeSatelliteType(child.label);
            }
          });
        }
      });
      data.value = menuData;
    }
  });
}

// 收藏影集
function collectImage(data){
  addCollectionInfo(data).then((response) => {
    proxy.$modal.msgSuccess("收藏成功");
    open.value = false;
    getList();
  })
}
// 收藏成功校验
/*const CollectionList = ref([]);
function getCollection(){
  listInfo().then((response) => {
    return response;
  }).then((data) => {
    CollectionList.value.push({
      data,
    });
  })
}*/

// 用户选择处理
function handleNodeClick(data, checked, indeterminate) {
  nextTick(() => {
    syncSelectedSatelliteTypesFromTree();
  });
  console.log('Selected Satellite Types:', selectedSatelliteTypes.value);
}

// 定义一个数组存储所有被选中的卫星类型标签
const selectedSatelliteTypes = ref([]);

const dateRange = ref([]);

// 处理时间格式,将日期格式从 yyyy-MM-dd hh:mm:ss 转换为 yyyy-MM-dd
function formatCollectTime(date) {
  const d = new Date(date);
  // 返回正确的日期字符串格式
  return `${d.getFullYear()}-${("0" + (d.getMonth() + 1)).slice(-2)}-${(
    "0" + d.getDate()
  ).slice(-2)}`;
}

function goToPath() {
  router.push({ name: "quickStart" });
}
/* 打开上传页面 */
function handleUpload() {
  reset();
  mapDom.value.clearAllLayers();
  // 移除图片
  mapDom.value.removeAllImage();
  if (satelliteData.value != null && satelliteData.value != undefined) {
    satelliteData.value.forEach((item)=>{
        checkboxStatus.value[item.id] = false
    })
  }
  fileList.value = [];
  if (getToken()) {
  open.value = true;
  }else{
    proxy.$modal.msgError("请先登录");
  }
}
/* 上传我的数据 */
//  function handleAdd() {
//    proxy.$refs["infoRef"].validate((valid) => {
//      if (valid) {
//        addInfo(form.value).then((res) => {
//          if (res.code == 200) {
//            proxy.$modal.msgSuccess("上传成功");
//            open.value = false;
//          }
//        });
//      }
//    }
//  )};
//  function handleAdd() {

//    proxy.$refs["infoRef"].validate((valid) => {
//      if (valid) {
//        addInfo1(form.value).then((res) => {
//          if (res.code == 200) {
//            proxy.$modal.msgSuccess("上传成功");
//            open.value = false;
//          }
//        });
//      }
//    }

//  )};
const sateInfo = ref({});
 import { ElLoading } from 'element-plus';
    function handleAdd() {
        proxy.$refs["infoRef"].validate((valid) => {
        if (valid) {
            // 创建一个 Loading 实例
            const loadingInstance = ElLoading.service({
                lock: true,       // 锁定屏幕，禁止滚动
                text: '上传中，预计1~2分钟，请耐心等待...',
                background: 'rgba(0, 0, 0, 0.7)'  // 背景颜色
            });

            addInfo1(form.value).then((res) => {
              if (res.code == 200) {
                  var sateId = res.data
                  if (sateId != null) {
                      // 关闭 Loading 实例
                      loadingInstance.close();
                      proxy.$modal.msgSuccess("上传成功");
                      open.value = false;
                      getSatellite(sateId).then(response => {
                          sateInfo.value = response.data
                          satelliteData.value = [sateInfo.value];
                          // 跳转到数据的中心点
                          const centerPoint = calculateCenterPoint(sateInfo.value);
                          navigateToCenterPoint(centerPoint);
                          cancelDrawing();

                          satelliteData.value.forEach(item => {
                              const imageUrl = item.image;
                              const points = {
                                  TopLeftLatitude: item.leftupLatitude,
                                  TopLeftLongitude: item.leftupLongitude,
                                  TopRightLatitude: item.rightupLatitude,
                                  TopRightLongitude: item.rightupLongitude,
                                  BottomRightLatitude: item.rightdownLatitude,
                                  BottomRightLongitude: item.rightdownLongitude,
                                  BottomLeftLatitude: item.leftdownLatitude,
                                  BottomLeftLongitude: item.leftdownLongitude
                              };

                              mapDom.value.addPolygon(points, imageUrl, item.id,item);
                              // index
                              showSearch.value = true;
                              isShowPage.value = false;
                          });
                      })
                  } else {
                      // 关闭 Loading 实例
                      loadingInstance.close();
                      proxy.$modal.msgSuccess("上传成功");
                      open.value = false;
                  }
              } else {
                  // 关闭 Loading 实例
                  loadingInstance.close();
                  proxy.$modal.msgError(res.msg);
                  open.value = false;
              }
            }).catch(() => {
                // 关闭 Loading 实例
                loadingInstance.close();
                open.value = false;
            });
        }
     }

    )};

/* 加入我的数据 */
function handleAddDate() {

    const selectedIds = Object.keys(checkboxStatus.value).filter(
      (id) => checkboxStatus.value[id]
    );
  if (selectedIds.length === 0) {
    ElMessageBox.alert(
      "未选择要收藏的数据，请正确勾选待收藏的数据。",
      "提示",
      {
        confirmButtonText: "确定",
        customClass: "message-box",
        dangerouslyUseHTMLString: true,
      }
    )
    return; // 终止函数执行
  }

  // 调用后端API保存选择的数据
  addDateInfo({ satelliteIds: selectedIds })
    .then((response) => {
      proxy.$modal.msgSuccess("添加成功");
    })
    .catch((error) => {
      // 可以在这里处理错误情况，比如提示错误信息
      proxy.$modal.msgError("添加失败，请重试");
    });
  }



//处理分析
function ProcessingAnalysis(data) {
  let selectedIds;

  if (data === undefined) {
    selectedIds = Object.keys(checkboxStatus.value).filter(id => checkboxStatus.value[id]);
    if (selectedIds.length === 0) {
      ElMessageBox.alert(
        "未选择要处理的数据，请正确勾选待处理的数据。",
        "提示",
        {
          confirmButtonText: "确定",
          customClass: "message-box",
          dangerouslyUseHTMLString: true,
        }
      )
      return;
    }
  } else if (Array.isArray(data.id)) {
    selectedIds = data.id;
  } else {
    selectedIds = [data.id]; // 假设data.id是一个单个ID
  }

  console.log("selectedIds:", selectedIds);

  aiInfo({ satelliteIds: selectedIds }).then((response) => {
    console.log(response.rows);
    const selectedData = response.rows;
    const rowData = JSON.stringify(selectedData);
    router.push({
      name: 'aiProcess',
      state: { rowData }
    });
  }).catch(error => {
    console.error("处理数据时出错:", error); // 记录错误以便调试
    // 根据应用程序的要求，可选地，向用户提供关于错误的反馈
  });
}

//下载本幅影像
function downloadImage() {
  const imageUrl = selectedItem.value.image;

  fetch(imageUrl)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.blob();
    })
    .then((blob) => {
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = "satellite_image.jpg";
      link.click();
      document.body.removeChild(link);
    })
    .catch((error) => {
      console.error("Error fetching image:", error);
    });
}

// 取消按钮
function cancel() {
  HandleAnalytics.value = false;
  reset();
}

// 制作按钮
async function makeButtton() {
  const imageList = await imageTypeGet();
  if (formList.title == null || formList.title == '' || formList.title == undefined) {
      proxy.$modal.msgError("专题图标题不能为空");
      return;
  }
  if (formList.type == null || formList.type == '' || formList.type == undefined) {
      proxy.$modal.msgError("专题图类型不能为空");
      return;
  }
  if (formList.type == '0') {
      proxy.$modal.msgError("请选择一个专题图类型");
      return;
  }
  generateGTMOrder(formList).then((response) => {
      if (response.code == 200) {
          proxy.$message.success(response.msg);
      } else {
          proxy.$message.error(response.msg);
      }
  });
}

getMenuList();
</script>
<style scoped lang="scss">
.home {
  display: flex;
  position: relative;
}
.app-content {
  width: 386px;
  min-width: 386px;
  height: calc(100vh - 50px);
  background-color: #1c1e21;
}
.images {
  width: 18px;
  height: 30px;
  position: absolute;
  z-index: 999999999999999;
  margin-top: 20px;
}

.panel-top {
  width: 100%;
  height: 55px;
  background-color: #282a2d;
  position: relative;
}

.panel-title {
  text-align: center;
  width: 100px;
  height: 25px;
  border-radius: 2px;
  position: absolute;
  top: 45%;
  left: 5%;
  color: #babcbf;
  background-color: #4e5153;
}

.app-type {
  color: #dbdde0;
  font-size: 14px;
  margin-left: 20px;
  margin-top: 20px;
}

.app-table {
  border: 1px solid #4e5153;
  width: 260px;
  height: 90px;
  margin-top: 20px;
  border-radius: 5px;
  display: flex;
  align-items: center;
}

.app-draw {
  border: 1px solid #4e5153;
  width: 258px;
  height: 50px;
  margin-top: 20px;
  border-radius: 5px;
  display: flex;
  flex-direction: row;
  align-content: stretch;
  align-items: center;
}

.top-button {
    // height: 300px;
    width: 386px;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
    position: fixed;
    bottom: 10px;
}

.app-quick {
  //margin-left: 30px;
  background-color: transparent;
  color: #fff;
  border: none;
  text-decoration: underline;
  font-size: 16px;
}

.dialog-footer-upload {
  text-align: left;
}

.instruction {
  text-align: left;
  margin-top: -0px; /* 减少与分割线的距离 */
}

.red-text {
  color: red;
}

.dark-text {
  color: #000;
  font-weight: bold;
}

.brown-text {
  color: brown;
}

.divider-custom {
  margin: 10px; /* 移除默认的上下边距 */
}

.button-container {
  display: flex;
  justify-content: right; /* 居中对齐 */
  margin-top: 40px; /* 向下移动的距离 */
}
.message-box {
  width: 200px !important;
}

.search-result {
  width: 386px;
  // min-width: 300px;
  height: calc(100vh - 50px);
  background-color: #1c1e21;
}

.search-text {
  height: 37px;
  display: flex;
  align-items: center;
  padding-left: 10px;
  margin-top: 25px;
  color: #fff;
  background-color: #2F3238;
  font-size: 14px;

}

.search-button {
      width: 386px;
      display: flex;
      // flex-direction: column;
      justify-content: flex-end;
      align-items: center;
      position: fixed;
      bottom: 0px;
      justify-content: space-evenly;
}

.search-box {
  background-color: #2F3238;
  width: 386px;
  height: auto; /* 移除固定高度 */
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex-wrap: wrap;
  padding-right: 40px;
}
.button-data {
  border: none;
  box-sizing: border-box;
  background-color: #3D444F;
  color: #fff
}

.el-scrollbar__wrap {
  overflow-x: hidden;
}

.el-scrollbar__bar.is-horizontal {
  display: none;
}

.data-list {
    box-sizing: border-box;
    height: calc(100% - 160px);
    overflow-x: hidden;
    overflow-y: auto;
    padding-right: 0;
    padding-top: 10px;
    width: 380px;
}

.dataInfoList {
    height: 100px;
    position: relative;
    width: 100%;
    transition: background-color 0.3s, color 0.3s; /* 添加过渡效果 */
}

.dataInfoList:hover {
    color: white; /* 鼠标悬停时文字颜色变为白色 */
    background-color: rgba(0, 0, 0, 0.7); /* 鼠标悬停时背景颜色变为半透明黑色 */
}

.input_label_info {
    /* width: 50px; */
    /* height: 20px;  */
    width: calc(100% - 10px);
    margin-left: 10px;
    /*margin-right: 10px;*/
    float: left;
}

.input_label {
    width: 100%;
    height: auto;
    align-content: center;
    color: whitesmoke;
    font-size: 15px;
    display: flex;  /* 使用 Flexbox 布局 */
  align-items: center;  /* 垂直居中对齐 */
  justify-content: space-between;  /* 水平方向上两端对齐 */
    /* float: left; */
    /* margin-top: 5px; */
    /* font-size: 16px; */
    /*background-color: cadetblue;*/
}

.right-buttons {
  display: flex;
}

.infor {
    width: auto;
    height: 60px;
    margin-top: 10px;
    /* background-color: rgb(165, 138, 240); */
    /* float: left; */
}

.info_details_1 {
    width: 110px;
    height: 50px;
    margin-left: 15px;
    float: left;
    color: whitesmoke;
    /* background-color: brown; */
}

.info_details_2 {
    width: 170px;
    height: 50px;
    margin-left: 15px;
    float: left;
    color: white;
    /* background-color: brown; */
}

.littleImg {
    width: 50px;
    height: 50px;
    float: left;
}

.chBoxStyle {
    zoom: 130%;
    vertical-align: middle;
    margin-right: 5px;
}

.container {
  display: flex; /* 启用Flexbox布局 */
  justify-content: space-between; /* 子元素之间的间隔 */
  align-items: center; /* 子元素在交叉轴上的对齐方式 */
  /* 根据需要添加其他样式，如padding, margin等 */
}

.form-container {
  /* 根据需要调整表单容器的样式 */
  flex: 10; /* 使得表单容器占据剩余空间 */
  padding: 20px; /* 示例：添加内边距 */
}

.side-container {
  /* 根据需要调整并排容器的样式 */
  width: 250px; /* 示例：固定宽度 */
  padding: 5px; /* 示例：添加内边距 */
}
.zhuantitu-img {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  margin-top: 10px;
}
.spacing {
       line-height: 1.6;
}
.upload-dragger {
  border: 2px dashed #409EFF; /* 更改为您想要的颜色 */
  border-radius: 4px;
  background-color: #f9f9f9; /* 背景色 */
  padding: 20px;
  text-align: center;
  transition: border-color 0.3s;
  min-height: 150px;
  width: 300px;
  padding: 20px;
}

.upload-dragger:hover {
  border-color: #66b1ff; /* 悬停时边框颜色 */
}

.upload-info {
  color: #606266; /* 信息文字颜色 */
  font-size: 14px; /* 信息文字大小 */
}

::v-deep .el-select-dropdown__item li{
  background-color:transparent !important;
}
// prev和next箭头的样式
::v-deep .el-pagination .btn-next,
::v-deep .el-pagination .btn-prev{
  background:transparent !important;
  background-color:transparent !important;
  color: #ffffff;
  margin: 0px;
}
// prev和next箭头disabled的样式
::v-deep .el-pagination button:disabled {
  background-color:transparent !important;
}
// 页码样式
::v-deep .el-pager li{
  background-color:transparent !important;
  color: #ffffff ;
}
// active的页码样式
::v-deep .el-pager li.is-active{
  border: 1px solid rgb(139, 201, 252);
}

// ::v-deep .el-select__wrapper.el-tooltip__trigger.el-tooltip__trigger{
//   background-color:#ffffff !important;
//   border: 1px solid rgb(0, 0, 0);
//   width: 150px;
// }
::v-deep .el-select__selected-item.el-select__placeholder{
  color: #606266;
}
::v-deep .el-select__selected-item{
  color: #ffffff
}
.el-pagination {
  padding-left: 5px;
  padding-top: 10px;
  padding-right: 10px;
  width: 380px;
}

.el-pagination ::v-deep .el-select__wrapper.el-tooltip__trigger.el-tooltip__trigger {
  background-color: #001529 !important;
  border: 1px solid #5169a5;
  width: 80px;
}

[data-theme="light"] .el-pagination ::v-deep .el-select__wrapper.el-tooltip__trigger.el-tooltip__trigger {
  background-color: #ffffff !important;
  border: 1px solid #94a3b8;
  width: 80px;
}
.el-pagination ::v-deep .el-select__selected-item.el-select__placeholder{
  color: #ffffff;
}

.el-pagination ::v-deep .el-pagination__total {
    color: #ffffff;
    font-weight: normal;
    margin-left: 5px;
}
.el-pagination ::v-deep .el-select {
    width: 100px;
}
.el-pagination__sizes{
    color: var(--el-text-color-regular);
    font-weight: normal;
    margin-left: var(--el-pagination-item-gap);
}
.el-pagination ::v-deep .el-pagination__sizes{
    color: #ffffff;
    font-weight: normal;
    margin-left: 5px;
}
.el-tag:hover {
    cursor: pointer;
}


/* UI优化：统一深色设计系统、分组卡片、按钮与状态层级 */
.app-content,
.search-result {
  background: linear-gradient(180deg, #111827 0%, #0f172a 100%);
  border-right: 1px solid rgba(148, 163, 184, 0.18);
  box-shadow: 12px 0 28px rgba(15, 23, 42, 0.22);
  overflow-y: auto;
}

.panel-top {
  height: auto;
  min-height: 92px;
  padding: 18px 20px 16px;
  background: radial-gradient(circle at top right, rgba(79, 125, 243, 0.28), transparent 55%), #172033;
  border-bottom: 1px solid rgba(148, 163, 184, 0.14);
}

.panel-title {
  position: static;
  width: auto;
  height: auto;
  background: transparent;
  color: #e5edf8;
  text-align: left;
  font-size: 20px;
  font-weight: 800;
  border-radius: 0;
}

.panel-subtitle {
  margin-top: 8px;
  color: #94a3b8;
  font-size: 13px;
  line-height: 1.6;
  text-align: left;
}

.workflow-hint {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin: 14px 16px 4px;
}

.workflow-step {
  padding: 8px 6px;
  border: 1px solid rgba(148, 163, 184, 0.16);
  border-radius: 999px;
  color: #94a3b8;
  background: rgba(31, 41, 55, 0.7);
  font-size: 12px;
  text-align: center;
}

.workflow-step.active {
  color: #dbeafe;
  background: rgba(79, 125, 243, 0.22);
  border-color: rgba(96, 165, 250, 0.45);
}

.mb8,
.app-content > .el-row,
.app-content > .el-radio-group {
  margin-left: 16px !important;
  margin-right: 16px !important;
}

.app-content > .el-row,
.app-content > .el-radio-group {
  display: block;
  margin-top: 14px;
  padding: 14px 12px;
  background: rgba(31, 41, 55, 0.72);
  border: 1px solid rgba(148, 163, 184, 0.14);
  border-radius: 14px;
}

.app-type {
  margin: 0 0 10px 0;
  color: #e5edf8;
  font-size: 14px;
  font-weight: 700;
  text-align: left;
}

.app-tabulation .app-type {
  margin: 0;
}

.app-table,
.app-draw {
  width: 100%;
  max-width: 306px;
  height: auto;
  min-height: 58px;
  margin-top: 8px;
  background: rgba(15, 23, 42, 0.7);
  border: 1px solid rgba(148, 163, 184, 0.18);
  border-radius: 12px;
}

.app-draw {
  padding: 8px 10px;
}

.quick-date-tag {
  margin: 6px 4px 0 !important;
  border-radius: 999px;
  border-color: rgba(148, 163, 184, 0.24) !important;
  background: rgba(15, 23, 42, 0.66) !important;
  color: #cbd5e1 !important;
  transition: all 0.18s ease;
}

.quick-date-tag:hover,
.quick-date-tag.active {
  color: #ffffff !important;
  border-color: rgba(96, 165, 250, 0.75) !important;
  background: #4f7df3 !important;
  transform: translateY(-1px);
}

.query-summary {
  position: fixed;
  left: 16px;
  bottom: 100px;
  width: 354px;
  padding: 12px 14px;
  background: rgba(15, 23, 42, 0.92);
  border: 1px solid rgba(96, 165, 250, 0.28);
  border-radius: 14px;
  box-shadow: 0 14px 30px rgba(2, 6, 23, 0.24);
  z-index: 5;
}

.query-summary__label {
  color: #7dd3fc;
  font-size: 12px;
  font-weight: 700;
  text-align: left;
}

.query-summary__value {
  margin-top: 6px;
  color: #e5edf8;
  font-size: 13px;
  line-height: 1.55;
  text-align: left;
}

.top-button {
  width: 386px;
  left: 0;
  bottom: 14px;
  padding: 0 16px;
  gap: 8px;
}

.search-primary {
  width: 100% !important;
  height: 42px;
  margin-left: 0 !important;
  border-radius: 10px;
  background: #4f7df3;
  border-color: #4f7df3;
  box-shadow: 0 10px 20px rgba(79, 125, 243, 0.24);
  font-weight: 700;
}

.app-quick {
  color: #93c5fd;
  font-size: 14px;
}

.search-text {
  height: auto;
  min-height: 62px;
  margin: 0;
  padding: 12px 14px;
  background: #172033;
  border-bottom: 1px solid rgba(148, 163, 184, 0.14);
}

.search-title-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
  margin-left: 10px;
}

.search-title-block span {
  font-size: 16px;
  font-weight: 800;
}

.search-title-block small {
  color: #94a3b8;
  font-size: 12px;
}

.data-list {
  width: 100%;
  padding: 12px;
  height: calc(100% - 174px);
}

.dataInfoList {
  height: auto;
  min-height: 112px;
  margin-bottom: 10px;
  border-radius: 14px;
  background: rgba(31, 41, 55, 0.72);
  border: 1px solid rgba(148, 163, 184, 0.14);
  overflow: hidden;
}

.dataInfoList:hover {
  background: rgba(37, 99, 235, 0.18);
  border-color: rgba(96, 165, 250, 0.45);
  transform: translateY(-1px);
}

.input_label_info {
  width: 100%;
  margin-left: 0;
  padding: 10px;
}

.input_label {
  font-size: 13px;
  gap: 8px;
}

.label_name {
  margin-left: 0 !important;
  flex: 1;
  text-align: left;
  line-height: 1.4;
}

.littleImg {
  width: 58px;
  height: 58px;
  border-radius: 12px;
  object-fit: cover;
}

.info_details_1,
.info_details_2 {
  color: #cbd5e1;
  font-size: 12px;
  line-height: 1.6;
  text-align: left;
}

.search-box {
  background: #172033;
  border-top: 1px solid rgba(148, 163, 184, 0.14);
  padding: 10px 18px 12px;
}

.button-data {
  border-radius: 10px;
  background-color: rgba(148, 163, 184, 0.14);
  border: 1px solid rgba(148, 163, 184, 0.18);
}

.map-status {
  position: absolute;
  top: 66px;
  left: 410px;
  z-index: 800;
  max-width: calc(100vw - 450px);
  padding: 10px 14px;
  color: #0f172a;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(148, 163, 184, 0.22);
  border-radius: 999px;
  box-shadow: 0 12px 30px rgba(15, 23, 42, 0.16);
  font-size: 13px;
}

:deep(.el-tree) {
  width: 100% !important;
  max-width: 306px !important;
  margin-top: 8px !important;
  padding: 8px;
  border-radius: 12px;
  background: rgba(15, 23, 42, 0.7) !important;
  color: #dbeafe;
}

:deep(.el-tree-node__content:hover) {
  background: rgba(79, 125, 243, 0.18);
}

:deep(.el-select),
:deep(.el-date-editor) {
  --el-border-radius-base: 10px;
}

:deep(.el-radio__input.is-checked .el-radio__inner) {
  background: #4f7df3;
  border-color: #4f7df3;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background: #4f7df3;
  border-color: #4f7df3;
}



/* Alignment fixes for Element Plus grid controls in the search panel */
.mb8 {
  display: block !important;
}

.mb8 > .el-col,
.app-content > .el-row > .el-col {
  width: 100% !important;
  max-width: 100% !important;
  flex: 0 0 100% !important;
}

.app-content > .el-radio-group .el-row {
  display: grid !important;
  grid-template-columns: 92px minmax(0, 1fr);
  gap: 10px;
  align-items: center;
  margin: 0 0 10px 0;
}

.app-content > .el-radio-group .el-row > .el-col {
  width: auto !important;
  max-width: none !important;
  flex: none !important;
}

.app-content > .el-radio-group .el-row > .el-col:first-child {
  grid-column: 1;
}

.app-content > .el-radio-group .el-row > .el-col:last-child {
  grid-column: 2;
}

.app-table,
.app-draw {
  max-width: none !important;
  width: 100% !important;
}

.app-table {
  display: grid !important;
  grid-template-columns: 34px minmax(0, 1fr);
  gap: 10px;
  padding: 10px;
  align-items: center;
}

.app-table > div {
  width: 100%;
}

.app-table :deep(.el-select) {
  width: 100% !important;
  margin: 4px 0 !important;
}

.app-draw {
  gap: 10px;
  padding: 10px;
}

.app-draw :deep(.el-button) {
  width: auto !important;
  min-width: 92px;
  margin: 0 !important;
  border-radius: 8px;
}

:deep(.el-tree) {
  max-width: 100% !important;
  width: 100% !important;
  overflow: visible !important;
}

:deep(.el-tree-node__content) {
  min-width: max-content;
  padding-right: 12px;
}

:deep(.el-tree-node__label) {
  color: #e5edf8;
  font-size: 14px;
  white-space: nowrap;
}

.quick-date-tag.active {
  box-shadow: 0 0 0 2px rgba(96, 165, 250, 0.28), 0 8px 18px rgba(79, 125, 243, 0.22);
  font-weight: 700;
}



/* Search area redesign: drawing/vector-first workflow */
.area-card {
  margin: 14px 16px;
  padding: 18px;
  background: rgba(31, 41, 55, 0.72);
  border: 1px solid rgba(148, 163, 184, 0.14);
  border-radius: 16px;
  text-align: left;
}

.area-card__title {
  color: #e5edf8;
  font-size: 16px;
  font-weight: 800;
  margin-bottom: 8px;
}

.area-card__desc,
.area-card__tip {
  color: #94a3b8;
  font-size: 12px;
  line-height: 1.6;
}

.area-card__tip {
  margin-top: 10px;
}

.area-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  margin-top: 14px;
}

.area-action {
  width: 100%;
  min-height: 38px;
  margin: 0 !important;
  border-radius: 10px;
  font-weight: 700;
}

.vector-upload,
.vector-upload :deep(.el-upload) {
  width: 100%;
}

.app-time :deep(.el-date-editor) {
  width: 100% !important;
  max-width: 380px;
}

.app-time :deep(.el-range-input) {
  color: #1f2937 !important;
  font-weight: 600;
}

.app-time :deep(.el-range-separator) {
  color: #111827 !important;
  font-weight: 700;
}

/* 本轮交互优化：简洁头部、同级上传入口、顺手检索按钮、树选中态与侧栏开关 */
.app-content {
  width: 400px;
  min-width: 400px;
  padding-bottom: 118px;
}

.panel-top {
  margin: 12px 16px 8px;
  width: calc(100% - 32px);
  box-sizing: border-box;
  min-height: auto;
  padding: 20px 22px;
  border-radius: 20px;
  background:
      linear-gradient(135deg, rgba(59, 130, 246, 0.18), rgba(14, 165, 233, 0.06)),
      rgba(17, 24, 39, 0.94);
  border: 1px solid rgba(148, 163, 184, 0.16);
  box-shadow: 0 18px 40px rgba(2, 6, 23, 0.24);
}

.panel-title {
  font-size: 26px;
  letter-spacing: 0.02em;
  line-height: 1.15;
}

.panel-subtitle {
  display: block;
  margin-top: 10px;
  color: #9fb0c7;
}

.panel-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  margin-top: 16px;
}

.panel-action {
  height: 38px;
  border: 1px solid rgba(148, 163, 184, 0.22);
  border-radius: 999px;
  color: #cbd5e1;
  background: rgba(15, 23, 42, 0.62);
  cursor: pointer;
  transition: all 0.18s ease;
  font-weight: 700;
}

.panel-action:hover,
.panel-action.active {
  color: #ffffff;
  border-color: rgba(96, 165, 250, 0.66);
  background: linear-gradient(135deg, #2563eb, #38bdf8);
  box-shadow: 0 10px 22px rgba(37, 99, 235, 0.25);
}

.search-action-card {
  position: fixed;
  left: 12px;
  bottom: 14px;
  width: 376px;
  z-index: 20;
  padding: 12px;
  border-radius: 18px;
  background: rgba(15, 23, 42, 0.94);
  border: 1px solid rgba(96, 165, 250, 0.24);
  box-shadow: 0 18px 38px rgba(2, 6, 23, 0.34);
  backdrop-filter: blur(10px);
}

.search-action-card__summary {
  margin-bottom: 10px;
  color: #cbd5e1;
  font-size: 12px;
  line-height: 1.45;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.search-primary {
  width: 100% !important;
  height: 44px;
  margin: 0 !important;
  border: none;
  border-radius: 13px;
  background: linear-gradient(135deg, #2563eb, #60a5fa);
  box-shadow: 0 12px 26px rgba(37, 99, 235, 0.32);
  font-size: 16px;
  font-weight: 800;
}

.top-button,
.app-quick {
  display: none !important;
}

:deep(.el-tree) {
  padding: 10px !important;
  background: rgba(15, 23, 42, 0.52) !important;
}

:deep(.el-tree-node__content) {
  height: 36px;
  border-radius: 10px;
  color: #dbeafe;
  transition: all 0.16s ease;
}

:deep(.el-tree-node__content:hover),
:deep(.el-tree-node:focus > .el-tree-node__content),
:deep(.el-tree-node.is-current > .el-tree-node__content) {
  background: rgba(59, 130, 246, 0.18) !important;
  color: #ffffff !important;
}

:deep(.el-tree-node__label) {
  color: #e5edf8 !important;
}

:deep(.el-tree-node:focus > .el-tree-node__content .el-tree-node__label),
:deep(.el-tree-node__content:hover .el-tree-node__label),
:deep(.el-tree-node.is-current > .el-tree-node__content .el-tree-node__label) {
  color: #ffffff !important;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner),
:deep(.el-checkbox__input.is-indeterminate .el-checkbox__inner) {
  background: #5b7cfa !important;
  border-color: #8fb2ff !important;
}

:deep(.el-checkbox__inner) {
  background: rgba(255, 255, 255, 0.94);
  border-color: rgba(203, 213, 225, 0.86);
}

.map-status {
  top: 74px;
  left: 424px;
  padding: 12px 18px;
  color: #111827;
  background: rgba(255, 255, 255, 0.94);
  border-radius: 18px;
  font-weight: 600;
}

.sidebar-toggle {
  position: absolute;
  top: 24px;
  width: 32px;
  height: 58px;
  z-index: 9999;
  display: grid;
  place-items: center;
  border: 1px solid rgba(148, 163, 184, 0.28);
  border-left: none;
  border-radius: 0 18px 18px 0;
  color: #e5edf8;
  background: linear-gradient(180deg, rgba(30, 41, 59, 0.92), rgba(15, 23, 42, 0.92));
  box-shadow: 8px 0 24px rgba(2, 6, 23, 0.24);
  cursor: pointer;
  transition: all 0.3s ease;
}

.sidebar-toggle.collapsed {
  left: 0 !important;
}

.sidebar-toggle:hover {
  width: 38px;
  color: #ffffff;
  background: linear-gradient(180deg, #2563eb, #38bdf8);
}

.sidebar-toggle span {
  font-size: 30px;
  line-height: 1;
  transform: translateY(-1px);
}

/* 浅色主题下的左侧边栏按钮样式 */
[data-theme="light"] .sidebar-toggle {
  border-color: #e2e8f0;
  color: #475569;
  background: #ffffff;
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.08);
}

[data-theme="light"] .sidebar-toggle:hover {
  color: #ffffff;
  background: linear-gradient(180deg, #2563eb, #38bdf8);
}

.upload-page-card {
  margin: 16px 16px 0;
  padding: 24px 20px;
  min-height: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  border-radius: 20px;
  background:
      radial-gradient(circle at top, rgba(96, 165, 250, 0.16), transparent 45%),
      rgba(31, 41, 55, 0.72);
  border: 1px solid rgba(148, 163, 184, 0.16);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.04);
}

.upload-page-card__icon {
  width: 64px;
  height: 64px;
  display: grid;
  place-items: center;
  margin-bottom: 18px;
  border-radius: 22px;
  color: #ffffff;
  background: linear-gradient(135deg, #2563eb, #38bdf8);
  box-shadow: 0 16px 34px rgba(37, 99, 235, 0.32);
  font-size: 34px;
  font-weight: 900;
}

.upload-page-card__title {
  color: #f8fafc;
  font-size: 22px;
  font-weight: 900;
}

.upload-page-card__desc {
  max-width: 310px;
  margin-top: 12px;
  color: #a8b6cc;
  font-size: 13px;
  line-height: 1.8;
}

.upload-page-card__button {
  width: 100%;
  height: 44px;
  margin-top: 24px;
  border: none;
  border-radius: 13px;
  background: linear-gradient(135deg, #2563eb, #60a5fa);
  box-shadow: 0 12px 26px rgba(37, 99, 235, 0.28);
  font-size: 15px;
  font-weight: 800;
}

.upload-page-card__tips {
  display: grid;
  gap: 8px;
  width: 100%;
  margin-top: 18px;
}

.upload-page-card__tips span {
  padding: 9px 10px;
  border-radius: 12px;
  color: #cbd5e1;
  background: rgba(15, 23, 42, 0.48);
  border: 1px solid rgba(148, 163, 184, 0.12);
  font-size: 12px;
}

/* 新增：搜索框底部操作按钮区域 */
.search-box__actions {
  display: flex;
  gap: 10px;
  padding: 12px 0;
  width: 100%;
  justify-content: center;
}

/* 新增：卫星详情对话框样式 */
.satellite-detail-dialog {
  background-color: var(--dialog-bg, #202326) !important;
}

.satellite-detail-dialog .el-dialog__header {
  background-color: var(--dialog-bg, #202326);
}

.satellite-detail-dialog .el-dialog__body {
  padding: 0;
}

.satellite-detail {
  display: flex;
  height: 100%;
  background-color: var(--bg-secondary, #1c1e21);
}

.satellite-detail__image {
  width: 50%;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.satellite-detail__title {
  color: var(--text-primary, white);
  margin-bottom: 25px;
  font-weight: 600;
}

.satellite-detail__img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
}

.satellite-detail__info {
  width: 50%;
  padding: 20px;
  color: var(--text-primary, white);
  line-height: 1.8;
}

.satellite-detail__info p {
  margin: 4px 0;
}

.satellite-detail__info strong {
  color: var(--text-secondary, #94a3b8);
}

/* 新增：专题图对话框样式 */
.thematic-map-dialog {
  background-color: var(--dialog-bg, #202326) !important;
}

.thematic-map-dialog .el-dialog__header {
  background-color: var(--dialog-bg, #202326);
}

.dialog-title {
  color: var(--text-primary, white);
  font-weight: 700;
}

.form-label {
  color: var(--text-primary, white);
  padding-bottom: 10px;
  font-weight: 600;
}

.required {
  color: #f56c6c;
  margin-right: 4px;
}

.thematic-preview-img {
  width: 100%;
  border-radius: 8px;
}

/* 新增：图标按钮间距优化 */
.right-buttons {
  display: flex;
  gap: 4px;
}

.right-buttons .el-button {
  margin-left: 0;
}

/* 右侧边栏样式 */
.right-sidebar {
  position: absolute;
  top: 0;
  right: 0;
  width: 320px;
  height: 100%;
  background: linear-gradient(180deg, #1c1e21 0%, #0f172a 100%);
  border-left: 1px solid #2d2f33;
  display: flex;
  flex-direction: column;
  z-index: 10;
}

.right-sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #2d2f33;
  background: rgba(31, 41, 55, 0.5);
}

.right-sidebar-title {
  color: #e5edf8;
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 4px;
}

.right-sidebar-subtitle {
  color: #94a3b8;
  font-size: 12px;
}

.right-sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
}

.result-item {
  background: rgba(31, 41, 55, 0.6);
  border: 1px solid rgba(148, 163, 184, 0.14);
  border-radius: 12px;
  margin-bottom: 12px;
  padding: 12px;
  transition: all 0.2s ease;
}

.result-item:hover {
  background: rgba(31, 41, 55, 0.8);
  border-color: rgba(96, 165, 250, 0.3);
}

.result-item-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.result-checkbox {
  margin-right: 8px;
  cursor: pointer;
}

.result-item-title {
  color: #e5edf8;
  font-size: 13px;
  font-weight: 600;
  flex: 1;
}

.result-item-body {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

.result-item-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--el-color-primary);
}

.result-item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-row {
  display: flex;
  font-size: 12px;
}

.info-label {
  color: #94a3b8;
  margin-right: 4px;
}

.info-value {
  color: #e5edf8;
}

.result-item-actions {
  display: flex;
  gap: 6px;
}

.result-item-actions .el-button {
  flex: 1;
}

.right-sidebar-footer {
  padding: 12px;
  border-top: 1px solid #2d2f33;
  background: rgba(31, 41, 55, 0.5);
}

.right-sidebar-footer .el-pagination {
  margin-bottom: 10px;
}

.footer-actions {
  display: flex;
  gap: 8px;
}

.footer-actions .el-button {
  flex: 1;
}

/* 右侧边栏收起按钮 */
.right-sidebar-toggle {
  position: absolute;
  top: 24px;
  width: 32px;
  height: 58px;
  z-index: 9999;
  display: grid;
  place-items: center;
  border: 1px solid rgba(148, 163, 184, 0.28);
  border-right: none;
  border-radius: 18px 0 0 18px;
  color: #e5edf8;
  background: linear-gradient(180deg, rgba(30, 41, 59, 0.92), rgba(15, 23, 42, 0.92));
  box-shadow: -8px 0 24px rgba(2, 6, 23, 0.24);
  cursor: pointer;
  transition: all 0.3s ease;
}

.right-sidebar-toggle.collapsed {
  right: 0 !important;
}

.right-sidebar-toggle:hover {
  width: 38px;
  color: #ffffff;
  background: linear-gradient(180deg, #2563eb, #38bdf8);
}

.right-sidebar-toggle span {
  font-size: 30px;
  line-height: 1;
  transform: translateY(-1px);
}

/* 浅色主题下的右侧边栏样式 */
[data-theme="light"] .right-sidebar {
  background: #ffffff;
  border-left-color: #e2e8f0;
}

[data-theme="light"] .right-sidebar-header {
  background: #f8fafc;
  border-bottom-color: #e2e8f0;
}

[data-theme="light"] .right-sidebar-title {
  color: #1e293b;
}

[data-theme="light"] .right-sidebar-subtitle {
  color: #64748b;
}

[data-theme="light"] .result-item {
  background: #f8fafc;
  border-color: #e2e8f0;
}

[data-theme="light"] .result-item:hover {
  background: #f1f5f9;
  border-color: rgba(96, 165, 250, 0.3);
}

[data-theme="light"] .result-item-title {
  color: #374151;
}

[data-theme="light"] .info-label {
  color: #64748b;
}

[data-theme="light"] .info-value {
  color: #374151;
}

[data-theme="light"] .right-sidebar-footer {
  background: #f8fafc;
  border-top-color: #e2e8f0;
}

[data-theme="light"] .right-sidebar-toggle {
  border-color: #e2e8f0;
  color: #475569;
  background: #ffffff;
  box-shadow: -4px 0 12px rgba(0, 0, 0, 0.08);
}

[data-theme="light"] .right-sidebar-toggle:hover {
  color: #ffffff;
  background: linear-gradient(180deg, #2563eb, #38bdf8);
}
</style>
