<template>
  <div class="app-container">
    <div class="order-management">
      <div class="page-header">
        <h2>订单管理</h2>
        <p>管理平台订单和交易记录</p>
      </div>
      <div class="content-area">
        <el-empty description="订单管理功能开发中" :image-size="200">
          <template #image>
            <div class="empty-icon">📋</div>
          </template>
          <template #description>
            <p>订单管理功能正在开发中，敬请期待</p>
          </template>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup name="orderManagement">
// 订单管理页面 - 功能开发中
</script>

<style scoped lang="scss">
.order-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
  
  h2 {
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary, #1f2937);
  }
  
  p {
    margin: 0;
    color: var(--text-secondary, #6b7280);
    font-size: 14px;
  }
}

.content-area {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background: var(--bg-tertiary, #f9fafb);
  border-radius: 12px;
  border: 1px solid var(--border-color, #e5e7eb);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}
</style>
