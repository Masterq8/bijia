<template>
  <div class="top-button">
    <div id="printArea"></div>
    <!-- <el-row>
      <div class="button-left" style="margin: 20px;">
        <el-col :span="1.5">
          <el-button type="primary" @click="handleExport1(0)">获取选中行数据</el-button>
          <el-button type="primary" @click="handleExport1(1)">获取当前页数据</el-button>
        </el-col>
      </div>
      <div class="button-right" style="margin: 20px;">
        <el-dropdown :hide-on-click="false">
          <el-button icon="Operation" circle style="margin-right: 12px;" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <el-checkbox v-model="checked1" label="订单名称" />
              </el-dropdown-item>
              <el-dropdown-item>
                <el-checkbox v-model="checked2" label="订单详情" />
              </el-dropdown-item>
              <el-dropdown-item>
                <el-checkbox v-model="checked3" label="订单状态" />
              </el-dropdown-item>
              <el-dropdown-item>
                <el-checkbox v-model="checked4" label="价格" />
              </el-dropdown-item>
              <el-dropdown-item>
                <el-checkbox v-model="checked5" label="下单时间" />
              </el-dropdown-item>
              <el-dropdown-item>
                <el-checkbox v-model="checked6" label="订单编号" />
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button icon="Download" circle @click="handleExport1(1)" />
        <el-button icon="Printer" circle @click="handlePrint" />
        <el-button icon="InfoFilled" circle @click="centerDialogVisible = true" />
      </div>
    </el-row> -->

    <el-form :inline="true" ref="queryRef" :model="queryParams" class="demo-form-inline" >
      <el-form-item label="订单名称"  prop="orderName" >
        <el-input v-model="queryParams.orderName" placeholder="请输入" clearable @input="handleQuery"/>
      </el-form-item>

      <el-form-item label="订单类别"  prop="category" >
        <el-select
                v-model="queryParams.category"
                placeholder="请选择" clearable @change="handleQuery">
          <el-option label="数据包" value="AIP" />
          <el-option label="专题图" value="GTM" />
        </el-select>
      </el-form-item>

      <el-form-item label="订单状态"  prop="state" >
        <el-select
                v-model="queryParams.state"
                placeholder="请选择" clearable @change="handleQuery">
          <el-option label="生成中" value="0" />
          <el-option label="已完成" value="1" />
          <el-option label="生成失败" value="2" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>

    <el-table
      v-loading="loading"
      :data="orderManagementList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="id主键" align="center" prop="id"/> -->
      <el-table-column label="订单编号" align="center" prop="orderNumber" width="260" />
      <el-table-column label="订单名称" align="center" prop="orderName" width="200" />
      <el-table-column label="订单类别" align="center" prop="category" width="120" >
        <template #default="scope">
          <dict-tag :options="orders_category" :value="scope.row.category" />
        </template>
      </el-table-column>
      <el-table-column label="订单详情" align="center" prop="orderDetail" />
      <el-table-column label="订单状态" align="center" prop="stateName" width="150">
        <template #default="scope">
<!--          <dict-tag-->
<!--            :options="orders_state"-->
<!--            :value="scope.row.state"-->
<!--          />-->

          {{scope.row.stateName}}
        </template>
      </el-table-column>

      <el-table-column
        label="价格（元）"
        align="center"
        prop="price"
        :formatter="formatPrice"
        v-if="checked4"
        width="130"
      />
      <el-table-column label="下单时间" align="center" prop="orderTime" width="200" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button v-if="scope.row.category == 'GTM' && scope.row.stateName == '已完成'" link type="primary" icon="view" @click="handleView(scope.row)">预览</el-button>
          <el-button :loading="scope.row.isLoading" link type="primary" icon="Edit" @click="downloadOrder(scope.row)">下载</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button v-if="scope.row.category == 'AIP'" link type="primary" icon="Edit" @click="generateGTM(scope.row)">生成专题图</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 提醒对话框 -->
    <el-dialog v-model="centerDialogVisible" title="信息" width="500" align-center>
      <span>如有问题请联系1777782xxxx</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="centerDialogVisible = false">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="viewVisible" title="预览" width="550" height="650" align-center>
      <template #default="scope">
        <el-image :src="thumbnailUrl" :width="500" :height="600"/>
      </template>
    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup  >
