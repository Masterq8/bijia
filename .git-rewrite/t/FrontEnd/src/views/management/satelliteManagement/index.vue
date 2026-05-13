<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
            <el-form-item label="省份" prop="province">
                <el-input
                        v-model="queryParams.province"
                        placeholder="请输入省份"
                        clearable
                        @input="handleQuery"
                />
            </el-form-item>
            <el-form-item label="城市" prop="city">
                <el-input
                        v-model="queryParams.city"
                        placeholder="请输入城市"
                        clearable
                        @input="handleQuery"
                />
            </el-form-item>
            <el-form-item label="传感器类型" prop="sensorType">
                <el-input
                        v-model="queryParams.sensorType"
                        placeholder="请输入传感器类型"
                        clearable
                        @input="handleQuery"
                />
            </el-form-item>
            <el-form-item label="卫星类型" prop="satelliteType">
                        <el-tree-select
                          v-model="queryParams.satelliteType"
                          :data="dataType"
                          filterable
                          style="width: 240px" clearable @change="handleQuery"
                          :props="{ value: 'dictValue', children: 'children' }"
                        />
                    </el-form-item>

            <!--<el-form-item label="采集时间" prop="collectTime">
                <el-date-picker clearable
                                v-model="queryParams.collectTime"
                                type="date"
                                value-format="YYYY-MM-DD hh:mm:ss"
                                placeholder="请选择采集时间">
                </el-date-picker>
            </el-form-item>-->
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
                        v-hasPermi="['map:satelliteManagement:add']"
                >新增
                </el-button>
            </el-col>
            <!--<el-col :span="1.5">-->
                <!--<el-button-->
                        <!--type="danger"-->
                        <!--plain-->
                        <!--icon="Delete"-->
                        <!--:disabled="multiple"-->
                        <!--@click="handleDelete"-->
                        <!--v-hasPermi="['map:satelliteManagement:remove']"-->
                <!--&gt;删除-->
                <!--</el-button>-->
            <!--</el-col>-->
            <!--<el-col :span="1.5">-->
                <!--<el-button-->
                        <!--type="warning"-->
                        <!--plain-->
                        <!--icon="Download"-->
                        <!--@click="handleExport"-->
                        <!--v-hasPermi="['map:satelliteManagement:export']"-->
                <!--&gt;导出-->
                <!--</el-button>-->
            <!--</el-col>-->
            <!-- <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar> -->
        </el-row>

        <el-table v-loading="loading" :data="satelliteList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <!--<el-table-column label="主键" align="center" prop="id"/>-->
            <!--<el-table-column label="地址" align="center" prop="region"/>-->
            <el-table-column label="影像速览" align="center" prop="image" width="100">
                <template #default="scope">
                    <image-preview :src="scope.row.image" :width="50" :height="50"/>
                </template>
            </el-table-column>
            <el-table-column label="卫星类型" align="center" prop="satelliteType"/>
            <el-table-column label="省份" align="center" prop="province" width="120"/>
            <el-table-column label="城市" align="center" prop="city" width="120"/>
            <el-table-column label="传感器类型" align="center" prop="sensorType"/>
            <!--<el-table-column label="景path" align="center" prop="viewPath"/>-->
            <!--<el-table-column label="景row" align="center" prop="viewRow"/>-->
            <!--<el-table-column label="星下点path" align="center" prop="starPath"/>-->
            <!--<el-table-column label="星下点row" align="center" prop="starRow"/>-->
            <el-table-column label="左上角经/维度" align="center" prop="leftupLongitude">
                <template #default="scope">
                    <span>{{ scope.row.leftupLongitude }},{{scope.row.leftupLatitude}}</span>
                </template>
            </el-table-column>
            <!--<el-table-column label="左上角纬度" align="center" prop="leftupLatitude"/>-->
            <el-table-column label="左下角经/纬度" align="center" prop="leftdownLongitude">
                <template #default="scope">
                    <span>{{ scope.row.leftdownLongitude }},{{scope.row.leftdownLatitude}}</span>
                </template>
            </el-table-column>
            <!--<el-table-column label="左下角纬度" align="center" prop="leftdownLatitude"/>-->
            <el-table-column label="右上角经/纬度" align="center" prop="rightupLongitude">
                <template #default="scope">
                    <span>{{ scope.row.rightupLongitude }},{{scope.row.rightupLatitude}}</span>
                </template>
            </el-table-column>
            <!--<el-table-column label="右上角纬度" align="center" prop="rightupLatitude"/>-->
            <el-table-column label="右下角经/纬度" align="center" prop="rightdownLongitude">
                <template #default="scope">
                    <span>{{ scope.row.rightdownLongitude }},{{scope.row.rightdownLatitude}}</span>
                </template>
            </el-table-column>
            <!--<el-table-column label="右下角纬度" align="center" prop="rightdownLatitude"/>-->
            <el-table-column label="采集时间" align="center" prop="collectTime" width="160"/>
            <el-table-column label="操作" align="center" width="150">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['map:satelliteManagement:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['map:satelliteManagement:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination
                v-show="total>0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加卫星影像管理对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" append-to-body @close="cancel">
            <el-form ref="satelliteRef" :model="form" :rules="rules" label-width="120px">
                <el-row>
                    <!-- <el-col :span="8">
                        <el-form-item label="卫星类型" prop="satelliteType">
                            <el-tree-select
                                    v-model="form.satelliteType"
                                    :data="dataType"
                                    filterable
                                    style="width: 240px"
                                    :props="{ value: 'label', children: 'children' }"
                            />
                        </el-form-item>
                    </el-col> -->
                    <el-col :span="24">
                        <el-form-item label="采集时间" prop="collectTime">
                            <el-date-picker clearable
                                            v-model="form.collectTime"
                                            type="date"
                                            value-format="YYYY-MM-DD"
                                            placeholder="请选择采集时间" style="width: 200px;">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="所属区域" key="form.province" prop="province">
                            <el-select v-model="selectedProvince" @change="handleProvinceChange" clearable placeholder="请选择省份" style="width:200px;">
                                <el-option
                                        v-for="province in options"
                                        :key="province.value"
                                        :label="province.label"
                                        :value="province.value"
                                ></el-option>
                            </el-select>
                            <el-select v-model="selectedCity" placeholder="请选择城市" clearable style="padding-left: 10px;width:210px;">
                                <el-option
                                        v-for="city in selectedProvinceChildren"
                                        :key="city.value"
                                        :label="city.label"
                                        :value="city.value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                    <!-- <el-col :span="8">
                        <el-form-item label="传感器类型" prop="sensorType">
                            <el-input v-model="form.sensorType" placeholder="请输入传感器类型"/>
                        </el-form-item>
                    </el-col> -->
                    <!--<el-col :span="8">-->
                        <!--<el-form-item label="源文件名称" prop="sourcefilename">-->
                            <!--<el-input v-model="form.sourcefilename" placeholder="请输入源文件名称"/>-->
                        <!--</el-form-item>-->
                    <!--</el-col>-->
                    <!-- <el-col :span="8">
                        <el-form-item label="分辨率" prop="resolution">
                            <el-tree-select
                                    v-model="form.resolution"
                                    :data="sys_ex_info"
                                    filterable
                                    style="width: 240px"
                                    :props="{ value: 'label', children: 'children' }"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="景path" prop="viewPath">
                            <el-input v-model="form.viewPath" placeholder="请输入景path"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="景row" prop="viewRow">
                            <el-input v-model="form.viewRow" placeholder="请输入景row"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="星下点path" prop="starPath">
                            <el-input v-model="form.starPath" placeholder="请输入星下点path"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="星下点row" prop="starRow">
                            <el-input v-model="form.starRow" placeholder="请输入星下点row"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左上角经度" prop="leftupLongitude">
                            <el-input v-model="form.leftupLongitude" placeholder="请输入左上角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左上角纬度" prop="leftupLatitude">
                            <el-input v-model="form.leftupLatitude" placeholder="请输入左上角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左下角经度" prop="leftdownLongitude">
                            <el-input v-model="form.leftdownLongitude" placeholder="请输入左下角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左下角纬度" prop="leftdownLatitude">
                            <el-input v-model="form.leftdownLatitude" placeholder="请输入左下角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右上角经度" prop="rightupLongitude">
                            <el-input v-model="form.rightupLongitude" placeholder="请输入右上角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右上角纬度" prop="rightupLatitude">
                            <el-input v-model="form.rightupLatitude" placeholder="请输入右上角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右下角经度" prop="rightdownLongitude">
                            <el-input v-model="form.rightdownLongitude" placeholder="请输入右下角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右下角纬度" prop="rightdownLatitude">
                            <el-input v-model="form.rightdownLatitude" placeholder="请输入右下角纬度"/>
                        </el-form-item>
                    </el-col>

                    -->
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="其他信息" prop="remark">
                            <el-input type="textarea" :rows="3" v-model="form.remark" placeholder="请输入其他信息" style="width: 80%;"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
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
                            <br>
                            <!--<div >-->
                                <!--<p class="upload-info">注：文件压缩较慢，请耐心等待。</p>-->
                                <!--<p class="upload-info">文件压缩成功后，会自动上传到卫星影像管理和我的数据，但没有省市信息，请自行添加。</p>-->
                            <!--</div>-->
                        </el-form-item>
                    </el-col>

                    <!-- <el-col :span="24">
                        <el-form-item label="卫星png图片" prop="image">
                            <image-upload v-model="form.image"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="卫星tiff源文件" prop="sourcefilepath">
                            <el-upload
                                    ref="uploadRef"
                                    :limit="1"
                                    accept=".tiff"
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
                            >
                                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                                <div class="el-upload__tip" slot="tip">只能上传tiff文件</div>
                            </el-upload>
                        </el-form-item>
                    </el-col> -->
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
                    <el-button style="margin-left: 200px" type="primary" @click="handleAdd1()">解压并上传</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>

            <!-- 修改卫星影像管理对话框 -->
            <el-dialog :title="title" v-model="editOpen" width="1000px" append-to-body @close="cancel">
            <el-form ref="satelliteRef" :model="form" :rules="rules" label-width="120px">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="采集时间" prop="collectTime">
                            <el-date-picker clearable
                                            v-model="form.collectTime"
                                            type="date"
                                            value-format="YYYY-MM-DD"
                                            placeholder="请选择采集时间">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="所属区域-省" key="form.province" prop="province">
                            <el-select v-model="selectedProvince" @change="handleProvinceChange" placeholder="请选择省份">
                                <el-option
                                        v-for="province in options"
                                        :key="province.value"
                                        :label="province.label"
                                        :value="province.value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="市" key="form.city" prop="city">
                            <el-select v-model="selectedCity" placeholder="请选择城市">
                                <el-option
                                        v-for="city in selectedProvinceChildren"
                                        :key="city.value"
                                        :label="city.label"
                                        :value="city.value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="卫星类型" prop="satelliteType">
                            <el-tree-select
                                    v-model="form.satelliteType"
                                    :data="dataType"
                                    filterable
                                    style="width: 240px"
                                    :props="{ value: 'label', children: 'children' }"
                                    :disabled="true"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="传感器类型" prop="sensorType">
                            <el-input v-model="form.sensorType" :disabled="true" placeholder="请输入传感器类型"/>
                        </el-form-item>
                    </el-col>
                    <!--<el-col :span="8">-->
                        <!--<el-form-item label="源文件名称" prop="sourcefilename">-->
                            <!--<el-input v-model="form.sourcefilename" placeholder="请输入源文件名称"/>-->
                        <!--</el-form-item>-->
                    <!--</el-col>-->
                    <el-col :span="8">
                        <el-form-item label="分辨率" prop="resolution">
                            <el-tree-select
                                    v-model="form.resolution"
                                    :data="sys_ex_info"
                                    filterable
                                    style="width: 240px"
                                    :props="{ value: 'label', children: 'children' }"
                                    :disabled="true"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="景path" prop="viewPath">
                            <el-input v-model="form.viewPath" :disabled="true" placeholder="请输入景path"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="景row" prop="viewRow">
                            <el-input v-model="form.viewRow" :disabled="true" placeholder="请输入景row"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="星下点path" prop="starPath">
                            <el-input v-model="form.starPath" :disabled="true" placeholder="请输入星下点path"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="星下点row" prop="starRow">
                            <el-input v-model="form.starRow" :disabled="true" placeholder="请输入星下点row"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左上角经度" prop="leftupLongitude">
                            <el-input v-model="form.leftupLongitude" :disabled="true" placeholder="请输入左上角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左上角纬度" prop="leftupLatitude">
                            <el-input v-model="form.leftupLatitude" :disabled="true" placeholder="请输入左上角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左下角经度" prop="leftdownLongitude">
                            <el-input v-model="form.leftdownLongitude" :disabled="true" placeholder="请输入左下角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="左下角纬度" prop="leftdownLatitude">
                            <el-input v-model="form.leftdownLatitude" :disabled="true" placeholder="请输入左下角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右上角经度" prop="rightupLongitude">
                            <el-input v-model="form.rightupLongitude" :disabled="true" placeholder="请输入右上角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右上角纬度" prop="rightupLatitude">
                            <el-input v-model="form.rightupLatitude" :disabled="true" placeholder="请输入右上角纬度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右下角经度" prop="rightdownLongitude">
                            <el-input v-model="form.rightdownLongitude" :disabled="true" placeholder="请输入右下角经度"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="右下角纬度" prop="rightdownLatitude">
                            <el-input v-model="form.rightdownLatitude" :disabled="true" placeholder="请输入右下角纬度"/>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="其他信息" prop="exInfo">
                            <el-input type="textarea" :rows="3" v-model="form.exInfo" placeholder="请输入其他信息"/>
                        </el-form-item>
                    </el-col>
                    <!-- <el-col :span="24">
                        <el-form-item label="卫星png图片" prop="image">
                            <image-upload v-model="form.image" :disabled="true"/>
                            <div>图片成功上传，无法修改</div>
                        </el-form-item>
                    </el-col>
                     -->
                     <el-col :span="24">
                            <el-form-item label="卫星png图片" prop="image">
                                <img
                                    :src="form.image"
                                    class="clickable-image"
                                    @click="showPreview(form.image)"
                                />
                            </el-form-item>
                        </el-col>

                    <!-- 预览模态框 -->
                    <el-dialog title="图片预览" v-model="dialogVisible">
                        <img :src="currentImage" style="width: 100%;" />
                        <span slot="footer" class="dialog-footer">
                            <el-button @click="dialogVisible = false">关闭</el-button>
                        </span>
                    </el-dialog>

                    <el-col :span="24">
                        <el-form-item label="卫星tiff源文件" prop="sourcefilepath">
                            <el-upload
                                    ref="uploadRef"
                                    :limit="1"
                                    accept=".tiff,.tif"
                                    :headers="upload.headers"
                                    :action="upload.url"
                                    :disabled="true"
                                    :on-change="handleChange"
                                    :on-progress="handleFileUploadProgress"
                                    :on-success="handleFileSuccess"
                                    :on-exceed="handleExceed"
                                    :auto-upload="true"
                                    :file-list="fileList"
                                    :list-type="listType"
                                    drag
                            >
                                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                                <div>文件成功上传，无法修改</div>
                                <!-- <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                                <div class="el-upload__tip" slot="tip">只能上传 tif/tiff 文件</div> -->
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancelEdit">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Satellite">
    import {
        listSatellite,
        getSatellite,
        delSatellite,
        addSatellite,
        updateSatellite } from "@/api/map/satellite";
    import {addInfo1 } from "@/api/record/info";
    import { getToken } from "@/utils/auth";
    import axios from 'axios';
    import {ref} from "vue";
    import { getMenuInfoList } from "@/api/Data/DataInfo.js";

    const {proxy} = getCurrentInstance();
        const { sys_resource_series, sys_high_score, sys_ex_info, sys_jilin_series, sys_vehicle_series,sys_data_series } = proxy.useDict('sys_resource_series', 'sys_high_score','sys_ex_info', 'sys_jilin_series', 'sys_vehicle_series','sys_data_series');

    const satelliteList = ref([]);
    const open = ref(false);
    const editOpen = ref(false);
    const dialogVisible = ref(false);
    const currentImage = ref('');
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref("");
    const dataType = ref([]);
    // 待上传
    const fileList = ref([]);
    const listType = ref("text");
    const options = ref([]);
    const selectedProvince = ref('');
    const selectedCity = ref('');
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
    const uploadRef = ref(null);
    // 检索区域省市选择方法
    const parseCitiesData = (data) => {
      const provinces = data.map(item => ({
        value: item.label,
        label: item.label,
        children: item.children.map(child => ({
          value: child.label,
          label: child.label
        }))
      }));
      options.value = provinces;
    };
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
    };
    // 上传文件列表的改变事件
    const handleChange = (file, fileList1) => {
        console.log("上传文件改变")
        fileList.value = fileList1;
    };
    // 重新上传提醒
    function handleExceed(){
        proxy.$message.warning("已存在卫星tiff源文件，请删除后重新上传");
    }
