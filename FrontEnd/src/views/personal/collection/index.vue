<template>
    <div class="app-container">
      <div class="top-button">
        <el-row>
          <div class="button-left" style="margin: 10px">
            <el-col :span="1.5">
              <el-button
                  type="primary"
                  icon="MessageBox"
                  :disabled="multiple"
                  @click="batchProcess"
              >批量处理
              </el-button>
              <el-button
                  type="danger"
                  icon="Delete"
                  :disabled="multiple"
                  @click="handleDelete"
              >批量删除
              </el-button>
            </el-col>
          </div>
          <!-- <div class="button-right" style="margin: 10px">
            <el-dropdown :hide-on-click="false" size="small">
              <el-button  title="筛选列" icon="Operation" circle style="margin-right: 12px;"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked1" label="ID"/>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked2" label="影像名称" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked3" label="影像速览" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked4" label="地理位置" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked5" label="经纬度范围" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked23" label="采集起始时间" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked7" label="采集结束时间" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked8" label="卫星类型" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked9" label="传感器类型" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked10" label="接收站ID" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked11" label="景Path" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked12" label="景Row" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked13" label="星下点Path" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked14" label="星下点Row" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked15" label="TopLeftLongitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked16" label="BottomLeftLongitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked17" label="TopRightLongitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked18" label="BottomRightLongitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked19" label="TopLeftLatitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked20" label="BottomLeftLatitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked21" label="TopRightLatitude" />
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checked22" label="BottomRightLatitude" />
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-dropdown :hide-on-click="false" size="small">
            <el-button  title="导出" icon="Download" circle style="margin-right: 12px;"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleExport1">
                    导出csv格式文件
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleExport2">
                    导出xls格式文件
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button  title="打印" icon="Printer" circle @click="handlePrint"/>
            <el-button  title="提示" icon="InfoFilled" circle @click="handleCue"/>
          </div> -->
        </el-row>
      </div>

      <!--卫星数据详情框-->
      <el-dialog :title="title" v-model="dialogTableVisible" width="800px" height="600px" append-to-body style="background-color:#202326;">
        <div style="display: flex; height: 100%; background-color: #1c1e21;">
          <!-- 图片区域 -->
          <div style="width: 50%; display: flex; flex-direction: column;" >
            <div style="color: white; margin-bottom: 25px">
              {{ DetailRow ? DetailRow.satelliteType + "_" + DetailRow.region + "_" + formatCollectTime(DetailRow.collectTime) : '' }}
            </div>
            <img :src="DetailRow ? DetailRow.image : ''" alt="" style="max-width: 90%; max-height: 90%;">
          </div>
          <!-- 数据展示区域 -->
          <div style="width: 50%; padding: 20px; color: white;" >
            <p v-if="DetailRow"><strong>地址:</strong> {{  DetailRow.province }} {{  DetailRow.city }}</p>
            <p v-if="DetailRow"><strong>卫星类型:</strong> {{ DetailRow.satelliteType }}</p>
            <p v-if="DetailRow"><strong>传感器类型:</strong> {{ DetailRow.sensorType }}</p>
            <p v-if="DetailRow"><strong>采集时间:</strong> {{ DetailRow.collectTime }}</p>
            <!--<p v-if="DetailRow"><strong>地面站代号：</strong> </p>-->
            <p v-if="DetailRow"><strong>景path:</strong> {{ DetailRow.viewPath }}</p>
            <p v-if="DetailRow"><strong>景row:</strong> {{ DetailRow.viewRow }}</p>
            <p v-if="DetailRow"><strong>星下点path:</strong> {{ DetailRow.starPath }}</p>
            <p v-if="DetailRow"><strong>星下点row:</strong> {{ DetailRow.starRow }}</p>
            <p v-if="DetailRow"><strong>左上角经/纬度:</strong> {{ DetailRow.leftupLongitude + "°E" + "," + DetailRow.leftupLatitude + "°N"}}</p>
            <p v-if="DetailRow"><strong>左下角经/纬度:</strong> {{ DetailRow.leftdownLongitude + "°E" + "," + DetailRow.leftdownLatitude + "°N"}}</p>
            <p v-if="DetailRow"><strong>右上角经/纬度:</strong> {{ DetailRow.rightupLongitude + "°E" + "," + DetailRow.rightupLatitude + "°N"}}</p>
            <p v-if="DetailRow"><strong>右下角经/纬度:</strong> {{ DetailRow.rightdownLongitude + "°E" + "," + DetailRow.rightdownLatitude + "°N"}}</p>
          </div>
        </div>
      </el-dialog>

        <el-table v-loading="loading" border :data="infoList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
                    <el-table-column label="影像名称" align="center" prop="imageName" >
                      <template #default="scope">
                        {{ scope.row.satelliteType + "_" + scope.row.sensorType + "_" + parseTime(scope.row.collectTime, '{y}-{m}-{d}') }}
                      </template>
                    </el-table-column>
                    <el-table-column label="影像速览" align="center" prop="image" width="100" >
                        <template #default="scope">
                            <image-preview :src="scope.row.image" :width="50" :height="50"/>
                        </template>
                    </el-table-column>
                    <el-table-column label="地理位置" align="center" prop="region">
                        <template #default="scope">
                            <span> {{scope.row.province }} {{scope.row.city}} </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="经纬度范围" align="center" prop="range">
                      <template #default="scope">
                        <span>{{ formatTo2Decimals(scope.row.leftupLongitude) }}</span>
                        <span> {{scope.row.leftdownLongitude + "," + scope.row.leftdownLatitude }} </span>
                      </template>
                    </el-table-column>
          <el-table-column label="采集时间" align="center" prop="collectTime" width="180" v-if="checked23" />
