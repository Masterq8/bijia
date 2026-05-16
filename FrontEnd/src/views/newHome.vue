<template>
  <div class="new-home-container">
    <!-- 主视觉区域 -->
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <div class="hero-left">
          <h1 class="hero-title">海洋海岸带遥感影像智能解译云平台</h1>
          <p class="hero-subtitle">从影像检索、智能解译到成果下载的一站式工作流</p>
          <div class="hero-buttons">
            <button class="primary-btn">
              <span class="btn-icon">📋</span>
              新建解译任务
            </button>
            <button class="secondary-btn">
              <span class="btn-icon">⚡</span>
              快速上手
            </button>
          </div>
        </div>
        <div class="hero-right">
          <!-- 平台运行概览 -->
          <div class="status-card">
            <div class="card-header">
              <span class="card-title">平台运行概览</span>
              <span class="status-badge running">运行正常</span>
            </div>
            <div class="status-grid">
              <div class="status-item">
                <div class="status-icon">📦</div>
                <div class="status-info">
                  <div class="status-value">12+</div>
                  <div class="status-label">可用模型</div>
                </div>
              </div>
              <div class="status-item">
                <div class="status-icon">📝</div>
                <div class="status-info">
                  <div class="status-value">68+</div>
                  <div class="status-label">今日任务</div>
                </div>
              </div>
              <div class="status-item">
                <div class="status-icon">⏱️</div>
                <div class="status-info">
                  <div class="status-value">6 min</div>
                  <div class="status-label">平均处理时长</div>
                </div>
              </div>
              <div class="status-item">
                <div class="status-icon">🔄</div>
                <div class="status-info">
                  <div class="status-value">6</div>
                  <div class="status-label">全部运行状态</div>
                </div>
              </div>
            </div>
            <div class="card-footer">
              <a href="#" class="view-all">查看全部运行状态 →</a>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 核心功能区域 -->
    <section class="features-section">
      <div class="features-grid">
        <div class="feature-card blue">
          <div class="feature-icon">🔍</div>
          <h3 class="feature-title">数据检索</h3>
          <p class="feature-description">实时数据影像检索与预览，多源遥感信息目标数据快速定位目标数据</p>
          <span class="arrow">→</span>
        </div>
        <div class="feature-card green">
          <div class="feature-icon">🤖</div>
          <h3 class="feature-title">AI解译</h3>
          <p class="feature-description">多种智能解译算法模型，高效精准解译目标</p>
          <span class="arrow">→</span>
        </div>
        <div class="feature-card purple">
          <div class="feature-icon">📊</div>
          <h3 class="feature-title">任务队列</h3>
          <p class="feature-description">灵活管理任务进度，实时查看解译任务</p>
          <span class="arrow">→</span>
        </div>
        <div class="feature-card orange">
          <div class="feature-icon">📥</div>
          <h3 class="feature-title">成果下载</h3>
          <p class="feature-description">一键下载解译成果，支持多种格式导出</p>
          <span class="arrow">→</span>
        </div>
      </div>
    </section>

    <!-- 五步完成解译流程 -->
    <section class="process-section">
      <div class="section-header">
        <h2 class="section-title">五步完成解译</h2>
        <span class="section-subtitle">简单高效的标准化工作流</span>
      </div>
      <div class="process-steps">
        <div class="process-step">
          <div class="step-number">1</div>
          <div class="step-icon">🗂️</div>
          <h4 class="step-title">选择数据</h4>
          <p class="step-description">检索并筛选目标影像</p>
        </div>
        <div class="step-arrow">→</div>
        <div class="process-step">
          <div class="step-number">2</div>
          <div class="step-icon">📦</div>
          <h4 class="step-title">加入数据篮</h4>
          <p class="step-description">管理已选数据</p>
        </div>
        <div class="step-arrow">→</div>
        <div class="process-step">
          <div class="step-number">3</div>
          <div class="step-icon">🔧</div>
          <h4 class="step-title">选择任务模板</h4>
          <p class="step-description">选择合适的解译模板</p>
        </div>
        <div class="step-arrow">→</div>
        <div class="process-step">
          <div class="step-number">4</div>
          <div class="step-icon">🚀</div>
          <h4 class="step-title">提交处理</h4>
          <p class="step-description">提交任务并开始处理</p>
        </div>
        <div class="step-arrow">→</div>
        <div class="process-step">
          <div class="step-number">5</div>
          <div class="step-icon">📥</div>
          <h4 class="step-title">预览与下载</h4>
          <p class="step-description">查看结果并下载成果</p>
        </div>
      </div>
    </section>

    <!-- 主体内容区域 -->
    <section class="main-content">
      <div class="content-left">
        <!-- 典型应用场景 -->
        <div class="section-header">
          <h2 class="section-title">典型应用场景</h2>
          <span class="section-subtitle">聚焦平台在海岸带领域的应用</span>
        </div>
        <div class="scenes-grid">
          <div class="scene-card">
            <div class="scene-image">🌊</div>
            <div class="scene-info">
              <h4 class="scene-title">海岸线提取</h4>
              <p class="scene-description">自动提取海岸线位置与变化，支持多时相对比分析</p>
              <a href="#" class="view-detail">查看详情 →</a>
            </div>
          </div>
          <div class="scene-card">
            <div class="scene-image">🐟</div>
            <div class="scene-info">
              <h4 class="scene-title">海水养殖识别</h4>
              <p class="scene-description">识别养殖围网、养殖区、养殖区域统计养殖面积分布</p>
              <a href="#" class="view-detail">查看详情 →</a>
            </div>
          </div>
          <div class="scene-card">
            <div class="scene-image">🌿</div>
            <div class="scene-info">
              <h4 class="scene-title">滨海湿地监测</h4>
              <p class="scene-description">提取湿地范围与类型，监测湿地变化与健康状况</p>
              <a href="#" class="view-detail">查看详情 →</a>
            </div>
          </div>
        </div>
      </div>

      <div class="content-right">
        <!-- 平台动态 -->
        <div class="dynamic-card">
          <div class="card-header">
            <span class="card-title">平台动态</span>
            <a href="#" class="more-link">更多 →</a>
          </div>
          <div class="dynamic-list">
            <div class="dynamic-item">
              <span class="dynamic-dot blue-dot"></span>
              <span class="dynamic-text">新增海岸带分类模型</span>
              <span class="dynamic-time">2024-05-20</span>
            </div>
            <div class="dynamic-item">
              <span class="dynamic-dot green-dot"></span>
              <span class="dynamic-text">系统维护通知（5月25日）</span>
              <span class="dynamic-time">2024-05-18</span>
            </div>
            <div class="dynamic-item">
              <span class="dynamic-dot blue-dot"></span>
              <span class="dynamic-text">新增湿地提取服务</span>
              <span class="dynamic-time">2024-05-15</span>
            </div>
            <div class="dynamic-item">
              <span class="dynamic-dot orange-dot"></span>
              <span class="dynamic-text">海岸带监测哨兵2 L2A数据</span>
              <span class="dynamic-time">2024-05-15</span>
            </div>
            <div class="dynamic-item">
              <span class="dynamic-dot blue-dot"></span>
              <span class="dynamic-text">支持Sentinel-2 L2A数据</span>
              <span class="dynamic-time">2024-05-12</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-left">
          <span class="footer-logo">🌊 海洋海岸带遥感影像智能解译云平台</span>
          <span class="footer-copyright">© 2024 版权所有</span>
        </div>
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">隐私政策</a>
          <a href="#">服务条款</a>
          <a href="#">联系我们</a>
        </div>
        <div class="footer-right">
          <span class="tech-support">技术支持：support@oceancosat.cn</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup name="NewHome">
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateTo = (path) => {
  router.push(path);
};
</script>

