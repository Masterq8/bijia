# 前后端集成说明

## ✅ 完成内容

### 1. 后端接口对接

**已实现的接口调用：**

1. **获取验证码** - `GET /captchaImage`
   - 返回验证码图片和 uuid
   - 图片和 uuid 自动更新到表单中

2. **登录接口** - `POST /login`
   - 请求参数：username, password, code(验证码), uuid
   - 响应：token (保存到 localStorage)
   - 错误处理：验证码错误自动刷新

3. **注册接口** - `POST /register`
   - 请求参数：username, password, email, phonenumber, identity, unit, code, uuid
   - 响应：成功后切换回登录页
   - 错误处理：失败后刷新验证码

### 2. 前端改造

**替换项目：**
- ❌ 前端本地生成的数学验证码
- ✅ 调用后端验证码接口获取验证码图片
- ✅ 前端负责验证码校验改为后端负责

**核心文件修改：**

| 文件 | 改动 |
|-----|------|
| `src/App.vue` | 调用后端接口，移除本地验证码生成 |
| `src/api/login.js` | 新建 API 模块 |
| `src/utils/auth.js` | Token 管理 (localStorage) |
| `.env` | 配置后端 API 地址 http://localhost:8066 |
| `vite.config.js` | 添加路径别名 @ |

### 3. 关键功能

#### 验证码
```js
// 自动获取验证码图片
async function refreshCaptcha() {
  const response = await getCodeImg()
  captchaInfo.img = response.img  // 验证码图片 base64
  captchaInfo.uuid = response.uuid // 验证码唯一标识
}
```

#### 登录流程
```js
1. 用户填写账号、密码、验证码
2. 点击登录按钮
3. 前端表单校验
4. 调用后端 POST /login
5. 保存 token 到 localStorage
6. 重定向到 /dashboard
```

#### 注册流程
```js
1. 用户填写注册信息
2. 点击注册按钮
3. 前端表单校验
4. 调用后端 POST /register
5. 成功后切换回登录页
6. 用户使用新账号登录
```

### 4. 后端地址配置

**文件：** `.env`
```env
VITE_APP_BASE_API = http://localhost:8066
```

**如需修改后端地址，编辑此文件后重启开发服务器**

### 5. Token 管理

**存储位置：** `localStorage.Authorization`

**自动在请求中添加：**
```js
// request.js 中自动添加 Authorization header
config.headers['Authorization'] = 'Bearer ' + getToken()
```

**作用：** 
- 登录成功后调用 `setToken(response.token)` 保存
- 后续请求自动携带 token
- 登出时调用 `removeToken()` 清除

## 🔧 开发调试

### 启动命令
```bash
# 开发环境
npm run dev

# 生产构建
npm run build

# 预览生产环境
npm run preview
```

### 调试步骤

1. **检查验证码加载**
   - 打开浏览器开发工具（F12）
   - Network 标签查看 `/captchaImage` 请求
   - 确认返回 200 状态码和图片数据

2. **测试登录**
   - 输入用户名、密码、验证码
   - 点击登录
   - Network 标签看 `/login` 请求
   - 确认返回 token

3. **测试注册**
   - 切换到注册页
   - 填写所有必填字段
   - Network 标签看 `/register` 请求
   - 成功后自动切换回登录页

### 常见问题

**Q: 验证码图片无法加载**
- A: 检查后端服务是否启动在 http://localhost:8066
- A: 检查 `.env` 中 VITE_APP_BASE_API 配置

**Q: 登录时提示网络错误**
- A: 确保后端 `/login` 接口可访问
- A: 检查浏览器控制台 Console 标签的错误信息

**Q: Token 无法保存**
- A: 检查浏览器是否禁用了 localStorage
- A: 查看 `src/utils/auth.js` 的实现

## 📝 表单字段映射

| 前端字段 | 后端字段 | 说明 |
|---------|---------|------|
| username | username | 用户名 |
| password | password | 密码 |
| code | code | 验证码 |
| uuid | uuid | 验证码唯一标识 |
| rememberMe | rememberMe | 记住密码（可选） |
| email | email | 邮箱 |
| phonenumber | phonenumber | 手机号 |
| unit | unit | 工作单位 |
| identity | identity | 主要身份 |
| confirmPassword | - | 确认密码（前端校验） |
| agree | - | 协议同意（前端校验） |

## 🚀 后续扩展

### 可添加功能
- [ ] 忘记密码功能
- [ ] 邮箱验证
- [ ] 手机验证码
- [ ] 第三方登录（微信、企业微信等）
- [ ] 登录日志
- [ ] 账户锁定（多次登录失败）

### API 预留
```js
// src/api/login.js 已预留的接口
getInfo()      // 获取用户信息
getRouters()   // 获取路由菜单
getDicts()     // 获取字典数据
```

## ✨ 技术栈

- **前端框架：** Vue 3.4.0 + Composition API
- **UI 组件库：** Element Plus 2.13.7
- **HTTP 客户端：** Axios 1.6.0
- **构建工具：** Vite 5.4.21
- **本地存储：** localStorage (Token)

## 📞 支持

如有问题，请检查：
1. 后端服务是否正常运行
2. 网络连接和防火墙设置
3. 浏览器缓存（Ctrl+Shift+Delete 清空缓存）
4. 浏览器控制台是否有错误日志
