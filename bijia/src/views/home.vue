<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useHomeStore } from '@/store/home'
import MapPanel from '@/components/home/MapPanel.vue'
import { cityMap, provinceOptions } from '@/data/regions'
import { detailFields, mockSatelliteList } from '@/data/homeMock'
import { thematicTypes } from '@/data/thematicTypes'
import { getToken } from '@/utils/auth'
import { listSatellite1, uploadFile } from '@/api/map/satellite'

const homeStore = useHomeStore()

const mapPanelRef = ref()
const treeRef = ref()
const activeMenu = ref('map')
const drawingMode = ref(false)
const searchMode = ref('region')
const currentPage = ref(1)
const pageSize = ref(3)
const activeRangeKey = ref('halfYear')
const checkedKeys = ref(['gaofen', 'ziyuan', 'jilin', 'uav'])
const drawnPolygon = ref([])

const checkedLabelList = ref(['高分系列', '资源系列', '吉林1号', '无人机'])
const timeRange = ref([])
const satListData = ref([])
const totalCount = ref(0)

const filterForm = reactive({
  province: homeStore.selectedProvince,
  city: homeStore.selectedCity,
  dateRange: ['2026-04-01', '2026-04-14'],
  keepResult: homeStore.keepResults,
})

const menuProps = {
  children: 'children',
  label: 'label',
}

const treeData = [
  {
    id: 'gaofen',
    label: '高分系列',
    children: [
      { id: 'gf-01', label: 'GF-01' },
      { id: 'gf-07', label: 'GF-07' },
      { id: 'gf-02', label: 'GF-02' },
    ],
  },
  {
    id: 'ziyuan',
    label: '资源系列',
    children: [{ id: 'cb-04', label: 'CB-04' }],
  },
  {
    id: 'jilin',
    label: '吉林1号',
    children: [{ id: 'jl-01', label: 'JL-01' }],
  },
  {
    id: 'uav',
    label: '无人机',
    children: [{ id: 'm-03', label: 'M-03' }],
  },
]

const quickRanges = [
  { key: 'threeDays', label: '近三天', days: 3, color: '#3a8cff' },
  { key: 'week', label: '近一周', days: 7, color: '#69c23a' },
  { key: 'month', label: '近一月', days: 30, color: '#a9b0bc' },
  { key: 'halfYear', label: '近半年', days: 180, color: '#f46f6f' },
  { key: 'year', label: '近一年', days: 365, color: '#f0a73b' },
]

const provinceNameMap = computed(() =>
  provinceOptions.reduce((accumulator, item) => {
    accumulator[item.value] = item.label
    return accumulator
  }, {}),
)

const cityOptions = computed(() => cityMap[filterForm.province] || [])

const uploadCityOptions = computed(() => cityMap[uploadForm.province] || [])

const cityNameMap = computed(() =>
  cityOptions.value.reduce((accumulator, item) => {
    accumulator[item.value] = item.label
    return accumulator
  }, {}),
)

const selectedProvinceLabel = computed(() => provinceNameMap.value[filterForm.province] || '')
const selectedCityLabel = computed(() => cityNameMap.value[filterForm.city] || '')
const selectedTypes = computed(() => checkedLabelList.value.join(' / '))

function pointInPolygon(point, polygon) {
  let isInside = false
  const [x, y] = point
  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i, i += 1) {
    const [xi, yi] = polygon[i]
    const [xj, yj] = polygon[j]
    const intersect = yi > y !== yj > y && x < ((xj - xi) * (y - yi)) / (yj - yi) + xi
    if (intersect) {
      isInside = !isInside
    }
  }
  return isInside
}

const filteredResults = computed(() =>
  mockSatelliteList.filter((item) => {
    if (searchMode.value === 'region') {
      if (selectedProvinceLabel.value && item.province !== selectedProvinceLabel.value) {
        return false
      }
      if (selectedCityLabel.value && item.city !== selectedCityLabel.value) {
        return false
      }
    }
    if (searchMode.value === 'draw') {
      if (!drawnPolygon.value.length) {
        return false
      }
      if (!pointInPolygon([item.center.lng, item.center.lat], drawnPolygon.value)) {
        return false
      }
    }
    if (!checkedLabelList.value.some((label) => item.type.includes(label) || item.name.includes(label))) {
      return false
    }
    const [start, end] = filterForm.dateRange || []
    if (start && item.date < start) {
      return false
    }
    if (end && item.date > end) {
      return false
    }
    return true
  }),
)