<style scoped lang="scss">
.new-home-container {
  min-height: 100vh;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: #f5f7fa;
}

/* 主视觉区域 */
.hero-section {
  position: relative;
  min-height: 500px;
  padding: 20px 40px 60px;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beautiful%20ocean%20coastline%20satellite%20view%20with%20turquoise%20water%20and%20islands%20technology%20style%20dark%20blue&image_size=landscape_16_9");
  background-size: cover;
  background-position: center;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(0, 50, 80, 0.9) 0%, rgba(0, 80, 120, 0.7) 100%);
  }
}

.hero-content {
  position: relative;
  z-index: 1;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  max-width: 1600px;
  margin: 0 auto;
  gap: 40px;
}

.hero-left {
  flex: 1;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 40px;
}

.hero-buttons {
  display: flex;
  gap: 20px;
}

.primary-btn, .secondary-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.primary-btn {
  background: linear-gradient(135deg, #00b4d8 0%, #0077b6 100%);
  color: #fff;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(0, 180, 216, 0.4);
  }
}

.secondary-btn {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.3);
  
  &:hover {
    background: rgba(255, 255, 255, 0.25);
    transform: translateY(-3px);
  }
}

.btn-icon {
  font-size: 18px;
}

.hero-right {
  width: 320px;
}