<!--                    <el-table-column label="采集开始时间" align="center" prop="startTime" width="180" v-if="checked6">
                        <template #default="scope">
                            <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {HH}:{mm}:{ss}') }}</span>
                        </template>
                    </el-table-column>-->
                    <!--<el-table-column label="采集结束时间" align="center" prop="endTime" width="180" v-if="checked7"/>-->
            <el-table-column label="卫星类型" align="center" prop="satelliteType" />
            <el-table-column label="传感器类型" align="center" prop="sensorType" />
            <el-table-column label="收藏时间" align="center" prop="createTime" width="180" />
          <!--<el-table-column label="接收站ID" align="center" prop="receivingId" v-if="checked10"/>-->
          <!--<el-table-column label="景Path" align="center" prop="viewPath" v-if="checked11"/>-->
          <!--<el-table-column label="景Row" align="center" prop="viewRow" v-if="checked12"/>-->
          <!--<el-table-column label="星下点Path" align="center" prop="starPath" v-if="checked13"/>-->
          <!--<el-table-column label="星下点Row" align="center" prop="starRow" v-if="checked14"/>-->
          <!--<el-table-column label="TopLeftLongitude" align="center" prop="leftupLongitude" v-if="checked15"/>-->
          <!--<el-table-column label="BottomLeftLongitude" align="center" prop="leftdownLongitude" v-if="checked16"/>-->
          <!--<el-table-column label="TopRightLongitude" align="center" prop="rightupLongitude" v-if="checked17"/>-->
          <!--<el-table-column label="BottomRightLongitude" align="center" prop="rightdownLongitude" v-if="checked18"/>-->
          <!--<el-table-column label="TopLeftLatitude" align="center" prop="leftupLatitude" v-if="checked19"/>-->
          <!--<el-table-column label="BottomLeftLatitude" align="center" prop="leftdownLatitude" v-if="checked20"/>-->
          <!--<el-table-column label="TopRightLatitude" align="center" prop="rightupLatitude" v-if="checked21"/>-->
          <!--<el-table-column label="BottomRightLatitude" align="center" prop="rightdownLatitude" v-if="checked22"/>-->
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
                <template #default="scope">
                  <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                             >移除
                  </el-button>
                  <el-button link type="primary" icon="Message" @click="Detail(scope.row)"
                               >详情
                    </el-button>
                  <el-button link type="primary" icon="Check" @click="handleProcess(scope.row)"
                             >处理分析
                  </el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />
    </div>
</template>

