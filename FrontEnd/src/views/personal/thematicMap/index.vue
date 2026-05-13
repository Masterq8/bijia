<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="专题图类别" prop="imageCategory">
        <el-input
                v-model="queryParams.imageCategory"
                placeholder="请输入专题图类别"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
        >新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="thematicList">
      <el-table-column label="专题图模板" align="center" prop="image">
        <template #default="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="专题图类别" align="center" prop="imageCategory" />
      <el-table-column label="类别编码" align="center" prop="categoryCode" />
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" >修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="thematicRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="专题图类别" prop="imageCategory">
          <el-input v-model="form.imageCategory" placeholder="请输入专题图类别" />
        </el-form-item>
        <el-form-item label="类别编码" prop="categoryCode">
          <el-input v-model="form.categoryCode" placeholder="请输入类别编码" />
        </el-form-item>
        <el-form-item label="专题图模板" prop="image">
          <image-upload v-model="form.image" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="thematic">
    import { thematicMapList, getThematic, deleteThematicMap, addThematicMap, updateThematic } from "@/api/thematicMap/thematicMap";

    const { proxy } = getCurrentInstance();

    const thematicList = ref([]);
    const open = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref("");
    const dateRange = ref([]);

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            imageCategory: undefined
        },
        rules: {
            imageCategory: [{ required: true, message: "专题图类别不能为空", trigger: "blur" }],
            categoryCode: [{ required: true, message: "类别编码不能为空", trigger: "blur" }],
            image: [{ required: true, message: "专题图模板不能为空", trigger: "blur" }]
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询参数列表 */
    function getList() {
        loading.value = true;
        thematicMapList(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
            thematicList.value = response.rows;
            total.value = response.total;
            loading.value = false;
        });
    }

    /** 取消按钮 */
    function cancel() {
        open.value = false;
        reset();
    }

    /** 表单重置 */
    function reset() {
        form.value = {
            id: undefined,
            imageCategory: undefined,
            categoryCode: undefined,
            image: undefined
        };
        proxy.resetForm("thematicRef");
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        dateRange.value = [];
        proxy.resetForm("queryRef");
        handleQuery();
    }

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        open.value = true;
        title.value = "添加专题图类别模板";
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const id = row.id
        getThematic(id).then(response => {
            form.value = response.data;
            open.value = true;
            title.value = "修改专题图类别模板";
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs["thematicRef"].validate(valid => {
            if (valid) {
                if (form.value.id != undefined) {
                    updateThematic(form.value).then(response => {
                        proxy.$modal.msgSuccess("修改成功");
                        open.value = false;
                        getList();
                    });
                } else {
                    addThematicMap(form.value).then(response => {
                        proxy.$modal.msgSuccess("新增成功");
                        open.value = false;
                        getList();
                    });
                }
            }
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const id = row.id;
        proxy.$modal.confirm('是否确认删除类别名称为"' + row.imageCategory + '"的数据项？').then(function () {
            return deleteThematicMap(id);
        }).then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    }

    getList();
</script>