.status-card {
  background: rgba(10, 30, 50, 0.9);
  border-radius: 16px;
  padding: 24px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
}

.status-badge {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 12px;
  font-weight: 500;
  
  &.running {
    background: rgba(0, 255, 136, 0.2);
    color: #00ff88;
  }
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 10px;
}

.status-icon {
  font-size: 24px;
}

.status-info {
  display: flex;
  flex-direction: column;
}

.status-value {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
}

.status-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.card-footer {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.view-all {
  font-size: 13px;
  color: #00b4d8;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

/* 核心功能区域 */
.features-section {
  padding: 60px 40px;
  background: #fff;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  max-width: 1600px;
  margin: 0 auto;
}

.feature-card {
  display: flex;
  flex-direction: column;
  padding: 28px;
  border-radius: 16px;
  transition: all 0.3s ease;
  position: relative;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
  }
  
  &.blue {
    background: linear-gradient(135deg, #e6f7ff 0%, #b3e5fc 100%);
    border: 1px solid #81d4fa;
  }
  
  &.green {
    background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
    border: 1px solid #81c784;
  }
  
  &.purple {
    background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
    border: 1px solid #ce93d8;
  }
  
  &.orange {
    background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
    border: 1px solid #ffb74d;
  }
}

.feature-icon {
  font-size: 40px;
  margin-bottom: 16px;
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 10px;
}

.feature-description {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  flex: 1;
}

.arrow {
  position: absolute;
  right: 28px;
  bottom: 28px;
  font-size: 18px;
  color: #999;
}

/* 五步完成解译流程 */
.process-section {
  padding: 60px 40px;
  background: #f5f7fa;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.section-subtitle {
  font-size: 14px;
  color: #999;
}

.process-steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
  flex-wrap: wrap;
  max-width: 1400px;
  margin: 0 auto;
}

.process-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  min-width: 160px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }
}

.step-number {
  position: relative;
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #0077b6 0%, #00b4d8 100%);
  border-radius: 50%;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.step-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.step-title {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 6px;
}

.step-description {
  font-size: 12px;
  color: #999;
  text-align: center;
}

.step-arrow {
  font-size: 24px;
  color: #ccc;
  font-weight: 300;
}

/* 主体内容区域 */
.main-content {
  display: flex;
  gap: 32px;
  padding: 60px 40px;
  max-width: 1600px;
  margin: 0 auto;
}

.content-left {
  flex: 1;
}

.content-right {
  width: 360px;
}

.scenes-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.scene-card {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }
}

.scene-image {
  width: 120px;
  height: 80px;
  background: linear-gradient(135deg, #e6f7ff 0%, #b3e5fc 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.scene-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.scene-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.scene-description {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  flex: 1;
}

.view-detail {
  font-size: 13px;
  color: #0077b6;
  text-decoration: none;
  margin-top: 10px;
  
  &:hover {
    text-decoration: underline;
  }
}

/* 平台动态卡片 */
.dynamic-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.dynamic-card .card-header {
  margin-bottom: 20px;
}

.more-link {
  font-size: 13px;
  color: #0077b6;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.dynamic-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.dynamic-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
    padding-bottom: 0;
  }
}

.dynamic-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  
  &.blue-dot {
    background: #0077b6;
  }
  
  &.green-dot {
    background: #4caf50;
  }
  
  &.orange-dot {
    background: #ff9800;
  }
}

.dynamic-text {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.dynamic-time {
  font-size: 12px;
  color: #999;
}

/* 页脚 */
.footer {
  background: #1a1a2e;
  padding: 30px 40px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1600px;
  margin: 0 auto;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.footer-logo {
  font-size: 14px;
  color: #fff;
  font-weight: 500;
}

.footer-copyright {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.footer-links {
  display: flex;
  gap: 32px;
}

.footer-links a {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  
  &:hover {
    color: #fff;
  }
}

.footer-right {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .hero-content {
    flex-direction: column;
    align-items: stretch;
  }
  
  .hero-right {
    width: 100%;
  }
  
  .main-content {
    flex-direction: column;
  }
  
  .content-right {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 12px 20px;
  }
  
  .logo-text {
    display: none;
  }
  
  .nav-menu {
    display: none;
  }
  
  .hero-section {
    padding: 100px 20px 40px;
  }
  
  .hero-title {
    font-size: 32px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .hero-buttons {
    flex-direction: column;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .process-steps {
    flex-direction: column;
    gap: 16px;
  }
  
  .step-arrow {
    display: none;
  }
  
  .footer-content {
    flex-direction: column;
    text-align: center;
  }
  
  .footer-links {
    gap: 16px;
  }
}
</style>