// 计算属性，动态生成城市选项
const selectedProvinceChildren = computed(() => {
  if (!selectedProvince.value) return [];
  const province = options.value.find(opt => opt.value === selectedProvince.value);
  return province?.children || [];
});

// 组件挂载后获取省市数据
onMounted(async () => {
  try {
    const response = await axios.get('/resData/city.json');
    const citiesData = response.data;
    parseCitiesData(citiesData);
  } catch (error) {
    console.error('Error fetching city data:', error);
  }
});

// 监听省份选择变化
watch(selectedProvince, (newProvince) => {
     form.value.province = newProvince; // 存储省份名称
     form.value.provinceCode = options.value.find(opt => opt.value === newProvince)?.value; // 存储省份编码
    if (isProvinceManuallySelected.value) {
        // 清空城市选择
        selectedCity.value = '';
        form.value.city = '';
        form.value.cityCode = '';
    }
    isProvinceManuallySelected.value = true; // 设置标识符为 true
});

// 监听城市选择变化
watch(selectedCity, (newCity) => {
  form.value.city = newCity; // 存储城市名称
  form.value.cityCode = selectedProvinceChildren.value.find(city => city.value === newCity)?.value; // 存储城市编码
});

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
                id: null,
                // region: null,
                viewPath: null,
                viewRow: null,
                starPath: null,
                starRow: null,
                leftupLongitude: null,
                leftupLatitude: null,
                leftdownLongitude: null,
                leftdownLatitude: null,
                rightupLongitude: null,
                rightupLatitude: null,
                rightdownLongitude: null,
                rightdownLatitude: null,
                sensorType: null,
                satelliteType: null,
                image: null,
                collectTime: null,
                province: null,
                city: null,
                resolution: null,
                sourcefilename: null,
                exInfo: null
    },
    rules: {
        sourcefilepath: [
            { required: true, message: "卫星tiff源文件不能为空", trigger: "blur" }
        ],
                /*region: [
                {
                    required: true, message: "地址不能为空", trigger: "blur" }
            ],*/
            //     viewPath: [
            //     {
            //         required: true, message: "景path不能为空", trigger: "blur" }
            // ],
            //     viewRow: [
            //     {
            //         required: true, message: "景row不能为空", trigger: "blur" }
            // ],
            //     starPath: [
            //     {
            //         required: true, message: "星下点path不能为空", trigger: "blur" }
            // ],
            //     starRow: [
            //     {
            //         required: true, message: "星下点row不能为空", trigger: "blur" }
            // ],
            //     leftupLongitude: [
            //     {
            //         required: true, message: "左上角经度不能为空", trigger: "blur" }
            // ],
            //     leftupLatitude: [
            //     {
            //         required: true, message: "左上角纬度不能为空", trigger: "blur" }
            // ],
            //     leftdownLongitude: [
            //     {
            //         required: true, message: "左下角经度不能为空", trigger: "blur" }
            // ],
            //     leftdownLatitude: [
            //     {
            //         required: true, message: "左下角纬度不能为空", trigger: "blur" }
            // ],
            //     rightupLongitude: [
            //     {
            //         required: true, message: "右上角经度不能为空", trigger: "blur" }
            // ],
            //     rightupLatitude: [
            //     {
            //         required: true, message: "右上角纬度不能为空", trigger: "blur" }
            // ],
            //     rightdownLongitude: [
            //     {
            //         required: true, message: "右下角经度不能为空", trigger: "blur" }
            // ],
            //     rightdownLatitude: [
            //     {
            //         required: true, message: "右下角纬度不能为空", trigger: "blur" }
            // ],
            //     sensorType: [
            //     {
            //         required: true, message: "传感器类型不能为空", trigger: "blur" }
            // ],
            //     satelliteType: [
            //     {
            //         required: true, message: "卫星类型不能为空", trigger: "blur" }
            // ],
            //     image: [
            //     {
            //         required: true, message: "卫星png图片不能为空", trigger: "blur" }
            // ],
            //     sourcefilepath: [
            //     {
            //         required: true, message: "卫星tiff源文件不能为空", trigger: "blur" }
            // ],
            //     collectTime: [
            //     {
            //         required: true, message: "采集时间不能为空", trigger: "blur" }
            // ],
            //     province: [
            //     {
            //         required: true, message: "省份不能为空", trigger: "change" }
            // ],
            //     city: [
            //     {
            //         required: true, message: "城市不能为空", trigger: "change" }
            // ],
            // sourcefilename: [
            // {
            //     required: true, message: "源文件名称", trigger: "blur" }
            // ],
            // exInfo: [
            // {
            //     required: true, message: "其他信息", trigger: "blur" }
            // ],
            // resolution: [
            // {
            //     required: true, message: "分辨率", trigger: "change" }
            // ],

    },
    isProvinceManuallySelected: false  // 添加标识符
    })
    ;

    const {queryParams, form, rules,isProvinceManuallySelected} = toRefs(data);

    function getMenuList() {
  getMenuInfoList().then((res) => {
  console.log(res.data)
    if (res.code == 200) {
      dataType.value = res.data;
    }
  });
}

