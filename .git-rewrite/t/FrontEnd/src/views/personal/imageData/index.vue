<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px"
      style="margin-top: 10px">
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="upload" @click="handleAdd" v-hasPermi="['record:info:add']">上传数据
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['record:info:remove']">批量删除
        </el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="primary" plain icon="Check" :disabled="multiple" @click="batchProcess">批量处理
        </el-button>
      </el-col>
      <!-- <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center">
      </el-table-column>
      <el-table-column label="影像名称" align="center" prop="imageName" width="200px">
        <template #default="scope">
          {{ scope.row.satelliteType + "_" + scope.row.sensorType + "_" + parseTime(scope.row.collectTime,
          '{y}-{m}-{d}') }}
        </template>
      </el-table-column>
      <el-table-column label="影像速览" align="center" prop="image" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="省份" align="center" prop="province" />
      <el-table-column label="城市" align="center" prop="city" />
      <el-table-column label="采集时间" align="center" prop="collectTime" width="180"/>
      <el-table-column label="卫星类型" align="center" prop="satelliteType" />
      <el-table-column label="传感器类型" align="center" prop="sensorType" />
      <el-table-column label="添加时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['record:info:query']">详情
          </el-button>

          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
            v-hasPermi="['record:info:remove']">删除
          </el-button>

          <el-button link type="primary" icon="Check" @click="handleProcess(scope.row)">处理分析
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改我的数据对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
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

        <el-button style="margin-left: 200px" type="success" @click="handleUnZip()">解压并上传</el-button>
      </el-form>
    </el-dialog>

    <!-- 打开详情对话框 -->
    <el-dialog v-model="openDetail" width="800px" style="height: 600px; background-color: #1c1e21" append-to-body>
      <div class="flex-container">
        <div style="width: 50%; display: flex; flex-direction: column;">
          <div style="color: white; margin-bottom: 25px">
            {{ detailList ? detailList.satelliteType + "_" + detailList.sensorType + "_" +
            parseTime(detailList.collectTime, '{y}-{m}-{d}') : '' }}
          </div>
          <img :src="detailList ? detailList.image : ''" alt="" style="max-width: 90%; max-height: 90%;">
        </div>
        <div class="text-container">
          <div style="padding:3px">
            地址：<span>{{ detailList.province }} {{ detailList.city }}</span>
          </div>
          <div style="padding:3px">
            卫星类型：<span>{{ detailList.satelliteType }}</span>
          </div>
          <div style="padding:3px">
            传感器类型：<span>{{ detailList.sensorType }}</span>
          </div>
          <div style="padding:3px">
            采集时间：<span>{{ detailList.collectTime }}</span>
          </div>
          <!--<div style="padding:3px">-->
            <!--地面站代号：<span>{{ detailList.zoneName }}</span>-->
          <!--</div>-->
          <div style="padding:3px">
            景path：<span>{{ detailList.viewPath }}</span>
          </div>
          <div style="padding:3px">
            景row：<span>{{ detailList.viewRow }}</span>
          </div>
          <div style="padding:3px">
            星下点path：<span>{{ detailList.starPath }}</span>
          </div>
          <div style="padding:3px">
            星下点row：<span>{{ detailList.starRow }}</span>
          </div>
          <div style="padding:3px">
            左上角经/纬度：<span>{{ detailList.leftupLongitude !== null && detailList.leftupLatitude !== null ?
              detailList.leftupLongitude.toFixed(3) + "°E, " + detailList.leftupLatitude.toFixed(3) + "°N" : ''
              }}</span>
          </div>
          <div style="padding:3px">
            左下角经/纬度：<span>{{ detailList.leftdownLongitude !== null && detailList.leftdownLatitude !== null ?
              detailList.leftdownLongitude.toFixed(3) + "°E, " + detailList.leftdownLatitude.toFixed(3) + "°N" : ''
              }}</span>
          </div>
          <div style="padding:3px">
            右上角经/纬度：<span>{{ detailList.rightupLongitude !== null && detailList.rightupLatitude !== null ?
              detailList.rightupLongitude.toFixed(3) + "°E, " + detailList.rightupLatitude.toFixed(3) + "°N" : ''
              }}</span>
          </div>
          <div style="padding:3px">
            右下角经/纬度：<span>{{ detailList.rightdownLongitude !== null && detailList.rightdownLatitude !== null ? detailList.rightdownLongitude.toFixed(3) + "°E, " + detailList.rightdownLatitude.toFixed(3) + "°N" : ''
              }}</span>
          </div>

        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Info">
