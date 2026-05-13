<template>
  <div class="ai-workbench">
    <aside class="tool-panel">
      <div class="tool-panel__header">
        <div class="eyebrow">AI TOOLBOX</div>
        <h2>AI工具箱</h2>
        <p>选择解译模型后，对待处理影像发起要素提取任务。</p>
      </div>

      <div class="tool-list">
        <section v-for="group in toolGroups" :key="group.title" class="tool-section">
          <div class="tool-section__title">{{ group.title }}</div>
          <div class="tool-grid">
            <button
              v-for="tool in group.tools"
              :key="tool.id"
              type="button"
              class="tool-card"
              :class="{ 'is-active': isActiveTool(tool.id), 'is-beta': tool.beta }"
              @click="toggleSelect(tool.id, tool.title)"
            >
              <span class="tool-card__image-wrap">
                <img :src="tool.image" :alt="tool.title" class="tool-card__image" />
              </span>
              <span class="tool-card__body">
                <span class="tool-card__name">{{ tool.title }}</span>
                <span class="tool-card__desc">{{ tool.desc }}</span>
                <span class="tool-card__tag">{{ tool.beta ? '暂未开放' : '可用' }}</span>
              </span>
            </button>
          </div>
        </section>
      </div>
    </aside>

    <main class="process-panel">
      <section class="hero-card">
        <div>
          <div class="eyebrow">REMOTE SENSING AI</div>
          <h1>AI遥感智能解译</h1>
          <p>选择左侧工具，对遥感影像进行海岸线、绿潮、湿地、赤潮等海洋海岸要素提取。</p>
        </div>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="goHome">上传/选择影像</el-button>
          <el-button size="large" plain @click="showExampleGuide">查看示例流程</el-button>
        </div>
      </section>

      <section class="flow-card">
        <div class="flow-step is-done">
          <span class="flow-step__index">1</span>
          <div>
            <strong>选择工具</strong>
            <p>{{ satelliteTypename || '请在左侧选择一个AI工具' }}</p>
          </div>
        </div>
        <div class="flow-line"></div>
        <div class="flow-step" :class="{ 'is-done': infoList.length > 0 }">
          <span class="flow-step__index">2</span>
          <div>
            <strong>准备影像</strong>
            <p>待处理 {{ infoList.length }} 景影像</p>
          </div>
        </div>
        <div class="flow-line"></div>
        <div class="flow-step">
          <span class="flow-step__index">3</span>
          <div>
            <strong>开始处理</strong>
            <p>生成AI解译任务并查看结果</p>
          </div>
        </div>
      </section>

      <section class="dataset-card">
        <div class="dataset-card__header">
          <div>
            <h2>待处理数据集</h2>
            <p>确认影像、卫星、传感器与要素提取类型后开始处理。</p>
          </div>
          <div class="dataset-summary">
            <span>{{ infoList.length }}</span>
            <em>待处理</em>
          </div>
        </div>

        <div class="custom-table">
          <el-table
            cellspacing="0"
            cellpadding="0"
            :data="infoList"
            :header-cell-style="tableHeaderStyle"
            style="width: 100%;"
            @selection-change="handleSelectionChange"
          >
            <el-table-column label="影像名称" align="center" prop="imageName" min-width="260" class-name="columnHead">
              <template #default="scope">
                <div class="image-name-cell">
                  {{ scope.row.satelliteType + '_' + scope.row.sensorType + '_' + parseTime(scope.row.collectTime, '{y}-{m}-{d}') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="影像速览" align="center" prop="image" width="120" class-name="columnHead">
              <template #default="scope">
                <image-preview :src="scope.row.image" :width="54" :height="54" />
              </template>
            </el-table-column>
            <el-table-column prop="satelliteType" label="卫星" align="center" min-width="120" class-name="columnHead" />
            <el-table-column prop="satelliteType" label="要素提取类型" align="center" min-width="150" class-name="columnHead">
              <template #default>
                <el-tag v-if="satelliteTypename" effect="dark" type="success">{{ satelliteTypename }}</el-tag>
                <el-tag v-else effect="plain" type="info">待选择</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="sensorType" label="传感器" align="center" min-width="120" class-name="columnHead" />
            <el-table-column prop="collectTime" label="采集时间" align="center" min-width="180" class-name="columnHead" />
            <el-table-column label="状态" align="center" width="110" class-name="columnHead">
              <template #default>
                <el-tag effect="plain" type="warning">待处理</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="210" class-name="small-padding fixed-width columnHead">
              <template #default="scope">
                <el-button type="primary" size="default" @click="editItem(scope.row)">要素提取</el-button>
                <el-button size="default" @click="deleteItem(scope.row)">移除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <div class="empty-state">
                <div class="empty-state__icon">🌊</div>
                <h3>暂无待处理数据</h3>
                <p>请先从地图检索页勾选公开数据并点击“处理分析”，或上传本地遥感影像。</p>
                <div class="empty-state__actions">
                  <el-button type="primary" @click="goHome">去选择/上传影像</el-button>
                  <el-button plain @click="showExampleGuide">查看示例流程</el-button>
                </div>
              </div>
            </template>
          </el-table>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup name="aiProcess">
import { ref } from "vue";
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { generateAipOrder } from "@/api/ai/info";
import coastImg from "@/assets/images/anxian.png";
import aquacultureImg from "@/assets/images/yangzhiqu.jpg";
import greenTideImg from "@/assets/images/chao.jpg";
import wetlandImg from "@/assets/images/shidi.png";
import redTideImg from "@/assets/images/redTide.png";
import sargassumImg from "@/assets/images/scagassum.png";
import classifyImg from "@/assets/images/fenlei.svg";
import changeImg from "@/assets/images/bianhua.svg";

const router = useRouter();
const infoList = ref([]);
const currentRow = ref(null);
const isSelected = ref(false);
const isSelected1 = ref(false);
const isSelected2 = ref(false);
const isSelected3 = ref(false);
const isSelected4 = ref(false);
const isSelected5 = ref(false);
const isSelected6 = ref(false);
const isSelected7 = ref(false);
const isSelected8 = ref(false);
const isSelected9 = ref(false);
const isSelected10 = ref(false);
const satelliteTypename = ref('');
const isTrue = ref(false);

const tableHeaderStyle = {
  'text-align': 'center',
  'font-size': '14px',
  'font-weight': '600',
  'background': '#eef2f7',
  'color': '#1f2937',
  'border': 'none'
};

const toolGroups = [
  {
    title: '海洋海岸典型要素信息提取',
    tools: [
      { id: 1, title: '海岸线', desc: '岸线边界自动提取', image: coastImg },
      { id: 2, title: '海水养殖', desc: '养殖区斑块识别', image: aquacultureImg },
      { id: 3, title: '浒苔绿潮(16m)', desc: '16m绿潮范围提取', image: greenTideImg },
      { id: 9, title: '浒苔绿潮(2m)', desc: '2m精细绿潮识别', image: greenTideImg },
      { id: 4, title: '滨海湿地', desc: '湿地类型信息提取', image: wetlandImg, beta: true },
      { id: 5, title: '赤潮提取', desc: '赤潮疑似区域识别', image: redTideImg },
      { id: 10, title: '马尾藻(2m)', desc: '2m马尾藻提取', image: sargassumImg },
      { id: 6, title: '马尾藻(16m)', desc: '16m马尾藻提取', image: sargassumImg }
    ]
  },
  {
    title: '通用遥感影像智能解译',
    tools: [
      { id: 7, title: '土地覆盖分类', desc: '多地物智能分类', image: classifyImg, beta: true },
      { id: 8, title: '变化检测', desc: '多时相变化分析', image: changeImg, beta: true }
    ]
  }
];

onMounted(() => {
  if (history.state.rowData) {
    try {
      const parsedRowData = JSON.parse(history.state.rowData);
      if (Array.isArray(parsedRowData)) {
        parsedRowData.forEach(item => {
          infoList.value.push(item);
        });
      } else {
        infoList.value.push(parsedRowData);
      }
    } catch (error) {
      console.error("Error parsing route query data:", error);
    }
  }
});

const editItem = (item) => {
  var selType = null;
  if (isSelected1.value) {
    selType = "1";
  } else if (isSelected2.value) {
    selType = "2";
  } else if (isSelected3.value) {
    selType = "3";
  } else if (isSelected5.value) {
    selType = "5";
  } else if (isSelected6.value) {
    selType = "6";
  } else if (isSelected9.value) {
    selType = "9";
  } else if (isSelected10.value) {
    selType = "10";
  } else if (isSelected4.value || isSelected5.value || isSelected7.value || isSelected8.value) {
    ElMessage({
      message: '暂未开放当前类别工具的处理，敬请期待',
      type: 'error',
      duration: 3000,
      showClose: true
    });
    return;
  }
  if (selType == null) {
    ElMessage({
      message: '请选择一种工具用来处理',
      type: 'error',
      duration: 3000,
      showClose: true
    });
    return;
  }
  generateAipOrder({ id: item.id, type: selType }).then((response) => {
    if (response.code == 200) {
      ElMessage({
        message: response.msg,
        type: 'success',
        duration: 3000,
        showClose: true
      });
      const index = infoList.value.indexOf(item);
      if (index !== -1) {
        infoList.value.splice(index, 1);
      }
    } else {
      ElMessage({
        message: response.msg,
        type: 'error',
        duration: 3000,
        showClose: true
      });
    }
  });
};

const handleClick = (itemName) => {
  alert("你点击了: " + itemName);
};

const deleteItem = (item) => {
  const index = infoList.value.indexOf(item);
  if (index !== -1) {
    infoList.value.splice(index, 1);
  }
};

function highlight(element) {
}

function unhighlight(element) {
}

function isActiveTool(id) {
  const activeMap = {
    1: isSelected1.value,
    2: isSelected2.value,
    3: isSelected3.value,
    4: isSelected4.value,
    5: isSelected5.value,
    6: isSelected6.value,
    7: isSelected7.value,
    8: isSelected8.value,
    9: isSelected9.value,
    10: isSelected10.value
  };
  return !!activeMap[id];
}

function toggleSelect(id, title = "") {
  isSelected1.value = false;
  isSelected2.value = false;
  isSelected3.value = false;
  isSelected4.value = false;
  isSelected5.value = false;
  isSelected6.value = false;
  isSelected7.value = false;
  isSelected8.value = false;
  isSelected9.value = false;
  isSelected10.value = false;
  satelliteTypename.value = title;
  switch (id) {
    case 1:
      isSelected1.value = true;
      break;
    case 2:
      isSelected2.value = true;
      break;
    case 3:
      isSelected3.value = true;
      break;
    case 4:
      isSelected4.value = true;
      break;
    case 5:
      isSelected5.value = true;
      break;
    case 6:
      isSelected6.value = true;
      break;
    case 7:
      isSelected7.value = true;
      break;
    case 8:
      isSelected8.value = true;
      break;
    case 9:
      isSelected9.value = true;
      break;
    case 10:
      isSelected10.value = true;
      break;
    default:
      console.error("未知操作类型");
  }
}

function handleSelectionChange(row) {
  currentRow.value = row;
}

function goHome() {
  router.push('/home');
}

function showExampleGuide() {
  ElMessage({
    message: '示例流程：在地图页检索/上传影像 → 勾选数据 → 点击处理分析 → 在本页选择AI工具并发起任务。',
    type: 'info',
    duration: 5000,
    showClose: true
  });
}
</script>

<style scoped lang="scss">
.ai-workbench {
  --panel-bg: #111827;
  --panel-bg-soft: #172033;
  --card-bg: rgba(31, 41, 55, 0.86);
  --border-color: rgba(148, 163, 184, 0.18);
  --primary: #4f7df3;
  --primary-soft: rgba(79, 125, 243, 0.18);
  --text: #e5edf8;
  --muted: #94a3b8;
  display: flex;
  min-height: calc(100vh - 50px);
  background:
    radial-gradient(circle at top right, rgba(37, 99, 235, 0.18), transparent 32%),
    linear-gradient(135deg, #0f172a 0%, #111827 46%, #0b1120 100%);
  color: var(--text);
  overflow: hidden;
}

.eyebrow {
  color: #7dd3fc;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.14em;
  text-transform: uppercase;
}

.tool-panel {
  width: 300px;
  min-width: 300px;
  padding: 18px 16px;
  background: rgba(15, 23, 42, 0.92);
  border-right: 1px solid var(--border-color);
  box-shadow: 12px 0 30px rgba(2, 6, 23, 0.24);
  overflow-y: auto;
}

.tool-panel__header {
  text-align: left;
  padding: 4px 4px 14px;

  h2 {
    margin: 8px 0 6px;
    font-size: 22px;
    line-height: 1.2;
  }

  p {
    margin: 0;
    color: var(--muted);
    font-size: 13px;
    line-height: 1.7;
  }
}

.tool-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.tool-section {
  background: rgba(30, 41, 59, 0.64);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 14px;
}

.tool-section__title {
  text-align: left;
  color: #dbeafe;
  font-size: 14px;
  font-weight: 700;
  padding-bottom: 12px;
}

.tool-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.tool-card {
  min-height: 142px;
  padding: 10px;
  border: 1px solid rgba(148, 163, 184, 0.16);
  border-radius: 14px;
  background: rgba(15, 23, 42, 0.72);
  color: var(--text);
  cursor: pointer;
  transition: transform 0.18s ease, border-color 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
  text-align: left;

  &:hover {
    transform: translateY(-2px);
    border-color: rgba(125, 211, 252, 0.45);
    box-shadow: 0 14px 28px rgba(2, 6, 23, 0.28);
  }

  &.is-active {
    background: linear-gradient(180deg, rgba(37, 99, 235, 0.34), rgba(15, 23, 42, 0.84));
    border-color: rgba(96, 165, 250, 0.9);
    box-shadow: 0 0 0 2px rgba(79, 125, 243, 0.18), 0 18px 32px rgba(37, 99, 235, 0.22);
  }
}

.tool-card__image-wrap {
  display: flex;
  width: 54px;
  height: 54px;
  margin: 0 auto 10px;
  border-radius: 14px;
  overflow: hidden;
  background: #0f172a;
  align-items: center;
  justify-content: center;
}

.tool-card__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tool-card__body,
.tool-card__name,
.tool-card__desc,
.tool-card__tag {
  display: block;
}

.tool-card__name {
  text-align: center;
  font-size: 13px;
  font-weight: 700;
  line-height: 1.35;
}

.tool-card__desc {
  margin-top: 4px;
  color: var(--muted);
  font-size: 12px;
  line-height: 1.35;
  text-align: center;
}

.tool-card__tag {
  width: fit-content;
  margin: 8px auto 0;
  padding: 2px 8px;
  border-radius: 999px;
  background: rgba(34, 197, 94, 0.12);
  color: #86efac;
  font-size: 11px;
}

.tool-card.is-beta .tool-card__tag {
  background: rgba(245, 158, 11, 0.12);
  color: #facc15;
}

.process-panel {
  flex: 1;
  min-width: 0;
  padding: 22px 24px;
  overflow-y: auto;
}

.hero-card,
.flow-card,
.dataset-card {
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 18px;
  box-shadow: 0 18px 42px rgba(2, 6, 23, 0.2);
}

.hero-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 24px;
  text-align: left;

  h1 {
    margin: 8px 0;
    font-size: 28px;
    line-height: 1.2;
  }

  p {
    max-width: 720px;
    margin: 0;
    color: var(--muted);
    line-height: 1.7;
  }
}

.hero-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.flow-card {
  display: flex;
  align-items: center;
  margin-top: 16px;
  padding: 16px 20px;
}

.flow-step {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 180px;
  text-align: left;

  strong {
    display: block;
    font-size: 15px;
  }

  p {
    margin: 3px 0 0;
    color: var(--muted);
    font-size: 12px;
  }
}

.flow-step__index {
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: rgba(148, 163, 184, 0.16);
  color: #cbd5e1;
  font-weight: 700;
}

.flow-step.is-done .flow-step__index {
  background: var(--primary);
  color: #fff;
}

.flow-line {
  flex: 1;
  height: 1px;
  margin: 0 16px;
  background: linear-gradient(90deg, rgba(96, 165, 250, 0.55), rgba(148, 163, 184, 0.14));
}

.dataset-card {
  margin-top: 16px;
  padding: 20px;
}

.dataset-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  text-align: left;

  h2 {
    margin: 0 0 6px;
    font-size: 22px;
  }

  p {
    margin: 0;
    color: var(--muted);
    font-size: 13px;
  }
}

.dataset-summary {
  min-width: 88px;
  padding: 10px 14px;
  border-radius: 14px;
  background: var(--primary-soft);
  border: 1px solid rgba(96, 165, 250, 0.24);
  text-align: center;

  span {
    display: block;
    font-size: 24px;
    font-weight: 800;
    color: #bfdbfe;
  }

  em {
    color: var(--muted);
    font-size: 12px;
    font-style: normal;
  }
}

.custom-table {
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

.image-name-cell {
  color: #e2e8f0;
  font-weight: 600;
}

.empty-state {
  padding: 54px 20px;
  text-align: center;
  color: var(--muted);

  h3 {
    margin: 10px 0 6px;
    color: #e5edf8;
    font-size: 20px;
  }

  p {
    margin: 0 auto 18px;
    max-width: 520px;
    line-height: 1.7;
  }
}

.empty-state__icon {
  font-size: 38px;
}

.empty-state__actions {
  display: flex;
  gap: 10px;
  justify-content: center;
}

:deep(.el-table) {
  --el-table-bg-color: #121a2a;
  --el-table-tr-bg-color: #121a2a;
  --el-table-row-hover-bg-color: #1e293b;
  --el-table-border-color: rgba(148, 163, 184, 0.16);
  --el-table-text-color: #dbeafe;
  --el-table-header-text-color: #dbeafe;
  background: #121a2a;
  color: #dbeafe;
}

:deep(.el-table__inner-wrapper::before),
:deep(.el-table__border-left-patch) {
  background-color: rgba(148, 163, 184, 0.16);
}

:deep(.el-table th.el-table__cell),
:deep(.el-table td.el-table__cell) {
  border-bottom: 1px solid rgba(148, 163, 184, 0.12);
}

:deep(.el-table__empty-block) {
  background: #121a2a;
}

:deep(.el-table__empty-text) {
  width: 100%;
  color: var(--muted);
}

:deep(.columnHead > .cell) {
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
}

:deep(.el-table th.el-table__cell) {
  background: #eef2f7 !important;
}

:deep(.el-table th.el-table__cell .cell) {
  color: #1f2937 !important;
}

@media (max-width: 1100px) {
  .ai-workbench {
    flex-direction: column;
    overflow: auto;
  }

  .tool-panel {
    width: 100%;
    min-width: 0;
    max-height: none;
  }

  .tool-grid {
    grid-template-columns: repeat(4, minmax(120px, 1fr));
  }

  .hero-card,
  .dataset-card__header,
  .flow-card {
    flex-direction: column;
    align-items: stretch;
  }

  .flow-line {
    display: none;
  }
}
</style>