<script setup name="Info">
    import {
        listInfo,
        getInfo,
        delInfo} from "@/api/collection/collection";
    import { ElMessageBox } from 'element-plus'
    import useAppStore from '@/store/modules/app'
    import { useRouter } from "vue-router";
    const router = useRouter();

    const {proxy} = getCurrentInstance();

    const dialogTableVisible = ref(false);
    const infoList = ref([]);
    const open = ref(false);
    const dataList = ref([]);
    const checked1 = ref(false);
    const checked2 = ref(true);
    const checked3 = ref(true);
    const checked4 = ref(true);
    const checked5 = ref(true);
    const checked6 = ref(false);
    const checked7 = ref(false);
    const checked8 = ref(true);
    const checked9 = ref(true);
    const checked10 = ref(false);
    const checked11 = ref(false);
    const checked12 = ref(false);
    const checked13 = ref(false);
    const checked14 = ref(false);
    const checked15 = ref(false);
    const checked16 = ref(false);
    const checked17 = ref(false);
    const checked18 = ref(false);
    const checked19 = ref(false);
    const checked20 = ref(false);
    const checked21 = ref(false);
    const checked22 = ref(false);
    const checked23 = ref(true);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref("");
    const nameList = ref([]);

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
                name: null,
                image: null,
                region: null,
                range: null,
                startTime: null,
                endTime: null,
                satelliteType: null,
                sensorType: null,
    },
    rules: {
    }});

    const {queryParams} = toRefs(data);

    /** 查询收藏列表 */
    function getList() {
      loading.value = true;
      listInfo(queryParams.value).then((response) => {
        infoList.value = response.rows;
        total.value = response.total;
        loading.value = false;
      });
    }

    // 定义一个方法
    const formatTo2Decimals = (value) => parseFloat(value).toFixed(2);
    // 多选框选中数据
    function handleSelectionChange(selection) {
      dataList.value = selection;
      ids.value = selection.map((item) => item.id);
      single.value = selection.length != 1;
      multiple.value = !selection.length;
    }

    // 删除操作
    function handleDelete(row) {
      const _ids = row.id || ids.value;
      const imageName = row.satelliteType + "_" + row.sensorType + "_" + row.collectTime;
      const _names = imageName;
      proxy.$modal
          .confirm("确认删除吗？")
          .then(function () {
            return delInfo(_ids);
          })
          .then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
          })
          .catch(() => {});
    }



    // 打印方法
    function handlePrint() {
      window.print();
      }


      // 提示
    function handleCue() {
      ElMessageBox.alert(
          '有问题请联系178xxxxxxxx',
          '提示',
          {
            confirmButtonText: 'OK',
            type: "info",
          }
      )
    }


    // 处理时间格式,将日期格式从 yyyy-MM-dd hh:mm:ss 转换为 yyyy-MM-dd
    function formatCollectTime(date) {
      const d = new Date(date);
      // 返回正确的日期字符串格式
      return `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`;
    }

    /* 批量处理分析 */
    function batchProcess() {
      useAppStore().setMenuHide(false);
      const rowData = JSON.stringify(dataList.value);
      router.push({
        path: "/aiProcess",
        state: { rowData }
      });
    }

    /* 单个处理分析 */
    function handleProcess(row) {
      useAppStore().setMenuHide(false);
      const rowData = JSON.stringify(row);
      router.push({
        path: "/aiProcess",
        state: { rowData }
      });
    }

    const DetailRow = ref(null);
    // 详情
    function Detail(row) {
      const _ids = row.id || ids.value;
      getInfo(_ids).then((response) => {
        DetailRow.value = response.data;
        dialogTableVisible.value = true;
      })
    }

    // 取消按钮
    function cancel() {
    }

    /** 导出按钮操作 */
    function handleExport2() {
        proxy.download('collection/info/export', {
            ...queryParams.value
        }, `table__${new Date().getTime()}.xls`)
    }

    function handleExport1() {
      proxy.download('collection/info/export', {
        ...queryParams.value
      }, `table_${new Date().getTime()}.csv`)
    }

    getList();
</script>

<style>
.top-button{
  margin-bottom: 10px;
}

.button-right{
  position: absolute;
  right: 17px;
  top: 0;
}

@media print {
  .navbar{
    display: none !important;
  }

}
</style>