const pagedResults = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  return filteredResults.value.slice(startIndex, startIndex + pageSize.value)
})

const totalResultCount = computed(() => filteredResults.value.length)

const detailVisible = ref(false)
const detailItem = ref(null)

const thematicVisible = ref(false)
const thematicSource = ref(null)
const thematicForm = reactive({
  title: '',
  type: thematicTypes[0],
  note: '根据当前选中的影像进行专题图制作。',
})

const uploadVisible = ref(false)
const uploadFileList = ref([])
const uploadLoading = ref(false)
const uploadProgress = ref(0)
const uploadForm = reactive({
  acquisitionDate: '2026-04-14',
  province: '',
  city: '',
})

function formatDate(date, includeTime = false) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  
  if (!includeTime) {
    return `${year}-${month}-${day}`
  }
  
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

/**
 * 格式化时间范围为后端所需格式
 * @param {Array} dateRange - [startDate, endDate]
 * @returns {Object} { start, end }
 */
function formatDateRange(dateRange) {
  if (!dateRange || dateRange.length !== 2) {
    return null
  }
  return {
    start: formatDate(dateRange[0], true) + ' 00:00:00',
    end: formatDate(dateRange[1], true) + ' 23:59:59'
  }
}

function getRangeByDays(days) {
  const end = new Date('2026-04-14T00:00:00')
  const start = new Date(end)
  start.setDate(end.getDate() - days + 1)
  return [formatDate(start), formatDate(end)]
}

function syncTreeSelection(data, checked) {
  checkedKeys.value = treeRef.value?.getCheckedKeys() || []
  const checkedNodes = treeRef.value?.getCheckedNodes(false, true) || []
  const labels = checkedNodes.map((node) => node.label)
  checkedLabelList.value = labels
  currentPage.value = 1
}

function setSearchMode(mode) {
  searchMode.value = mode
  if (mode === 'region') {
    drawingMode.value = false
    mapPanelRef.value?.cancelDrawMode()
    ElMessage.info('已切换到省市检索模式')
    return
  }
  filterForm.province = ''
  filterForm.city = ''
  drawingMode.value = true
  mapPanelRef.value?.startDrawMode()
  ElMessage.info('已切换到绘制区域检索模式')
}

function applyQuickRange(item) {
  activeRangeKey.value = item.key
  filterForm.dateRange = getRangeByDays(item.days)
  currentPage.value = 1
}

async function handleSearch() {
  try {
    // 参数验证
    if (checkedLabelList.value.length === 0) {
      ElMessageBox.alert('请选择数据类型！', '提示', {
        confirmButtonText: '确定'
      })
      return
    }

    if (!timeRange.value || timeRange.value.length !== 2) {
      ElMessageBox.alert('请选择时间范围！', '提示', {
        confirmButtonText: '确定'
      })
      return
    }

    // 根据搜索模式构造查询参数
    const queryParams = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      satelliteTypes: checkedLabelList.value
    }

    // 格式化时间范围
    const timeRange = formatDateRange(timeRange.value)
    queryParams.startCollectTime = timeRange.start
    queryParams.endCollectTime = timeRange.end

    // 根据搜索模式选择区域参数
    if (searchMode.value === 'region') {
      // 省市检索
      if (!filterForm.province) {
        ElMessageBox.alert('请选择查询区域（至少选择省份）！', '提示', {
          confirmButtonText: '确定'
        })
        return
      }
      queryParams.province = filterForm.province
      queryParams.city = filterForm.city || ''
    } else if (searchMode.value === 'draw') {
      // 自定义区域检索
      if (!drawnPolygon.value || drawnPolygon.value.length === 0) {
        ElMessageBox.alert('请先绘制检索区域！', '提示', {
          confirmButtonText: '确定'
        })
        return
      }
      // 转换多边形点格式为 "lat,lng"
      queryParams.points = drawnPolygon.value.map(point => {
        return `${point.lat},${point.lng}`
      })
    }

    // 调用后端API
    ElMessage.loading('正在查询数据...')
    const response = await listSatellite1(queryParams)
    
    if (response.code === 200) {
      // 更新搜索结果
      satListData.value = response.rows || []
      totalCount.value = response.total || 0
      
      if (satListData.value.length === 0) {
        ElMessage.warning('未查询到相关数据，请调整筛选条件')
      } else {
        ElMessage.success(`成功查询到 ${response.total} 条数据`)
      }
      
      // 保存搜索条件到store
      homeStore.setProvince(filterForm.province)
      homeStore.setCity(filterForm.city)
      homeStore.setKeepResults(filterForm.keepResult)
    } else {
      ElMessage.error(response.msg || '查询失败')
    }
  } catch (error) {
    console.error('检索错误:', error)
    ElMessage.error('检索数据时出错，请检查网络连接')
  }
}

