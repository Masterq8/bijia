<template>
  <div class="new-home-container">
    <!-- 主视觉区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">海洋海岸带遥感影像智能解译云平台</h1>
        <p class="hero-subtitle">基于深度学习的海洋遥感影像智能解译系统</p>
        <div class="hero-buttons">
          <button class="primary-btn" @click="navigateTo('/home')">
            <span class="btn-icon">🗺️</span>
            进入平台
          </button>
          <button class="secondary-btn" @click="navigateTo('/quickStart')">
            <span class="btn-icon">📖</span>
            快速上手
          </button>
        </div>
      </div>
      <div class="hero-wave"></div>
    </section>

    <!-- 功能特性区域 -->
    <section class="features-section">
      <div class="section-title">核心功能</div>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">🗺️</div>
          <h3 class="feature-title">遥感影像检索</h3>
          <p class="feature-description">支持多源卫星影像数据的快速检索与可视化展示</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🧠</div>
          <h3 class="feature-title">AI智能解译</h3>
          <p class="feature-description">基于深度学习算法的海洋地物自动识别与分类</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📊</div>
          <h3 class="feature-title">数据分析</h3>
          <p class="feature-description">提供丰富的统计分析工具和可视化报表</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">☁️</div>
          <h3 class="feature-title">云端存储</h3>
          <p class="feature-description">安全可靠的云存储服务，支持海量数据管理</p>
        </div>
      </div>
    </section>

    <!-- 数据展示区域 -->
    <section class="data-section">
      <div class="section-title">数据资源</div>
      <div class="data-stats">
        <div class="stat-item">
          <div class="stat-value">500+</div>
          <div class="stat-label">卫星影像</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">10TB+</div>
          <div class="stat-label">数据存储</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">99.9%</div>
          <div class="stat-label">服务可用性</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">24/7</div>
          <div class="stat-label">全天候服务</div>
        </div>
      </div>
    </section>

    <!-- 合作伙伴区域 -->
    <section class="partners-section">
      <div class="section-title">合作伙伴</div>
      <div class="partners-list">
        <div class="partner-item">青岛黄海学院</div>
        <div class="partner-item">华为云</div>
        <div class="partner-item">AICC</div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-info">
          <span>鲁ICP备2024052761号</span>
          <span>鲁公网安备37021002001371号</span>
        </div>
        <div class="footer-copyright">青岛山科人工智能有限公司 © 2024</div>
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
}

/* 主视觉区域 */
.hero-section {
  background: linear-gradient(180deg, #0a192f 0%, #112240 50%, #1d3557 100%);
  padding: 120px 60px 100px;
  position: relative;
  overflow: hidden;
}

.hero-content {
  text-align: center;
  max-width: 1200px;
  margin: 0 auto;
}

.hero-title {
  font-size: clamp(36px, 6vw, 64px);
  font-weight: 700;
  color: #fff;
  margin-bottom: 20px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  letter-spacing: 2px;
}

.hero-subtitle {
  font-size: clamp(16px, 2vw, 22px);
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 40px;
  letter-spacing: 1px;
}

.hero-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
  flex-wrap: wrap;
}

.primary-btn, .secondary-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 36px;
  font-size: 18px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.primary-btn {
  background: linear-gradient(135deg, #64ffda 0%, #00d4aa 100%);
  border: none;
  color: #0a192f;
  font-weight: 600;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 30px rgba(100, 255, 218, 0.3);
  }
}

.secondary-btn {
  background: transparent;
  border: 2px solid rgba(255, 255, 255, 0.5);
  color: #fff;
  
  &:hover {
    background: rgba(255, 255, 255, 0.1);
    border-color: #fff;
    transform: translateY(-3px);
  }
}

.btn-icon {
  font-size: 20px;
}

.hero-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100px;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1440 320'%3E%3Cpath fill='%23ffffff' fill-opacity='1' d='M0,224L48,213.3C96,203,192,181,288,181.3C384,181,480,203,576,213.3C672,224,768,224,864,213.3C960,203,1056,181,1152,181.3C1248,181,1344,203,1392,213.3L1440,224L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z'%3E%3C/path%3E%3C/svg%3E");
  background-size: cover;
}

/* 功能特性区域 */
.features-section {
  background: #ffffff;
  padding: 80px 60px;
}

.section-title {
  text-align: center;
  font-size: 32px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 60px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.feature-card {
  background: #f8fafc;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  }
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #64ffda 0%, #00d4aa 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  font-size: 28px;
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 12px;
}

.feature-description {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
}

/* 数据展示区域 */
.data-section {
  background: #f8fafc;
  padding: 80px 60px;
}

.data-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.stat-item {
  text-align: center;
  padding: 30px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.stat-value {
  font-size: 48px;
  font-weight: 700;
  color: #0a192f;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
}

/* 合作伙伴区域 */
.partners-section {
  background: #ffffff;
  padding: 80px 60px;
}

.partners-list {
  display: flex;
  justify-content: center;
  gap: 60px;
  flex-wrap: wrap;
}

.partner-item {
  font-size: 20px;
  font-weight: 500;
  color: #475569;
  padding: 16px 32px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

/* 页脚 */
.footer {
  background: #0a192f;
  padding: 40px 60px;
}

.footer-content {
  text-align: center;
}

.footer-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.footer-info span {
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.footer-copyright {
  color: rgba(255, 255, 255, 0.4);
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-section {
    padding: 120px 20px 80px;
  }
  
  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .primary-btn, .secondary-btn {
    width: 100%;
    max-width: 280px;
    justify-content: center;
  }
  
  .features-section, .data-section, .partners-section {
    padding: 50px 20px;
  }
  
  .partners-list {
    gap: 20px;
  }
  
  .partner-item {
    font-size: 16px;
    padding: 12px 24px;
  }
  
  .footer {
    padding: 30px 20px;
  }
}
</style>