import {
  listInfo,
  getInfo,
  delInfo,
  addInfo,
  updateInfo,
  addInfo1
} from "@/api/record/info";
import { getMenuInfoList } from "@/api/Data/DataInfo.js";
import axios from "axios";
import { ElLoading } from 'element-plus';
import { parseTime } from "../../../utils/ruoyi";
import { useRouter } from "vue-router";
import { getToken } from '@/utils/auth'
const router = useRouter();
const { proxy } = getCurrentInstance();
const infoList = ref([]);
const dataList = ref([]);
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const detailList = ref({});
const options = ref([]);
const selectedProvince = ref("");
const selectedCity = ref("");
const dataType = ref();
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
const { sys_resource_series, sys_high_score, sys_jilin_series, sys_vehicle_series, sys_data_series } = proxy.useDict('sys_resource_series', 'sys_high_score', 'sys_jilin_series', 'sys_vehicle_series', 'sys_data_series');

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    iamgeName: null,
    image: null,
    collectTime: null,
    province: null,
    city: null,
    satelliteType: null,
    sensorType: null,
  },
  rules: {
    imageName: [
      { required: true, message: '请输入文件名称', trigger: 'blur' }
    ],
    image: [
      { required: true, message: '请上传影像', trigger: 'change' }
    ],
    collectTime: [
      { required: true, message: '请选择采集时间', trigger: 'blur' }
    ],
    province: [
      { required: true, message: '请选择省份', trigger: 'change' }
    ],
    city: [
      { required: true, message: '请选择城市', trigger: 'change' }
    ],
    satelliteType: [
      { required: true, message: '请选择卫星类型', trigger: 'change' }
    ],
    sensorType: [
      { required: true, message: '请输入传感器类型', trigger: 'blur' }
    ],
  },
});
const fileList = ref([]);
const { queryParams, form, rules } = toRefs(data);

/** 查询我的数据列表 */
function getList() {
  loading.value = true;
  listInfo(queryParams.value).then((response) => {
    infoList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    iamgeName: null,
    imagePath: null,
    zoneName: null,
    leftdownLongitude: null,
    leftdownLatitude: null,
    rightupLongitude: null,
    rightupLatitude: null,
    province: null,
    city: null,
    startTime: null,
    endTime: null,
    satelliteId: null,
    sensorId: null,
    userAccount: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  };
  selectedProvince.value = "";
  selectedCity.value = "";

  proxy.resetForm("infoRef");
}

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
    const response = await axios.get("/resData/city.json");
    const citiesData = response.data;
    parseCitiesData(citiesData);
  } catch (error) {
    console.error("Error fetching city data:", error);
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

function getMenuList() {
  getMenuInfoList().then((res) => {
    if (res.code == 200) {
      dataType.value = res.data;
    }
  });
}

getMenuList();
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  dataList.value = selection;
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加我的数据";
}
import useAppStore from '@/store/modules/app'

function handleUnZip() {
    proxy.$refs["infoRef"].validate((valid) => {
        if (valid) {
            // 创建一个 Loading 实例
            const loadingInstance = ElLoading.service({
                lock: true,       // 锁定屏幕，禁止滚动
                text: '上传中，预计1~2分钟，请耐心等待...',
                background: 'rgba(0, 0, 0, 0.7)'  // 背景颜色
            });

            addInfo1(form.value).then((res) => {
                // 关闭 Loading 实例
                loadingInstance.close();
                if (res.code == 200) {
                    proxy.$modal.msgSuccess("上传成功");
                } else {
                    proxy.$modal.msgError(res.msg);
                }
                open.value = false;
                getList();
            }).catch(() => {
                // 关闭 Loading 实例
                loadingInstance.close();
                open.value = false;
            });
        }
    })
}

/* 批量处理分析 */
function batchProcess() {
  useAppStore().setMenuHide(false);
  const newdatalist = JSON.parse(JSON.stringify(dataList.value));

  for(let index in newdatalist){
    newdatalist[index].id = newdatalist[index].satelliteId
  }
  const rowData = JSON.stringify(newdatalist);
  router.push({
    path: "/aiProcess",
    state: { rowData }
  });
}
/* 单个处理分析 */
function handleProcess(row) {
  useAppStore().setMenuHide(false);
  row.id = row.satelliteId
  const rowData = JSON.stringify(row);
  router.push({
    path: "/aiProcess",
    state: { rowData }
  });
}

/** 详情按钮操作 */
function handleUpdate(row) {
  reset();
  const _ids = row.id || ids.value;
  getInfo(_ids).then((response) => {
    detailList.value = response.data;
    openDetail.value = true;
    title.value = "我的数据";
  });
}

/* 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const imageName = row.satelliteType + "_" + row.sensorType + "_" + row.collectTime;
  const _names = imageName;
  proxy
    .$confirm("是否确认删除影像名称为" + _names + "的数据项?", "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(function () {
      return delInfo(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    });
}

/* 提交按钮操作 */
function submitForm() {
  proxy.$refs["infoRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateInfo(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addInfo(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

getList();
</script>

<style scoped lang="scss">
.flex-container {
  display: flex;
  align-items: flex-start; /* Align items at the start */
}

.image-container {
  margin-left: 50px; /* Space between image and text */
  width: 400px;
}

.text-container {
  margin-top: 40px;
  display: flex;
  margin-left: 10px;
  flex-direction: column;
  color: #fff;
  padding: 10px;
  width: 50%;
}
</style>