function startDrawing() {
  if (searchMode.value !== 'draw') {
    ElMessage.warning('请先选择“绘制区域检索”模式')
    return
  }
  drawingMode.value = true
  mapPanelRef.value?.startDrawMode()
  ElMessage.info('已进入绘制模式，点击地图可进行区域绘制（示意）')
}

function cancelDrawing() {
  drawingMode.value = false
  drawnPolygon.value = []
  mapPanelRef.value?.cancelDrawMode()
  mapPanelRef.value?.clearDrawings()
  ElMessage.info('已取消绘制')
}

function handleDrawCreated(payload) {
  drawnPolygon.value = payload.points
  searchMode.value = 'draw'
  drawingMode.value = false
  ElMessage.success(`已生成绘制区域，顶点数 ${payload.points.length}`)
}

function handleDrawCleared() {
  drawnPolygon.value = []
}

function handleDrawInvalid(payload) {
  drawnPolygon.value = []
  drawingMode.value = true
  mapPanelRef.value?.startDrawMode()
  ElMessage.warning(`绘制区域至少需要3个点，当前为${payload.count}个点，请继续绘制并点击首点闭合`)
}

function openDetail(item) {
  detailItem.value = item
  detailVisible.value = true
}

function openThematic(item) {
  thematicSource.value = item
  thematicForm.title = `${item.name}专题图`
  thematicForm.type = thematicTypes[0]
  thematicForm.note = `基于 ${item.name} 自动生成专题图。`
  thematicVisible.value = true
}

function openUploadDialog() {
  uploadVisible.value = true
}

function handleViewImage(item) {
  ElMessage.success(`已切换 ${item.name} 的影像显示状态（示意）`)
}

function navigateToCenter(item) {
  ElMessage.info(`已定位到 ${item.city} ${item.name} 中心点（示意）`)
}

function collectImage(item) {
  ElMessage.success(`已收藏 ${item.name}`)
}

function processingAnalysis(item) {
  ElMessage.success(`已提交 ${item.name} 处理分析任务`)
}

function handleAddDate(item) {
  ElMessage.success(`已加入我的数据：${item.name}`)
}

function downloadCurrent(item) {
  ElMessage.success(`已开始下载 ${item.name} 影像（示意）`)
}

function submitThematic() {
  ElMessage.success(`已提交专题图：${thematicForm.title}`)
  thematicVisible.value = false
}

async function submitUpload() {
  try {
    // 表单验证
    if (!uploadFileList.value.length) {
      ElMessage.warning('请先选择上传文件')
      return
    }

    if (!uploadForm.acquisitionDate) {
      ElMessage.warning('请选择采集时间')
      return
    }

    if (!uploadForm.province) {
      ElMessage.warning('请选择省份')
      return
    }

    // 检查是否已登录
    if (!getToken()) {
      ElMessage.error('请先登录')
      return
    }

    uploadLoading.value = true
    uploadProgress.value = 0

    // 遍历每个文件进行上传
    for (let i = 0; i < uploadFileList.value.length; i++) {
      const file = uploadFileList.value[i]
      const formData = new FormData()
      formData.append('file', file.raw)

      try {
        // 调用文件上传接口
        const uploadResponse = await uploadFile(formData)
        
        if (uploadResponse.code === 200) {
          uploadProgress.value = ((i + 1) / uploadFileList.value.length) * 100
          ElMessage.success(`文件 ${file.name} 上传成功`)
        } else {
          ElMessage.error(`文件 ${file.name} 上传失败: ${uploadResponse.msg}`)
        }
      } catch (error) {
        console.error(`上传文件 ${file.name} 出错:`, error)
        ElMessage.error(`文件 ${file.name} 上传出错`)
      }
    }

    // 所有文件上传完成后
    ElMessage.success('所有文件已提交，后端正在处理解压和数据入库')
    uploadVisible.value = false
    clearUploadDialog()
  } catch (error) {
    console.error('上传错误:', error)
    ElMessage.error('上传过程中出错，请重试')
  } finally {
    uploadLoading.value = false
    uploadProgress.value = 0
  }
}