getMenuList();

    /** 查询卫星影像管理列表 */
    function getList() {
        loading.value = true;
        listSatellite(queryParams.value).then(response => {
        console.log(response);
        satelliteList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    })

    }

function showPreview(imageSrc) {
    dialogVisible.value = true;
    currentImage.value = imageSrc;

}
    // 取消按钮
    function cancel() {
        fileList.value = []
        open.value = false;
        reset();
    }
    function cancelEdit() {
        fileList.value = []
        editOpen.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
                id: null,
                // region: null,
                viewPath: null,
                viewRow: null,
                starPath: null,
                starRow: null,
                leftupLongitude: null,
                leftupLatitude: null,
                leftdownLongitude: null,
                leftdownLatitude: null,
                rightupLongitude: null,
                rightupLatitude: null,
                rightdownLongitude: null,
                rightdownLatitude: null,
                sensorType: null,
                satelliteType: null,
                sourcefilepath: null,
                image: null,
                collectTime: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                highScore: null,
                resourceSeries: null,
                jilin: null,
                vehicle: null,
                province: null,
                city: null
    };
    selectedProvince.value = '';
    selectedCity.value = '';
        proxy.resetForm("satelliteRef");
    }

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

    /** 多选框选中数据*/
    function handleSelectionChange(selection) {
        ids.value = selection.map(item => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
    }

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        open.value = true;
        title.value = "添加卫星影像";
    }

    import { ElLoading } from 'element-plus';
    function handleAdd1() {

        proxy.$refs["satelliteRef"].validate((valid) => {
        if (valid) {
            // 创建一个 Loading 实例
            const loadingInstance = ElLoading.service({
                lock: true,       // 锁定屏幕，禁止滚动
                text: '上传中，预计1~2分钟，请耐心等待...',
                background: 'rgba(0, 0, 0, 0.7)'  // 背景颜色
            });

            addInfo1(form.value).then((res) => {
                if (res.code == 200) {
                    // 关闭 Loading 实例
                    loadingInstance.close();
                    proxy.$modal.msgSuccess("上传成功");
                    open.value = false;
                    getList();
                }
            });
        }
     }

    )};

