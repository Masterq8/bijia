<template>
  <div class="home">
    <div class="app-content" v-show="showText">

      <div v-show="showSearch" class="search-result">

        <div class="search-text">
          <svg @click="goBack()" viewBox="64 64 896 896" focusable="false" data-icon="arrow-left" width="1em"
            height="1em" fill="#fff" aria-hidden="true" class="backIcon" style="font-size: large;">
            <path
              d="M872 474H286.9l350.2-304c5.6-4.9 2.2-14-5.2-14h-88.5c-3.9 0-7.6 1.4-10.5 3.9L155 487.8a31.96 31.96 0 000 48.3L535.1 866c1.5 1.3 3.3 2 5.2 2h91.5c7.4 0 10.8-9.2 5.2-14L286.9 550H872c4.4 0 8-3.6 8-8v-60c0-4.4-3.6-8-8-8z">
            </path>
          </svg>
          公开数据检索
        </div>
        <div class="data-list">
          <div v-for="item in satelliteData" :key="item.id">
            <div class="dataInfoList" @dblclick="navigateToCenter(item)" @mouseover="onMouseOver(item)"
              @mouseleave="onMouseLeave(item)">
              <!-- 生成卫星名、采集时间、传感器信息 -->
              <div class="input_label_info">
                <div class="input_label">
                  <input type="checkbox" class="chBoxStyle" @click="imageshow(item)" :ref="`checkbox_${item.id}`"
                    :value="item.id" v-model="checkboxStatus[item.id]">
                  <label class="label_name" for="checkbox" style="margin-left: -70px">{{ item.satelliteType + "_" +
                    item.region + "_" + formatCollectTime(item.collectTime) }}</label>
                  <div class="right-buttons">
                    <!-- <el-button class="iconfont-zr icon-zryanjing-kai_1" color="black"   @view-image="handleViewImage" :satellite-data="satelliteData" style="margin-right: -25px"></el-button> -->
                    <el-button class="iconfont-zr icon-zryanjing-kai_1" color="black" @click="handleViewImage(item)"
                      style="margin-right: -25px"></el-button>
                    <el-button class="iconfont-zr icon-zrxiangqing" color="black" @click="viewDetails(item)"
                      style="margin-right: -25px"></el-button>
                    <el-button class="iconfont-zr icon-zrxiangqing1" color="black"
                      @click="openThematicMap(item)"></el-button>
                  </div>
                </div>
                <div class="infor">
                  <img :src="item.image" class="littleImg" @click="viewDetails(item)">
                  <div class="info_details_1">
                    <div class="satellite">
                      <span class="white">卫星: </span>
                      <span class="satellite_name white">{{ item.satelliteType }}</span>
                      <br>
                      <span class="white">传感器: </span>
                      <span class="sensor_value white">{{ item.sensorType }}</span>
                    </div>
                  </div>
                  <div class="info_details_2">
                    采集时间：
                    <br />
                    {{ item.collectTime }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="search-button">
          <div class="search-box">
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
            <br><br><br>
            <el-button :disabled="!isAnalyzeAllowed" class="button-data" @click="handleAddDate()">加入我的数据</el-button>
            <el-button :disabled="!isAnalyzeAllowed" @click="ProcessingAnalysis()" type="primary">处理分析</el-button>
          </div>
        </div>
      </div>

      <div class="panel-top">
        <div class="panel-title">遥感数据</div>
      </div>

      <el-row :gutter="10" class="mb8">
        <el-col :span="6">
          <div class="app-tabulation">
            <div class="app-type">数据类型</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-tree ref="elTreeRef"
              style="max-width: 600px; width: 200px; background-color: #1c1e21; margin-top: 20px;" :data="data"
              show-checkbox node-key="dictValue" :props="menuProps" @check-change="handleNodeClick">
            </el-tree>
          </div>
        </el-col>
      </el-row>

      <el-radio-group v-model="radio1" @change="handleRadioChange">
        <el-row>
          <el-col :span="7">
            <div class="app-tabulation">
              <div class="app-type">检索区域</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="app-table">
              <el-radio value="1" size="large" style="margin: 0px 0px 0px 10px"></el-radio>
              <div style="">
                <el-select placeholder="请选择省份" class="black-text" style="
                    width: 200px;
                    margin-top: 10px;
                    margin-bottom: 5px;
                    margin-left: 15px;
                    color: #1c1e21;
                  " size="default" v-model="selectedProvince" :disabled="isDisabled" clearable @clear="setValueNull">
                  <el-option v-for="item in options" :key="item.value" :label="item.label"
                    :value="item.value" style="width: 200px"></el-option>
                </el-select>
                <el-select placeholder="请选择城市" style="
                    width: 200px ;
                    margin-top: 0px;
                    margin-bottom: 5px;
                    margin-left: 15px;
                  " size="default" v-model="selectedCity" :disabled="isDisabled" clearable >
                  <el-option v-for="item in selectedProvinceChildren" :key="item.value" :label="item.label"
                    :value="item.value"></el-option>
                </el-select>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="9"> </el-col>
          <el-col :span="4">
            <div class="app-draw">
              <el-radio value="2" size="large" style="width: 1px;margin-left:10px;"></el-radio>
              <el-button color="black" style="
                  width: 65px;
                  margin-top: 7px;
                  margin-bottom: 5px;
                  margin-left: 0px;
                " :disabled="edDisabled" @click="confirm">开始绘制</el-button>
              <el-button color="black" style="
                  width: 65px;
                  margin-top: 7px;
                  margin-bottom: 5px;
                  margin-left: 15px;
                " :disabled="edDisabled" @click="cancelDrawing">取消绘制</el-button>
            </div>
          </el-col>
        </el-row>
      </el-radio-group>

      <el-row>
        <el-col :span="6">
          <div class="app-type">采集时间</div>
        </el-col>
        <el-col :span="4">
          <div class="app-time">
            <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期"
              end-placeholder="结束日期" size="small" :disabled-date="disabledDate"
              style="width: 230px; height: 30px; margin-top: 15px">
            </el-date-picker>
          </div>
        </el-col>
      </el-row>

    <el-row type="flex" justify="center">
        <el-tag
                v-for="item in dateRanges"
                :key="item.label"
                :type="item.type"
                effect="dark" style="margin-left: 5px;margin-top:10px;" @click="handleTagClick(item.value)">
            {{ item.label }}
        </el-tag>
    </el-row>
      <el-row>
        <el-col :span="6">
          <div class="app-type">其他选项</div>
        </el-col>
        <el-col :span="8">
          <div>
            <div class="app-type">
              <el-checkbox size="large" v-model="checked" @click="openSelect()" />
              保持查询结果
            </div>
            <el-divider style="width: 230px; margin-top: 10px" />
          </div>
          <div>
            <el-button style="
                background-color: #3d444f;
                color: aliceblue;
                border: none;
                margin-top: -10px;
              " @click="handleUpload()">上传我的数据</el-button>
          </div>
          <el-divider style="width: 230px; margin-top: 10px" />
        </el-col>
      </el-row>

      <div>
        <div class="top-button">
          <el-button class="app-quick" @click="goToPath()" v-show="isShowPage">快速上手</el-button>
          <el-button type="primary" @click="search()" style="width: 200px;margin-left: 0px !important;"
            v-show="isShowPage">检索</el-button>
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

    <img src="@/assets/images/data.png" alt="" class="images" :class="{ active: isActive == true }"
      @click="toggleTextVisibility()" :style="`left:${realLeft}; `" />
    <baseMap ref="mapDom" :style="`width:${realWidth};`" @getValue="getSonValue" @getSonValue1="getSonValue1"
      @getSonValue2="getSonValue2" />

    <!--卫星数据详情框-->
    <el-dialog :title="title" v-model="satelliteDetails" width="800px" height="600px" append-to-body
      style="background-color:#202326;">
      <div style="display: flex; height: 100%; background-color: #1c1e21;">
        <!-- 图片区域 -->
        <div style="width: 50%; display: flex; flex-direction: column;">
          <div style="color: white; margin-bottom: 25px">
            {{ selectedItem ? selectedItem.satelliteType + "_" + selectedItem.region + "_" +
            formatCollectTime(selectedItem.collectTime) : '' }}
          </div>
          <img :src="selectedItem ? selectedItem.image : ''" alt="" style="max-width: 90%; max-height: 90%;">
        </div>
        <!-- 数据展示区域 -->
        <div style="width: 50%; padding: 20px; color: white;">
          <p v-if="selectedItem"><strong>地址:</strong> {{ selectedItem.province }} {{ selectedItem.city }}</p>
          <p v-if="selectedItem"><strong>卫星类型:</strong> {{ selectedItem.satelliteType }}</p>
          <p v-if="selectedItem"><strong>传感器类型:</strong> {{ selectedItem.sensorType }}</p>
          <p v-if="selectedItem"><strong>采集时间:</strong> {{ selectedItem.collectTime }}</p>
          <!--<p v-if="selectedItem"><strong>地面站代号：</strong> </p>-->
          <p v-if="selectedItem"><strong>景path:</strong> {{ selectedItem.viewPath }}</p>
          <p v-if="selectedItem"><strong>景row:</strong> {{ selectedItem.viewRow }}</p>
          <p v-if="selectedItem"><strong>星下点path:</strong> {{ selectedItem.starPath }}</p>
          <p v-if="selectedItem"><strong>星下点row:</strong> {{ selectedItem.starRow }}</p>
          <p v-if="selectedItem" class="spacing"><strong>左上角经/纬度:</strong> {{ selectedItem.leftupLongitude + "°E" + ","
            +
            selectedItem.leftupLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>左下角经/纬度:</strong> {{ selectedItem.leftdownLongitude + "°E" +
            "," +
            selectedItem.leftdownLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>右上角经/纬度:</strong> {{ selectedItem.rightupLongitude + "°E" + ","
            +
            selectedItem.rightupLatitude + "°N"}}</p>
          <p v-if="selectedItem" class="spacing"><strong>右下角经/纬度:</strong> {{ selectedItem.rightdownLongitude + "°E" +
            "," +
            selectedItem.rightdownLatitude + "°N"}}</p>
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
    <el-dialog v-model="HandleAnalytics" width="800px" append-to-body style="background-color: #202326">
      <template #title>
        <span style="color: white">专题图</span>
      </template>
      <div class="container">
        <div class="form-container">
          <el-form :model="form" label-width="120px">
            <div style="color: white;padding-bottom: 10px;"><span style="color: red;">*</span> 专题图标题</div>
            <el-input style="width: 50%" v-model="formList.title" placeholder="请输入专题图标题" />
            <div style="color: white; width: 50%;padding-top: 10px;padding-bottom: 10px;"><span
                style="color: red;">*</span> 专题图类型</div>
            <el-select v-model="formList.type" style="color: white; width: 50%">
              <el-option v-for="item in imageTypeData" :key="item.id" :value="item.categoryCode"
                :label="item.imageCategory">
              </el-option>
            </el-select>
            <div style="color: white;padding-top: 10px;padding-bottom: 10px;"> 专题图备注信息</div>
            <el-input type="textarea" v-model="formList.exInfo" style="resize: none;width:80%;" name="info" rows="5"
              placeholder="请输入备注信息"></el-input>
            <!-- <el-input
              style="color: white; width: 80%; height: 100px"
              v-model="form.image4"
              placeholder="请输入专题图备注信息"
            /> -->
          </el-form>
        </div>
        <div class="side-container">
          <div class="zhuantitu-img">
            <img :id="formList.type" :src="getImageUrl(formList.type)" style="width: 100%" alt="Type1" />
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
          <span class="red-text">单击</span>地图开始绘制查询区域， 点击<span class="dark-text">取消绘制</span>按钮或者<span
            class="brown-text">选择省份检索方式</span>退出当前绘图
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
import { ref, reactive } from "vue";
import axios from "axios";
import baseMap from "@/components/Map/Map.vue";
import { getMenuInfoList } from "@/api/Data/DataInfo.js";
import { addInfo,addDateInfo,addInfo1 } from "@/api/record/info";
import { aiInfo } from "@/api/ai/info";
import { listSatellite, getSatellite, listSatellite1 } from "@/api/map/satellite.js";
import { ElMessageBox } from "element-plus";
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
const radio1 = ref("1");
const isDisabled = ref(false);
const edDisabled = ref(true);
const data = ref([]);
const dialogVisible = ref(false);
const open = ref(false);
const options = ref([]);
const selectedProvince = ref("");
const selectedCity = ref("");
const checked = ref(false);
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
])
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

const realWidth = computed(() => (showText.value ? "80vw" : "100vw"));

const realLeft = computed(() => (showText.value ? "386px" : "0vw"));

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
  mapDom.value.drawPolygon();
  console.log("确定绘制");
};

//清除所绘制的检索区域
const cancelDrawing = () => {
  // 清除所有绘制的图形
  mapDom.value.clearAllLayers();
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
      const startDate = new Date(tempDate.getFullYear() + '-' + (tempDate.getMonth() + 1) + '-' + tempDate.getDate());
      dateRange.value = [startDate, endDate];
  }

// 检索
const search = () => {
    if (selectedSatelliteTypes.value.length === 0) {
      return ElMessageBox.alert('请选择数据类型！', '提示', {
        confirmButtonText: '确定',
        customClass: 'my-custom-message-box'
      });
    }
    queryParams.satelliteTypes = selectedSatelliteTypes.value // 用户选择的卫星类型

    if (radio1.value === "1") {
        if (selectedProvince.value == '') {
            return ElMessageBox.alert('请选择查询区域！(至少选择查询省份)', '提示', {
                confirmButtonText: '确定',
                customClass: 'my-custom-message-box'
            });
        }
        queryParams.province = selectedProvince.value, // 用户选择的省份
        queryParams.city = selectedCity.value // 用户选择的城市
        queryParams.points = []
    } else if (radio1.value === "2") {
        queryParams.province = null
        queryParams.city = null
        const drawnPolygon = getDrawnPolygon();
        if (drawnPolygon == null) {
            return ElMessageBox.alert('请绘制检索区域！', '提示', {
                confirmButtonText: '确定',
                customClass: 'my-custom-message-box'
            });
        }
        queryParams.points = []
        drawnPolygon.getLatLngs()[0].forEach(item => {
            queryParams.points.push(item.lat+','+item.lng)
        })
        // queryParams.points = drawnPolygon.getLatLngs()[0];
    }
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


function getMenuList() {
  getMenuInfoList().then((res) => {
    if (res.code == 200) {
      data.value = res.data;
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
  const { dictValue, children } = data;

  // 处理父节点
  if (children && children.length > 0) {
    if (checked) {
      // 选择父节点时，选择所有子节点
      children.forEach(child => {
        if (!selectedSatelliteTypes.value.includes(child.dictValue)) {
          selectedSatelliteTypes.value.push(child.dictValue);
        }
      });
    } else {
      // 取消选择父节点时，取消选择所有子节点
      children.forEach(child => {
        selectedSatelliteTypes.value = selectedSatelliteTypes.value.filter(type => type !== child.dictValue);
      });
    }
  } else {
    // 处理叶子节点
    if (checked) {
      // 选择叶子节点
      if (!selectedSatelliteTypes.value.includes(dictValue)) {
        selectedSatelliteTypes.value.push(dictValue);
      }
    } else {
      // 取消选择叶子节点
      selectedSatelliteTypes.value = selectedSatelliteTypes.value.filter(type => type !== dictValue);
    }
  }

  // 使用 Set 去重
  selectedSatelliteTypes.value = Array.from(new Set(selectedSatelliteTypes.value));

  // 调试输出
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

.el-pagination ::v-deep .el-select__wrapper.el-tooltip__trigger.el-tooltip__trigger{
  background-color:#000000 !important;
  border: 1px solid rgb(0, 0, 0);
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
</style>