function handleUploadChange(_, files) {
  uploadFileList.value = files
}

function clearUploadDialog() {
  uploadFileList.value = []
}

function onProvinceChange() {
  if (searchMode.value !== 'region') {
    return
  }
  filterForm.city = ''
  currentPage.value = 1
}

watch(
  () => filteredResults.value.length,
  () => {
    currentPage.value = 1
  },
)

watch(
  () => filterForm.province,
  () => {
    onProvinceChange()
  },
)

function getDetailValue(field) {
  if (!detailItem.value) return '-'
  if (field.key === 'provinceCity') return `${detailItem.value.province} ${detailItem.value.city}`
  return detailItem.value[field.key] || '-'
}

function getDateDisplay() {
  const [start, end] = filterForm.dateRange || []
  return `${start || '-'} 至 ${end || '-'}`
}
</script>

<template>
  <div class="home-page">
    <header class="top-nav">
      <div class="nav-left">
        <div class="logo-wrap">
          <div class="logo-badge">ITRS Lab</div>
        </div>
        <nav class="menu-list">
          <button :class="['menu-item', { active: activeMenu === 'map' }]" @click="activeMenu = 'map'">笔架地图</button>
          <button class="menu-item">AI工具</button>
          <button class="menu-item">关于我们</button>
          <button class="menu-item">产品展示</button>
        </nav>
      </div>
      <div class="nav-right">
        <router-link to="/login">登录</router-link>
        <router-link to="/register">注册</router-link>
      </div>
    </header>

    <main class="content-wrap">
      <aside class="left-panel">
        <div class="panel-header">遥感数据</div>

        <section class="block">
          <h3 class="title">数据类型</h3>
          <el-tree
            ref="treeRef"
            v-model:checked-keys="checkedKeys"
            :data="treeData"
            show-checkbox
            node-key="id"
            :props="menuProps"
            @check-change="syncTreeSelection"
            default-expand-all
          />
        </section>

        <section class="block">
          <h3 class="title">检索区域</h3>
          <div class="area-box">
            <el-radio-group v-model="searchMode" class="mode-group">
              <el-radio label="省市检索" value="region" @change="setSearchMode('region')" />
            </el-radio-group>
            <div class="select-group">
              <el-select v-model="filterForm.province" placeholder="请选择省份" size="small" :disabled="searchMode === 'draw'">
                <el-option v-for="item in provinceOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <el-select v-model="filterForm.city" placeholder="请选择城市" size="small" :disabled="searchMode === 'draw'">
                <el-option v-for="item in cityOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </div>
          </div>

          <div class="draw-box">
            <el-radio-group v-model="searchMode" class="mode-group">
              <el-radio label="绘制区域检索" value="draw" @change="setSearchMode('draw')" />
            </el-radio-group>
            <button class="mini-btn" :disabled="searchMode !== 'draw'" @click="startDrawing">开始绘制</button>
            <button class="mini-btn" :disabled="searchMode !== 'draw'" @click="cancelDrawing">取消绘制</button>
          </div>
        </section>

        <section class="block">
          <h3 class="title">采集时间</h3>
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            size="small"
            range-separator="-"
            start-placeholder="2026-04-01"
            end-placeholder="2026-04-14"
          />

          <div class="tags">
            <button
              v-for="tag in quickRanges"
              :key="tag.key"
              class="time-tag"
              :class="{ active: activeRangeKey === tag.key }"
              :style="{ backgroundColor: tag.color }"
              @click="applyQuickRange(tag)"
            >
              {{ tag.label }}
            </button>
          </div>
        </section>

        <section class="block">
          <h3 class="title">其他选项</h3>
          <el-checkbox v-model="filterForm.keepResult">保持查询结果</el-checkbox>
          <button class="upload-btn" @click="openUploadDialog">上传我的数据</button>
        </section>

        <section class="block result-block">
          <div class="result-head">
            <h3 class="title center">检索结果</h3>
            <span class="result-count">{{ totalResultCount }} 条</span>
          </div>

          <div class="summary-line">类型：{{ selectedTypes }}</div>
          <div class="summary-line">区域：{{ selectedProvinceLabel || '全部省份' }}{{ selectedCityLabel ? ` / ${selectedCityLabel}` : '' }}</div>
          <div class="summary-line">时间：{{ getDateDisplay() }}</div>

          <div class="result-list">
            <article v-for="item in pagedResults" :key="item.id" class="result-card">
              <img class="result-thumb" :src="item.thumb" :alt="item.name" />
              <div class="result-body">
                <div class="result-title-row">
                  <h4 class="result-title">{{ item.name }}</h4>
                  <span class="result-type">{{ item.type }}</span>
                </div>
                <p class="result-desc">{{ item.description }}</p>
                <div class="result-meta">
                  <span>{{ item.province }}</span>
                  <span>{{ item.city }}</span>
                  <span>{{ item.date }}</span>
                </div>
                <div class="result-actions">
                  <el-button size="small" @click="openDetail(item)">详情</el-button>
                  <el-button size="small" @click="openThematic(item)">专题图</el-button>
                  <el-button size="small" @click="handleViewImage(item)">影像</el-button>
                  <el-button size="small" @click="navigateToCenter(item)">定位</el-button>
                  <el-button size="small" @click="collectImage(item)">收藏</el-button>
                  <el-button size="small" @click="processingAnalysis(item)">分析</el-button>
                  <el-button size="small" @click="handleAddDate(item)">入库</el-button>
                </div>
              </div>
            </article>
          </div>

          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            layout="prev, pager, next"
            :total="totalResultCount"
            small
          />

          <button class="search-btn" @click="handleSearch">检索</button>
        </section>
      </aside>

      <section class="map-area">
        <MapPanel
          ref="mapPanelRef"
          @draw-created="handleDrawCreated"
          @draw-cleared="handleDrawCleared"
          @draw-invalid="handleDrawInvalid"
        />
      </section>
    </main>

    <el-dialog v-model="detailVisible" title="卫星数据详情" width="980px" align-center>
      <div v-if="detailItem" class="detail-layout">
        <div class="detail-image-pane">
          <img class="detail-image" :src="detailItem.thumb" :alt="detailItem.name" />
          <div class="detail-image-label">{{ detailItem.name }}</div>
          <p class="detail-image-desc">{{ detailItem.description }}</p>
        </div>
        <div class="detail-info-pane">
          <div v-for="field in detailFields" :key="field.key" class="detail-row">
            <span class="detail-label">{{ field.label }}</span>
            <span class="detail-value">{{ getDetailValue(field) }}</span>
          </div>
          <div class="detail-extents">
            <div class="detail-label">四角坐标</div>
            <ul>
              <li v-for="point in detailItem.extent" :key="point">{{ point }}</li>
            </ul>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button type="primary" @click="downloadCurrent(detailItem)">下载本幅影像</el-button>
          <el-button type="success" @click="collectImage(detailItem)">收藏本幅影像</el-button>
          <el-button type="warning" @click="processingAnalysis(detailItem)">处理分析本幅影像</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="thematicVisible" title="专题图制作" width="860px" align-center>
      <div class="thematic-layout">
        <div class="thematic-form-pane">
          <el-form label-position="top">
            <el-form-item label="专题图标题">
              <el-input v-model="thematicForm.title" placeholder="请输入专题图标题" />
            </el-form-item>
            <el-form-item label="专题图类型">
              <el-select v-model="thematicForm.type" placeholder="请选择专题图类型">
                <el-option v-for="item in thematicTypes" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注信息">
              <el-input v-model="thematicForm.note" type="textarea" :rows="5" placeholder="请输入备注信息" />
            </el-form-item>
          </el-form>
        </div>

        <div class="thematic-preview-pane">
          <div class="preview-header">预览效果</div>
          <div class="preview-card">
            <div class="preview-tag">{{ thematicForm.type }}</div>
            <h4>{{ thematicForm.title }}</h4>
            <p>{{ thematicForm.note }}</p>
            <ul>
              <li>图层边界高亮</li>
              <li>专题色带自动匹配</li>
              <li>支持导出 PNG / PDF</li>
            </ul>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="thematicVisible = false">取消</el-button>
          <el-button type="primary" @click="submitThematic">制作专题图</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="uploadVisible" title="数据上传" width="860px" align-center @closed="clearUploadDialog">
      <div class="upload-layout">
        <div class="upload-form-pane">
          <el-form label-position="top">
            <el-form-item label="采集时间">
              <el-date-picker v-model="uploadForm.acquisitionDate" type="date" placeholder="请选择采集时间" />
            </el-form-item>
            <el-form-item label="所属区域">
              <div class="upload-region-grid">
                <el-select v-model="uploadForm.province" placeholder="请选择省份">
                  <el-option v-for="item in provinceOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
                <el-select v-model="uploadForm.city" placeholder="请选择城市">
                  <el-option v-for="item in uploadCityOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </div>
            </el-form-item>
            <el-form-item label="上传影像文件">
              <el-upload
                v-model:file-list="uploadFileList"
                drag
                multiple
                :auto-upload="false"
                accept=".zip,.rar,.tar.gz,.tar"
                :on-change="handleUploadChange"
                class="upload-box"
              >
                <div class="upload-placeholder">
                  <div class="upload-title">拖拽文件到此处，或点击选择文件</div>
                  <div class="upload-desc">支持 zip、tar.gz、rar 格式，点击“解压并上传”后统一提交</div>
                </div>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>

        <div class="upload-preview-pane">
          <div class="preview-header">待上传文件</div>
          <div class="file-list">
            <div v-if="uploadFileList.length === 0" class="empty-tip">尚未选择文件</div>
            <div v-for="file in uploadFileList" :key="file.uid" class="file-item">{{ file.name }}</div>
          </div>
          <div class="upload-tip">上传前请确认采集时间与区域信息正确。</div>
        </div>
      </div>

      <template #footer>
        <div>
          <div v-if="uploadLoading" class="upload-progress-wrapper">
            <div class="progress-label">上传进度：{{ Math.round(uploadProgress) }}%</div>
            <el-progress :percentage="uploadProgress" :indeterminate="uploadProgress === 0" />
          </div>
          <div class="dialog-footer">
            <el-button @click="uploadVisible = false" :disabled="uploadLoading">取消</el-button>
            <el-button type="primary" @click="submitUpload" :loading="uploadLoading">{{ uploadLoading ? '上传中...' : '解压并上传' }}</el-button>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.home-page {
  width: 100%;
  height: 100%;
  background: #10131a;
}