import {
  listOrderManagement2,
  getOrderManagement,
  delOrderManagement,
  addOrderManagement,
  updateOrderManagement,
  generateGTMOrder
} from "@/api/order/orderManagement";
import axios from 'axios'
import { getToken } from '@/utils/auth'
import { blobValidate } from '@/utils/ruoyi'
import { saveAs } from 'file-saver'
import {ref, watch,computed, watchEffect} from "vue";
import useWebSocket from '@/utils/websocket';
import useUserStore from '@/store/modules/user'
// const userStore = useUserStore();
// const name = userStore.$state.name
const baseURL = import.meta.env.VITE_APP_BASE_API
//const { socket, isConnected, sendMessage } = useWebSocket(import.meta.env.VITE_APP_WS_URL + name)
const viewVisible = ref(false);
const thumbnailUrl = ref('');



//监听自定义事件
// watchEffect(() => {
//   console.log(socket.value)
//   socket.value?.addEventListener('message', (res) => {
//     console.log(res)
//     if (res.includes("生成进度")) {
//       let id = res.substring(0, res.indexOf(";"));
//       let socketMsg = res.substring(res.indexOf(";")+1, res.length);
//       console.log(id);
//       console.log(socketMsg);
//
//     }
//      getList();
//   });
// });


const { proxy } = getCurrentInstance();

const centerDialogVisible = ref(false);
const checked1 = ref(true);
const checked2 = ref(true);
const checked3 = ref(true);
const checked4 = ref(true);
const checked5 = ref(true);
const checked6 = ref(true);
const visible = ref(false);
const orderManagementList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const selectedItems = ref([]);
const formatPrice = function(row, column) {
  return row.price + "元";
};
const {orders_state} = proxy.useDict("orders_state");

const orders_category = ref([{label: '专题图',value: 'GTM'}, {label: '数据包',value: 'AIP'}])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    ids: [],
    orderName: '',
    category: '',
    state: ''
  },
  rules: {}
});
const { queryParams, form, rules } = toRefs(data);
const jinduobj = computed(() => {

  return useUserStore().jinduobj;
})

const isgenxinnum = computed(() => {
  //更新
  return Number(useUserStore().isgenxin);

})

function setlistdata(){
  for (let i = 0; i < jinduobj.value.length; i++) {
    let index1= orderManagementList.value.findIndex(item => { return item.id == jinduobj.value[i].id });
    if(index1!==-1){
      if(orderManagementList.value[index1].stateName.includes('生成')){
        orderManagementList.value[index1].stateName=jinduobj.value[i].socketMsg;
      }

    }
  }
}
watch(
    () => isgenxinnum,
    (newValue, oldValue) => {
      //debugger
      if (newValue) {
      getList()
      }

    },
    { deep: true, immediate: true }
);


watch(jinduobj, (value) => {
  setlistdata()
}, { deep: true, immediate: true });

