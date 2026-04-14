# 🚀 前后端集成快速开始

## 1️⃣ 前置准备

### 后端环境
```bash
# 确保后端服务已启动
# 监听地址：http://localhost:8066
# 需要实现以下接口：
# - GET  /captchaImage      (验证码)
# - POST /login             (登录)
# - POST /register          (注册)
```

### 前端环境
```bash
# Node.js 版本：>= 16
# npm 版本：>= 8
```

## 2️⃣ 快速启动

### 开发模式 (推荐)
```bash
cd h:\VScode_project\bijia\bijia

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 浏览器自动打开 http://localhost:5173
```

### 生产构建
```bash
# 编译生产版本
npm run build

# 输出目录：dist/
# 可通过任何 Web 服务器（nginx、Apache等）部署
```

## 3️⃣ 接口信息速查

### 验证码接口
```
GET /captchaImage

响应示例：
{
  "code": 200,
  "img": "data:image/gif;base64,...",
  "uuid": "abc123def456"
}
```

### 登录接口
```
POST /login

请求体：
{
  "username": "admin",
  "password": "password123",
  "code": "1234",
  "uuid": "abc123def456"
}

响应示例：
{
  "code": 200,
  "msg": "操作成功",
  "token": "eyJhbGc..."
}
```

### 注册接口
```
POST /register

请求体：
{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com",
  "phonenumber": "13800138000",
  "identity": "admin",
  "unit": "公司名称",
  "code": "1234",
  "uuid": "abc123def456"
}

响应示例：
{
  "code": 200,
  "msg": "操作成功"
}
```

## 4️⃣ 核心文件速查

```
src/
├── App.vue                  # 主登录/注册页面（含UI和交互）
├── main.js                  # 应用入口
├── api/
│   └── login.js            # 登录/注册 API 接口
├── utils/
│   ├── request.js          # Axios 实例和拦截器 ⭐ 重要
│   ├── auth.js             # Token 管理
│   ├── errorCode.js        # 错误信息映射
│   └── ruoyi.js            # 工具函数
├── plugins/
│   └── cache.js            # 缓存模块
└── store/
    └── modules/
        └── user.js         # 用户 Store

.env                        # 环境变量配置 ⭐ 重要
vite.config.js              # Vite 配置
```

## 5️⃣ 配置修改

### 修改后端地址
编辑 `.env` 文件：
```env
VITE_APP_BASE_API = http://localhost:8066  # 改成实际后端地址
```

### 修改验证码逻辑
如需上传验证码，修改 `src/api/login.js` 的 `getCodeImg()` 方法

### 修改登录跳转
编辑 `src/App.vue` 中的 `handleLogin()` 函数：
```js
// 当前跳转到 /dashboard，可改为自己的路径
setTimeout(() => {
  window.location.href = '/dashboard'  // 改成你的首页路径
}, 1000)
```

## 6️⃣ 常见问题解决

### 问题 1：验证码无法加载
```
错误信息：GET /captchaImage 404
原因：后端服务未启动或路由错误
解决方案：
1. 确保后端服务启动在 http://localhost:8066
2. 检查后端是否实现了 /captchaImage 接口
3. 打开浏览器 F12 → Network，查看请求详情
```

### 问题 2：登录失败，提示网络错误
```
解决方案：
1. 检查 CORS 配置（跨域资源共享）
2. 查看浏览器 F12 → Console 的错误日志
3. 确认后端返回格式符合文档要求
```

### 问题 3：Token 无法保存
```
原因：localStorage 被禁用或浏览器隐私模式
解决方案：
1. 在正常浏览模式打开（非隐私/无痕模式）
2. 检查浏览器设置是否禁用了存储
```

## 7️⃣ 测试流程

### 完整登录测试
```
1. 打开 http://localhost:5173
2. 检查验证码图片是否显示
3. 输入用户名、密码、验证码
4. 点击登录
5. F12 → Network 看请求是否成功
6. 登录成功后应该重定向到 /dashboard
```

### 完整注册测试
```
1. 打开 http://localhost:5173
2. 点击"立即注册"
3. 填写所有字段（注意邮箱格式）
4. 勾选协议同意
5. 点击注册
6. 注册成功后自动返回登录页
7. 用新账号登录验证
```

## 8️⃣ 性能优化建议

### 现有优化
- ✅ 验证码图片动态加载（减少初始包体积）
- ✅ 表单验证前在浏览器端（减少无效请求）
- ✅ 请求去重处理（防止重复提交）

### 进一步优化空间
- 📦 分开打包 Element Plus 组件
- 🖼️ 验证码图片压缩
- ⚡ 添加请求缓存策略
- 🔄 注册成功自动登录

## 9️⃣ 调试技巧

### 浏览器 Console 调试命令
```js
// 查看当前 token
localStorage.getItem('Authorization')

// 清除 token（模拟登出）
localStorage.removeItem('Authorization')

// 查看 API 地址
fetch('http://localhost:8066/captchaImage')
  .then(r => r.json())
  .then(d => console.log(d))
```

### 启用详细日志
编辑 `src/utils/request.js`，取消注释特定行以看到详细的请求日志

## 🔟 部署清单

部署到生产环境前：
- [ ] 更新 `.env` 的 `VITE_APP_BASE_API` 为正式服务器地址
- [ ] 执行 `npm run build` 生成 dist/ 目录
- [ ] 检查 HTTPS 证书配置
- [ ] 配置 CORS 头允许前端跨域访问
- [ ] 配置 Web 服务器（nginx/Apache）指向 dist/index.html
- [ ] 测试 Token 过期时的重新登录流程
- [ ] 监控错误日志（ES 或其他日志系统）

## 📞 技术支持

如遇问题，按优先级检查：
1. 后端服务状态（curl 测试接口）
2. 网络连接和防火墙
3. 浏览器开发工具错误信息
4. `.env` 配置是否正确
5. 前端依赖是否完整安装

---

**开发环境：** Windows 11 / VS Code  
**构建工具：** Vite 5.4.21  
**最后更新：** 2026-04-14