.top-nav {
  height: 54px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(90deg, #2b2c31, #32343a);
  color: #fff;
  padding: 0 14px;

  .nav-left {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .logo-wrap {
    width: 62px;
    height: 40px;
    border-radius: 20px;
    background: #e9edf2;
    color: #1f2531;
    display: grid;
    place-items: center;

    .logo-badge {
      font-size: 9px;
      font-weight: 700;
      line-height: 1;
    }
  }

  .menu-list {
    display: flex;
    align-items: center;
    gap: 8px;

    .menu-item {
      border: none;
      color: #f2f2f2;
      background: transparent;
      padding: 9px 14px;
      border-radius: 6px;
      font-size: 16px;
      cursor: pointer;

      &.active {
        background: #2f86ff;
      }
    }
  }

  .nav-right {
    display: flex;
    gap: 28px;

    a {
      color: #fff;
      text-decoration: none;
      font-size: 14px;
      font-weight: 600;
    }
  }
}

.content-wrap {
  display: flex;
  height: calc(100% - 54px);
}

.left-panel {
  width: 420px;
  background: linear-gradient(180deg, #1a1d23, #131720);
  color: #fff;
  padding: 16px 20px;
  overflow: auto;
  border-right: 1px solid rgba(255, 255, 255, 0.1);

  .panel-header {
    display: inline-block;
    background: rgba(255, 255, 255, 0.14);
    padding: 6px 20px;
    border-radius: 2px;
    margin-bottom: 16px;
    font-size: 20px;
    color: #ced6e4;
  }

  .block {
    margin-bottom: 18px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.15);
    padding-bottom: 16px;

    &.result-block {
      border-bottom: none;
      padding-bottom: 0;
    }

    .title {
      margin: 0 0 12px;
      font-size: 18px;
      font-weight: 500;
      color: #f5f7fa;

      &.center {
        text-align: center;
      }
    }
  }

  .area-box,
  .draw-box {
    display: flex;
    align-items: center;
    gap: 10px;
    border: 1px solid rgba(255, 255, 255, 0.15);
    border-radius: 6px;
    padding: 10px;
    margin-bottom: 12px;

    .select-group {
      display: grid;
      gap: 6px;
      width: 100%;
    }
  }

  .mode-group {
    min-width: 92px;

    :deep(.el-radio) {
      margin-right: 0;
      color: #dbe3ef;
      font-size: 13px;
    }
  }

  .mini-btn {
    border: 1px solid rgba(255, 255, 255, 0.2);
    background: rgba(255, 255, 255, 0.08);
    color: #fff;
    padding: 8px 12px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;

    &:disabled {
      cursor: not-allowed;
      opacity: 0.5;
    }
  }

  .tags {
    display: flex;
    gap: 8px;
    margin-top: 12px;
    flex-wrap: wrap;

    .time-tag {
      border: none;
      color: #fff;
      border-radius: 4px;
      padding: 5px 12px;
      cursor: pointer;
      font-size: 13px;

      &.active {
        outline: 2px solid rgba(255, 255, 255, 0.7);
      }
    }
  }

  .upload-btn {
    margin-top: 14px;
    border: none;
    color: #dce4ee;
    background: rgba(67, 105, 164, 0.4);
    padding: 8px 16px;
    border-radius: 6px;
    cursor: pointer;
    font-size: 15px;
  }

  .search-btn {
    width: 100%;
    border: none;
    background: #2f91f3;
    color: #fff;
    padding: 10px 0;
    border-radius: 6px;
    cursor: pointer;
    font-size: 22px;
    font-weight: 600;
    margin-top: 10px;
  }

  .result-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;
  }

  .result-count {
    color: #8fb8ff;
    font-size: 14px;
  }

  .summary-line {
    font-size: 13px;
    color: #d7dde7;
    line-height: 1.7;
  }

  .result-list {
    display: grid;
    gap: 10px;
    margin: 12px 0;
  }

  .result-card {
    display: flex;
    gap: 10px;
    padding: 10px;
    border-radius: 8px;
    background: rgba(255, 255, 255, 0.06);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }

  .result-thumb {
    width: 88px;
    height: 88px;
    object-fit: cover;
    border-radius: 6px;
    flex-shrink: 0;
  }

  .result-body {
    min-width: 0;
    flex: 1;
  }

  .result-title-row {
    display: flex;
    justify-content: space-between;
    gap: 10px;
    align-items: flex-start;
  }

  .result-title {
    margin: 0;
    font-size: 15px;
    color: #fff;
    line-height: 1.2;
  }

  .result-type {
    flex-shrink: 0;
    font-size: 12px;
    color: #8db7ff;
    background: rgba(47, 145, 243, 0.14);
    border: 1px solid rgba(47, 145, 243, 0.3);
    padding: 2px 6px;
    border-radius: 999px;
  }

  .result-desc {
    margin: 6px 0 8px;
    font-size: 12px;
    color: #c9d2df;
    line-height: 1.5;
  }

  .result-meta {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    color: #93a2b6;
    font-size: 12px;
    margin-bottom: 8px;
  }

  .result-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  :deep(.el-tree) {
    color: #d0d8e6;
    background: transparent;
    font-size: 14px;
  }

  :deep(.el-tree-node__content) {
    height: 34px;
  }

  :deep(.el-tree-node:focus > .el-tree-node__content) {
    background: rgba(255, 255, 255, 0.12);
  }

  :deep(.el-checkbox__inner) {
    background: #fff;
    border: 1px solid #c7c9d1;
  }

  :deep(.el-checkbox__inner::after) {
    border-color: #409eff;
  }

  :deep(.el-checkbox.is-checked .el-checkbox__inner) {
    background: #409eff;
    border-color: #409eff;
  }

  :deep(.el-checkbox.is-checked .el-checkbox__inner::after) {
    border-color: #fff;
  }

  :deep(.el-tree-node.is-checked > .el-tree-node__content .el-checkbox .el-checkbox__inner) {
    background: #409eff;
    border-color: #409eff;
  }

  :deep(.el-tree-node.is-checked > .el-tree-node__content .el-checkbox__inner::after) {
    border-color: #fff;
  }

  :deep(.el-select) {
    width: 100%;
  }

  :deep(.el-input__wrapper) {
    background: #f5f7fa;
    border-radius: 4px;
  }

  :deep(.el-date-editor.el-input__wrapper) {
    width: 100%;
  }

  :deep(.el-checkbox) {
    color: #d6dce7;
    font-size: 14px;
  }

  :deep(.el-pagination) {
    justify-content: center;
    margin-bottom: 8px;
  }
}

.map-area {
  flex: 1;
  background: #e8edf3;
}

.detail-layout,
.thematic-layout,
.upload-layout {
  display: grid;
  gap: 18px;
}

.detail-layout {
  grid-template-columns: 1fr 1.1fr;
}

.detail-image-pane {
  background: #f7f9fc;
  border-radius: 12px;
  padding: 14px;
}

.detail-image {
  width: 100%;
  height: 280px;
  object-fit: cover;
  border-radius: 10px;
}

.detail-image-label {
  margin-top: 10px;
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.detail-image-desc {
  margin: 8px 0 0;
  color: #687287;
}

.detail-info-pane {
  display: grid;
  gap: 10px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  background: #f7f9fc;
  border-radius: 8px;
}

.detail-label {
  color: #75829b;
  font-size: 14px;
}

.detail-value {
  color: #263043;
  font-weight: 600;
}

.detail-extents {
  padding: 12px;
  border-radius: 8px;
  background: #f7f9fc;
  color: #263043;

  ul {
    margin: 8px 0 0;
    padding-left: 18px;
    color: #5a6476;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;

  .upload-progress-wrapper {
    margin-bottom: 16px;

    .progress-label {
      margin-bottom: 8px;
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
  flex-wrap: wrap;
  gap: 10px;
}

.thematic-layout {
  grid-template-columns: 1fr 0.85fr;
}

.upload-layout {
  grid-template-columns: 1fr 0.75fr;
}

.thematic-form-pane,
.upload-form-pane,
.thematic-preview-pane,
.upload-preview-pane {
  background: #f7f9fc;
  border-radius: 12px;
  padding: 16px;
}

.preview-header {
  font-size: 16px;
  font-weight: 700;
  color: #243044;
  margin-bottom: 12px;
}

.preview-card {
  min-height: 260px;
  border-radius: 16px;
  padding: 18px;
  background: linear-gradient(180deg, #2f86ff, #6ac8ff);
  color: #fff;

  .preview-tag {
    display: inline-block;
    padding: 4px 10px;
    border-radius: 999px;
    background: rgba(255, 255, 255, 0.22);
    margin-bottom: 12px;
  }

  h4 {
    margin: 0 0 10px;
    font-size: 24px;
  }

  p {
    margin: 0 0 12px;
    line-height: 1.6;
  }

  ul {
    margin: 0;
    padding-left: 20px;
  }
}

.upload-region-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.upload-box {
  width: 100%;
}

.upload-placeholder {
  padding: 24px 12px;
  text-align: center;
}

.upload-title {
  font-size: 16px;
  color: #344257;
  font-weight: 700;
}

.upload-desc {
  margin-top: 8px;
  color: #6b7482;
  font-size: 13px;
}

.file-list {
  min-height: 260px;
  display: grid;
  gap: 8px;
}

.file-item {
  background: #fff;
  padding: 10px 12px;
  border-radius: 8px;
  color: #344257;
  box-shadow: 0 4px 12px rgba(36, 48, 68, 0.06);
}

.empty-tip,
.upload-tip {
  color: #7b8798;
  font-size: 13px;
}

.upload-tip {
  margin-top: 12px;
}

@media (max-width: 1200px) {
  .left-panel {
    width: 340px;

    .panel-header {
      font-size: 18px;
    }

    .block .title {
      font-size: 16px;
    }

    .search-btn {
      font-size: 18px;
    }
  }

  .thematic-layout,
  .upload-layout,
  .detail-layout {
    grid-template-columns: 1fr;
  }
}
</style>