/** 查询订单管理列表 */
function getList() {
  loading.value = true;
  listOrderManagement2(queryParams.value).then(response => {
    //debugger
    orderManagementList.value = response.rows;
    setlistdata()
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;

};

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
}

// 触发打印的方法
function handlePrint() {

    const printArea = document.getElementById('printArea');
      printArea.innerHTML = ''; // 清空打印区域

      if (selectedItems.value.length === 0) {
        alert('请选择要打印的数据');
        return;
      }

      const table = document.createElement('table');
      const thead = document.createElement('thead');
      const tbody = document.createElement('tbody');

      // 创建表头
      const headerRow = document.createElement('tr');
      headerRow.innerHTML = `
        <th>产品订单名称</th>
        <th>订单类别</th>
        <th>订单详情</th>
        <th>订单状态</th>
        <th>价格</th>
        <th>下单时间</th>
        <th>订单编号</th>
      `;
      thead.appendChild(headerRow);
      console.log(selectedItems.value);
      // 创建表体
      selectedItems.value.map(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td>${item.thumbnail}</td>
          <td>${item.category}</td>
          <td>${item.orderDetail}</td>
          <td>${item.stateName}</td>
          <td>${item.price}</td>
          <td>${item.orderTime}</td>
          <td>${item.orderNumber}</td>
        `;
        tbody.appendChild(row);
      });

      table.appendChild(thead);
      table.appendChild(tbody);
      printArea.appendChild(table);


      // 创建打印样式表
      const printStyle = document.createElement('style');
      printStyle.type = 'text/css';
      printStyle.media = 'print';
      printStyle.innerHTML = `
        body * {
          visibility: hidden;
        }
        #printArea, #printArea * {
        border: 1px solid #000;
          visibility: visible;
          margin: 0px;
        }
        #printArea {
        border: 1px solid #000;
          position: absolute;
          left: 0;
          top: 0;
        }
      `;
      document.head.appendChild(printStyle);

      // 打印
      window.print();

      // 移除打印样式表
      document.head.removeChild(printStyle);

      // 清理打印区域
      printArea.innerHTML = '';
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
    orderName: null,
    orderNumber: null,
    orderDetail: null,
    price: null,
    orderTime: null,
    createTime: null,
    createBy: null,
    updateTime: null,
    updateBy: null,
    image: null,
    isShow: null,
    title: null,
    prodId: null,
    category: null,
    stateName: null
  };
  proxy.resetForm("orderManagementRef");
}

// 多选框选中数据
function handleSelectionChange(selection) {
  queryParams.value.ids = selection.map(item => item.id);
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
  selectedItems.value = selection.map(item => item);
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加订单管理";
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || ids.value;
  proxy.$confirm(
      '是否确认删除订单管理编号为"' + row.orderNumber + '"的数据项?',
      "警告",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    )
    .then(function() {
      return delOrderManagement(ids);
    })
    .then(() => {
      getList();
    });
}

function handleView(row) {
    thumbnailUrl.value = row.thumbnailUrl
    viewVisible.value = true
}

// 生成专题图
function generateGTM(row) {
    proxy.$confirm(
        '确认将编号为【' + row.orderNumber + '】的订单生成专题图吗?',
        "警告",
        {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
        }
    ).then(function() {
        generateGTMOrder({id:row.id}).then((response) => {
            if (response.code == 200) {
                proxy.$message.success(response.msg);
            } else {
                proxy.$message.error(response.msg);
            }
        });
    }).then(() => {
        getList();
    });
}

function downloadOrder(row) {
  const id = row.id
  row.isLoading = true
    var url = baseURL + "/order/orderInfo/download?id=" +id
    axios({
        method: 'get',
        url: url,
        responseType: 'blob',
        headers: { 'Authorization': 'Bearer ' + getToken() }
    }).then((res) => {
        const isBlob = blobValidate(res.data);
        if (isBlob) {
            const blob = new Blob([res.data])
            saveAs(blob, decodeURIComponent(res.headers['download-filename']))
        } else {
            proxy.$message.error(res.data);
        }
    }).finally(()=>{
      row.isLoading = false
    })
}

/** 复选框选中数据 */
// function handle$ {
//     subClassName
// }

// SelectionChange(selection)
// {
//     checked$
//     {
//         subClassName
//     }
//     value = selection.map(item => item.index
// )
// }

/** 导出按钮操作 */
// function handleExport() {
//   queryParams.value.ids = orderManagementList.value.map(item => item.id)
//   console.log(queryParams.value)
//   proxy.download(
//     "orderManagement/orderManagement/export",
//     {
//       ...queryParams.value
//     },
//     `orderManagement_${new Date().getTime()}.xlsx`
//   );
// }
function handleExport1(n) {
  if(n===1){
  queryParams.value.ids = orderManagementList.value.map(item => item.id)
  }
  proxy.download(
    "orderManagement/orderManagement/export1",
    {
      ...queryParams.value
    },
    `orderManagement_${new Date().getTime()}.xlsx`
  );
}

getList();
</script>

<style scoped>
.top-button {
  padding: 20px;
}

.button-right {
  position: absolute;
  right: 17px;
  top: 0;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>