// 修改按钮操作
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getSatellite(_id).then(response => {
        form.value = response.data;
        // 设置省份
        if (response.data.province) {
            selectedProvince.value = response.data.province;
            // 立即设置城市，这样就不会被省份的 watch 清空
            if (response.data.city) {
                selectedCity.value = response.data.city;
            }
        }
        editOpen.value = true;
        title.value = "修改卫星影像";
        fileList.value = [{name: row.sourcefilename, url: row.sourcefilepath}];
        isProvinceManuallySelected.value = false; // 设置标识符为 false
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['satelliteRef'].validate(valid => {
        if (valid) {
            if (form.value.sourcefilename == null || form.value.sourcefilename == '' || form.value.sourcefilename == undefined) {
                proxy.$message.warn('卫星tiff源文件未上传完成，请在出现绿色对号后，再【确定】');
                return
            }
            if (form.value.id !== null) {
                updateSatellite(form.value).then(response => {
                    proxy.$message.success('修改成功');
                    editOpen.value = false;
                    getList();
                });
            } else {
                addSatellite(form.value).then(response => {
                    proxy.$message.success('新增成功');
                    open.value = false;
                    getList();
                });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _id = row.id || ids.value;
    proxy.$confirm(`是否确认删除当前卫星影像数据？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        return delSatellite(_id);
    }).then(() => {
        getList();
        proxy.$message.success('删除成功');
    }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download('map/satellite/export', {
        ...queryParams.value
    }, `satellite_${new Date().getTime()}.xlsx`);
}

    getList();
</script>

<style>
.select-width {
  width: 150px; /* 根据需要调整宽度 */
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
.clickable-image {
    width: 200px;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s; /* 添加过渡效果 */
}

.clickable-image:hover {
    transform: scale(1.05); /* 鼠标悬停时放大 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 添加阴影效果 */
}
</style>
